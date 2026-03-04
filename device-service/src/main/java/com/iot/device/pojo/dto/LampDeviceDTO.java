package com.iot.device.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/16 13:23
 * @Version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LampDeviceDTO {

    /**
     * 设备唯一码
     * */
    private String ieme;

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

    /**
     * 用户设备别名
     * */
    private String userNickname;

    /**
     * 用户ID
     * */
    private Long userId;

    /**
     * 开关状态 0 关 1 开
     * */
    private Integer onSwitch;

    /**
     * 网络状态 0 断开 1 连接
     * */
    private Integer networking;
}
