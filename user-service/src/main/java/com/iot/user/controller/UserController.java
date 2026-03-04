package com.iot.user.controller;

import com.iot.user.pojo.req.LoginReq;
import com.iot.user.pojo.req.UserInfoReq;
import com.iot.user.pojo.resp.LoginResp;
import com.iot.user.pojo.resp.Response;
import com.iot.user.pojo.resp.UserInfoResp;
import com.iot.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/16 02:49
 * @Version: 1.0
 */

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Response<LoginResp> login(@RequestBody LoginReq loginReq) {
        return Response.success(userService.login(loginReq));
    }

    @PostMapping("/getUserInfo")
    public UserInfoResp getUserInfo(@RequestBody UserInfoReq req) {
        return userService.getUserInfo(req);
    }
}
