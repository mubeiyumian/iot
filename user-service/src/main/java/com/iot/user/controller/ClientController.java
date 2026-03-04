package com.iot.user.controller;

import com.iot.common.pojo.entity.SysUser;
import com.iot.user.pojo.req.UserInfoReq;
import com.iot.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/24 22:20
 * @Version: 1.0
 */

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private UserService userService;

    @PostMapping("/getUserInfo")
    public SysUser getUserInfo(@RequestBody UserInfoReq req) {
        return userService.clientUserInfo(req);
    }
}
