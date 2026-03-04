package com.iot.device.pojo.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/16 17:47
 * @Version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LampControlReq {

    /**
     * 用户ID
     * */
    private Long userId;

    /**
     * 设备唯一码
     * */
    private String ieme;

    /**
     * 灯开关 0 开 1 关
     * */
    private Integer onSwitch;

    /**
     * 亮度
     * */
    private Integer brightness;

    /**
     * 色温
     * */
    private Integer colorTemp;
}
