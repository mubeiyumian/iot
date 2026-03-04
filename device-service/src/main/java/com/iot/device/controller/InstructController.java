package com.iot.device.controller;

import com.iot.device.pojo.req.InstructReq;
import com.iot.device.pojo.resp.Response;
import com.iot.device.servcie.InstructService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/24 23:53
 * @Version: 1.0
 */

@RestController
public class InstructController {

    @Autowired
    private InstructService instructService;

    /**
     * 智能灯指令下发
     * */
    @PostMapping("/lampControl")
    public Response<Void> lampControl(@RequestBody InstructReq req) {
        instructService.lampControl(req);
        return Response.success();
    }
}
