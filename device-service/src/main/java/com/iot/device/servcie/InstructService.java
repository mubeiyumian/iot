package com.iot.device.servcie;

import com.alibaba.fastjson.JSONObject;
import com.iot.common.pojo.entity.IotDevice;
import com.iot.common.pojo.entity.SysUser;
import com.iot.device.client.UserClient;
import com.iot.device.exception.BusinessException;
import com.iot.device.mqtt.MqttSendMessage;
import com.iot.device.pojo.dto.IotProductDTO;
import com.iot.device.pojo.req.InstructReq;
import com.iot.device.pojo.req.UserInfoReq;
import com.iot.device.repository.IotDeviceBindingRepository;
import com.iot.device.repository.IotDeviceRepository;
import com.iot.device.repository.IotProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/16 17:49
 * @Version: 1.0
 */

@Service
public class InstructService {

    @Autowired
    private MqttSendMessage mqttSendMessage;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private UserClient userClient;

    @Autowired
    private IotDeviceRepository iotDeviceRepository;

    @Autowired
    private IotDeviceBindingRepository iotDeviceBindingRepository;

    @Autowired
    private IotProductRepository iotProductRepository;

    public void lampControl(InstructReq req) {
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
            throw new BusinessException("核对序列号");
        }
        Boolean binding = iotDeviceBindingRepository.existsByUserIdAndDeviceId(req.getUserId(), iotDevice.getId());
        if (!binding) {
            throw new BusinessException("设备未绑定");
        }
        Integer connected = (Integer) redisTemplate.opsForHash().get("iot:device:" + iotDevice.getDeviceSn(), "connected");
        if (connected == 0) {
            throw new BusinessException("设备未联网");
        }
        IotProductDTO productDTO = iotProductRepository.findByDeviceSn(req.getSn());
        Integer onSwitch = (Integer) redisTemplate.opsForHash().get("iot:device:" + iotDevice.getDeviceSn(), "onSwitch");
        JSONObject messageBody = new JSONObject();
        messageBody.put("sn", iotDevice.getDeviceSn());
        messageBody.put("operateType", "CONTROL");
        messageBody.put("timestamp", System.currentTimeMillis());
        if (!Objects.equals(onSwitch, req.getOnSwitch())) {
            String switchStatus = onSwitch == 0 ? "on" : "off";
            messageBody.put("params", switchStatus);
            //下发指令
            mqttSendMessage.sendMessage("lamp/control/" + iotDevice.getDeviceSn(), messageBody.toJSONString());
            redisTemplate.opsForHash().put("iot:device:" + iotDevice.getDeviceSn(), "onSwitch", req.getOnSwitch());
            redisTemplate.opsForHash().put("iot:device:" + iotDevice.getDeviceSn(), "timestamp", System.currentTimeMillis());
            return;
        }
        if (req.getOnSwitch() == 0) {
            return;
        }
        String key = productDTO.getRedisPrefix() + iotDevice.getDeviceSn();
        if (req.getBrightness() != null && req.getAdjustParam() == 0) {
            messageBody.put("params", "brightness");
            messageBody.put("brightness", req.getBrightness());
            mqttSendMessage.sendMessage("lamp/control/" + iotDevice.getDeviceSn(), messageBody.toJSONString());
            redisTemplate.opsForHash().put(key, "brightness", req.getBrightness());
            return;
        }
        if (req.getWarmCurrent() != null && req.getColdCurrent() != null && req.getAdjustParam() == 1) {
            messageBody.put("params", "current");
            messageBody.put("warmCurrent", req.getWarmCurrent());
            messageBody.put("coldCurrent", req.getColdCurrent());
            mqttSendMessage.sendMessage("lamp/control/" + iotDevice.getDeviceSn(), messageBody.toJSONString());
            redisTemplate.opsForHash().put(key, "warmCurrent", req.getWarmCurrent());
            redisTemplate.opsForHash().put(key, "coldCurrent", req.getColdCurrent());
        }
    }
}
