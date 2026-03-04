package com.iot.device.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/16 20:55
 * @Version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LampDTO {

    /**
     * 亮度初始值
     * */
    private Integer brightnessMin;

    /**
     * 亮度最高值
     * */
    private Integer brightnessMax;

    /**
     * 亮度
     * */
    private Integer brightness;

    /**
     * 暖关电流
     * */
    private Integer warmCurrent;

    /**
     * 冷光电流
     * */
    private Integer coldCurrent;

    /**
     * 联网状态
     * */
    private Integer connected;

    /**
     * 开关状态
     * */
    private Integer onSwitch;
}
