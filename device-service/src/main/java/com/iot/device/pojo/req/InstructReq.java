package com.iot.device.pojo.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/16 21:06
 * @Version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstructReq {

    /**
     * 用户ID
     * */
    private Long userId;

    /**
     * 设备唯一码
     * */
    private String sn;

    /**
     * 开关
     * */
    private Integer onSwitch;

    /**
     * 亮度
     * */
    private Integer brightness;

    /**
     * 暖光电流
     * */
    private Integer warmCurrent;

    /**
     * 冷光电流
     * */
    private Integer coldCurrent;

    /**
     * 调整参数 0 亮度 1 色温
     * */
    private Integer adjustParam;
}
