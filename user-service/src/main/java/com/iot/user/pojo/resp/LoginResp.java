package com.iot.user.pojo.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/16 02:53
 * @Version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResp {

    /**
     * 用户ID
     * */
    private Long userId;

    /**
     * 用户名
     * */
    private String username;

    /**
     * 手机号
     * */
    private String phone;

    /**
     * token
     * */
    private String token;
}
