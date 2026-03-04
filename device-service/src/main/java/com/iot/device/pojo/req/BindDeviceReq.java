package com.iot.device.pojo.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/16 13:09
 * @Version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BindDeviceReq {

    /**
     * 用户ID
     * */
    private Long userId;

    /**
     * 设备唯一码
     * */
    private String sn;
}
