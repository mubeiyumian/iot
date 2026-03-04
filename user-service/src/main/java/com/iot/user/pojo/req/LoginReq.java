package com.iot.user.pojo.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/16 02:50
 * @Version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginReq {

    /**
     * 手机号
     * */
    private String phone;
}
