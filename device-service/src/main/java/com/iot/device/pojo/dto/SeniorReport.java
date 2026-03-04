package com.iot.device.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/21 18:11
 * @Version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeniorReport {

    /**
     * 序列号
     * */
    private String sn;

    /**
     * 温度
     * */
    private Double temperature;

    /**
     * 湿度
     * */
    private Double humidity;

    /**
     * 时间戳
     * */
    private Long timestamp;
}
