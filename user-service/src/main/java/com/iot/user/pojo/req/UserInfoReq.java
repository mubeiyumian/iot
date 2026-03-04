package com.iot.user.pojo.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/16 11:54
 * @Version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoReq {

    /**
     * 用户ID
     * */
    private Long userId;
}
