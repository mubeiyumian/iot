package com.iot.device.servcie;

import com.iot.common.pojo.entity.IotDevice;
import com.iot.common.pojo.entity.IotDeviceBinding;
import com.iot.common.pojo.entity.SysUser;
import com.iot.device.client.UserClient;
import com.iot.device.exception.BusinessException;
import com.iot.device.pojo.dto.IotProductDTO;
import com.iot.device.pojo.req.BindDeviceReq;
import com.iot.device.pojo.req.DeviceInfoReq;
import com.iot.device.pojo.req.UserInfoReq;
import com.iot.device.pojo.resp.IotProductResp;
import com.iot.device.pojo.resp.LampParamResp;
import com.iot.device.pojo.resp.SeniorResp;
import com.iot.device.repository.IotDeviceBindingRepository;
import com.iot.device.repository.IotDeviceRepository;
import com.iot.device.repository.IotProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/24 22:28
 * @Version: 1.0
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class DeviceService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private IotProductRepository iotProductRepository;

    @Autowired
    private IotDeviceRepository iotDeviceRepository;

    @Autowired
    private IotDeviceBindingRepository iotDeviceBindingRepository;

    @Autowired
    private UserClient userClient;

    public IotProductResp getDeviceInfo(DeviceInfoReq req) {
        SysUser user = userClient.getUserInfo(new UserInfoReq(req.getUserId()));
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        IotProductDTO productDTO = iotProductRepository.findByDeviceSn(req.getSn());
        if (productDTO == null) {
            throw new BusinessException("请核对序列号");
        }
        IotProductResp resp = new IotProductResp();
        BeanUtils.copyProperties(productDTO, resp);
        return resp;
    }

    public void bindDevice(BindDeviceReq req) {
        if (req.getUserId() == null) {
            throw new BusinessException("用户ID不能为空");
        }
        if (req.getSn().isEmpty()) {
            throw new BusinessException("设备唯一码不能为空");
        }
        SysUser user = userClient.getUserInfo(new UserInfoReq(req.getUserId()));
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        IotDevice iotDevice = iotDeviceRepository.findByDeviceSn(req.getSn()).orElse(null);
        if (iotDevice == null) {
            throw new BusinessException("设备不存在，核对序列号");
        }
        if (iotDevice.getStatus() != 0) {
            throw new BusinessException("设备已激活");
        }
        Boolean isbind = iotDeviceBindingRepository.existsByUserIdAndDeviceId(req.getUserId(), iotDevice.getId());
        if (isbind) {
            throw new BusinessException("设备已绑定");
        }
        LocalDateTime now = LocalDateTime.now();
        //原设备状态更改
        iotDevice.setActivateTime(now);
        //在线
        iotDevice.setStatus(1);
        //添加绑定设备
        IotDeviceBinding binding = new IotDeviceBinding();
        binding.setUserId(user.getId());
        binding.setDeviceId(iotDevice.getId());
        binding.setBindingTime(now);
        //已绑定
        binding.setStatus(1);
        iotDeviceBindingRepository.save(binding);
        iotDeviceRepository.save(iotDevice);
        //更新redis设备联网状态和开关状态
        redisTemplate.opsForHash().put("iot:device:" + iotDevice.getDeviceSn(), "connected", 1);
        redisTemplate.opsForHash().put("iot:device:" + iotDevice.getDeviceSn(), "onSwitch", 0);
        redisTemplate.opsForHash().put("iot:device:" + iotDevice.getDeviceSn(), "timestamp", System.currentTimeMillis());
    }

    public List<IotProductResp> ownerDevice(UserInfoReq req) {
        if (req.getUserId() == null) {
            throw new BusinessException("用户ID不能为空");
        }
        SysUser user = userClient.getUserInfo(req);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        List<IotDeviceBinding> bindingList = iotDeviceBindingRepository.findByUserId(req.getUserId());
        if (bindingList.isEmpty()) {
            return new ArrayList<>();
        }
        Set<Long> deviceIds = bindingList.stream().map(IotDeviceBinding::getDeviceId).collect(Collectors.toSet());
        List<IotProductDTO> iotProductDTOS = iotProductRepository.findByDeviceIdIn(deviceIds);
        List<IotProductResp> respList = new ArrayList<>();
        for (IotProductDTO productDTO : iotProductDTOS) {
            IotProductResp resp = new IotProductResp();
            BeanUtils.copyProperties(productDTO, resp);
            respList.add(resp);
        }
        return respList;
    }

    public LampParamResp lampParam(DeviceInfoReq req) {
        if (req.getUserId() == null) {
            throw new BusinessException("用户ID不能为空");
        }
        if (req.getSn().isEmpty()) {
            throw new BusinessException("设备唯一码不能为空");
        }
        SysUser user = userClient.getUserInfo(new UserInfoReq(req.getUserId()));
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        IotDevice iotDevice = iotDeviceRepository.findByDeviceSn(req.getSn()).orElse(null);
        if (iotDevice == null) {
            throw new BusinessException("设备不存在，核对序列号");
        }
        //检查绑定关系
        Boolean isBinding = iotDeviceBindingRepository.existsByUserIdAndDeviceId(user.getId(), iotDevice.getId());
        if (!isBinding) {
            throw new BusinessException("设备未绑定");
        }
        //联网查询
        Integer connected = (Integer) redisTemplate.opsForHash().get("iot:device:" + iotDevice.getDeviceSn(), "connected");
//        if (connected == 0) {
//            throw new BusinessException("设备未联网");
//        }
        Integer onSwitch = (Integer) redisTemplate.opsForHash().get("iot:device:" + iotDevice.getDeviceSn(), "onSwitch");
        LampParamResp resp = new LampParamResp();
        IotProductDTO productDTO = iotProductRepository.findByDeviceSn(req.getSn());
        if (!redisTemplate.hasKey(productDTO.getRedisPrefix() + iotDevice.getDeviceSn())) {
            //第一次初始化需要查询预置参数，再添加到redis中
            Integer brightnessMin = (Integer) redisTemplate.opsForHash().get(productDTO.getRedisPrefix() + "000000", "brightnessMin");
            Integer brightnessMax = (Integer) redisTemplate.opsForHash().get(productDTO.getRedisPrefix() + "000000", "brightnessMax");
            Integer brightness = (Integer) redisTemplate.opsForHash().get(productDTO.getRedisPrefix() + "000000", "brightness");
            Integer warmCurrent = (Integer) redisTemplate.opsForHash().get(productDTO.getRedisPrefix() + "000000", "warmCurrent");
            Integer coldCurrent = (Integer) redisTemplate.opsForHash().get(productDTO.getRedisPrefix() + "000000", "coldCurrent");
            resp = new LampParamResp(brightnessMin, brightnessMax, brightness, warmCurrent, coldCurrent, connected, onSwitch);
            redisTemplate.opsForHash().put(productDTO.getRedisPrefix() + iotDevice.getDeviceSn(), "brightnessMin", resp.getBrightnessMin());
            redisTemplate.opsForHash().put(productDTO.getRedisPrefix() + iotDevice.getDeviceSn(), "brightnessMax", resp.getBrightnessMax());
            redisTemplate.opsForHash().put(productDTO.getRedisPrefix() + iotDevice.getDeviceSn(), "brightness", resp.getBrightness());
            redisTemplate.opsForHash().put(productDTO.getRedisPrefix() + iotDevice.getDeviceSn(), "warmCurrent", resp.getWarmCurrent());
            redisTemplate.opsForHash().put(productDTO.getRedisPrefix() + iotDevice.getDeviceSn(), "coldCurrent", resp.getColdCurrent());
        }else {
            Integer brightnessMin = (Integer) redisTemplate.opsForHash().get(productDTO.getRedisPrefix() + iotDevice.getDeviceSn(), "brightnessMin");
            Integer brightnessMax = (Integer) redisTemplate.opsForHash().get(productDTO.getRedisPrefix() + iotDevice.getDeviceSn(), "brightnessMax");
            Integer brightness = (Integer) redisTemplate.opsForHash().get(productDTO.getRedisPrefix() + iotDevice.getDeviceSn(), "brightness");
            Integer warmCurrent = (Integer) redisTemplate.opsForHash().get(productDTO.getRedisPrefix() + iotDevice.getDeviceSn(), "warmCurrent");
            Integer coldCurrent = (Integer) redisTemplate.opsForHash().get(productDTO.getRedisPrefix() + iotDevice.getDeviceSn(), "coldCurrent");
            resp = new LampParamResp(brightnessMin, brightnessMax, brightness, warmCurrent, coldCurrent, connected, onSwitch);
        }
        return resp;
    }

    public SeniorResp seniorParam(DeviceInfoReq req) {
        if (req.getUserId() == null) {
            throw new BusinessException("用户ID不能为空");
        }
        if (req.getSn().isEmpty()) {
            throw new BusinessException("设备唯一码不能为空");
        }
        SysUser user = userClient.getUserInfo(new UserInfoReq(req.getUserId()));
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        IotDevice iotDevice = iotDeviceRepository.findByDeviceSn(req.getSn()).orElse(null);
        if (iotDevice == null) {
            throw new BusinessException("设备不存在，核对序列号");
        }
        //检查绑定关系
        Boolean isBinding = iotDeviceBindingRepository.existsByUserIdAndDeviceId(user.getId(), iotDevice.getId());
        if (!isBinding) {
            throw new BusinessException("设备未绑定");
        }
        //联网查询
        Integer connected = (Integer) redisTemplate.opsForHash().get("iot:device:" + iotDevice.getDeviceSn(), "connected");
//        if (connected == 0) {
//            throw new BusinessException("设备未联网");
//        }
        Integer onSwitch = (Integer) redisTemplate.opsForHash().get("iot:device:" + iotDevice.getDeviceSn(), "onSwitch");
        IotProductDTO productDTO = iotProductRepository.findByDeviceSn(req.getSn());
        SeniorResp resp = new SeniorResp();
        Double temperature = (Double) redisTemplate.opsForHash().get(productDTO.getRedisPrefix() + iotDevice.getDeviceSn(), "temperature");
        Double humidity = (Double) redisTemplate.opsForHash().get(productDTO.getRedisPrefix() + iotDevice.getDeviceSn(), "humidity");
        if (temperature == null || humidity == null) {
            throw new BusinessException("设备暂时无数据，请稍后再试");
        }
        resp.setTemperature(temperature);
        resp.setHumidity(humidity);
        resp.setConnected(connected);
        resp.setOnSwitch(onSwitch);
        return resp;
    }
}
