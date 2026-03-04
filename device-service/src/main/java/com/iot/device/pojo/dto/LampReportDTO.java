package com.iot.device.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/19 12:27
 * @Version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LampReportDTO {

    /**
     * 设备唯一码
     * */
    private String ieme;

    /**
     * 设备序列号
     * */
    private String sn;

    /**
     * 连接状态
     * */
    private Long connected;

    /**
     * 时间戳
     * */
    private Long timestamp;
}
