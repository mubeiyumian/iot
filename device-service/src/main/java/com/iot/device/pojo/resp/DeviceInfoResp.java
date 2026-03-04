package com.iot.device.pojo.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/16 12:49
 * @Version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceInfoResp {

    /**
     * 设备ID
     * */
    private Long deviceId;

    /**
     * 设备序列号
     * */
    private String sn;

    /**
     * 设备名称
     * */
    private String name;

    /**
     * 设备别名
     * */
    private String nickname;
}
