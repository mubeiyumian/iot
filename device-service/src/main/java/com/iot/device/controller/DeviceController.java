package com.iot.device.controller;

import com.iot.device.pojo.req.BindDeviceReq;
import com.iot.device.pojo.req.DeviceInfoReq;
import com.iot.device.pojo.req.UserInfoReq;
import com.iot.device.pojo.resp.IotProductResp;
import com.iot.device.pojo.resp.LampParamResp;
import com.iot.device.pojo.resp.Response;
import com.iot.device.pojo.resp.SeniorResp;
import com.iot.device.servcie.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/24 23:33
 * @Version: 1.0
 */

@RestController
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    /**
     * 获取设备基本信息
     * */
    @PostMapping("/getDeviceInfo")
    public Response<IotProductResp> getDeviceInfo(@RequestBody DeviceInfoReq req) {
        return Response.success(deviceService.getDeviceInfo(req));
    }

    /**
     * 绑定设备
     * */
    @PostMapping("/bindDevice")
    public Response<Void> bindDevice(@RequestBody BindDeviceReq req) {
        deviceService.bindDevice(req);
        return Response.success();
    }

    /**
     * 获取用户设备列表
     * */
    @PostMapping("/ownerDevice")
    public Response<List<IotProductResp>> ownerDevice(@RequestBody UserInfoReq req) {
        return Response.success(deviceService.ownerDevice(req));
    }

    /**
     * 获取智能灯参数
     * */
    @PostMapping("/lampParam")
    public Response<LampParamResp> lampParam(@RequestBody DeviceInfoReq req) {
        return Response.success(deviceService.lampParam(req));
    }

    /**
     * 获取感应器参数
     * */
    @PostMapping("/sensorParam")
    public Response<SeniorResp> sensorParam(@RequestBody DeviceInfoReq req) {
        return Response.success(deviceService.seniorParam(req));
    }
}
