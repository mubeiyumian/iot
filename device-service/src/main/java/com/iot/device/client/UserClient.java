package com.iot.device.client;

import com.iot.common.pojo.entity.SysUser;
import com.iot.device.pojo.req.UserInfoReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/16 12:44
 * @Version: 1.0
 */

@FeignClient(name = "gateway-service", url = "http://localhost:9000")
public interface UserClient {

    @PostMapping("/user/client/getUserInfo")
    SysUser getUserInfo(@RequestBody UserInfoReq req);
}
