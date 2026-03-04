package com.iot.device.pojo.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/21 18:25
 * @Version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeniorResp {

    /**
     * 温度
     * */
    private Double temperature;

    /**
     * 湿度
     * */
    private Double humidity;

    /**
     * 是否联网
     * */
    private Integer connected;

    /**
     * 是否开关
     * */
    private Integer onSwitch;
}
