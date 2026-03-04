package com.iot.device.pojo.report;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/16 22:28
 * @Version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LampReport {

    /**
     * 设备序列号
     * */
    private String sn;
    // 数值字段
    @JSONField(name = "onSwitch")
    private Integer onSwitch;
    @JSONField(name = "warmCurrent")
    private Integer warmCurrent;
    @JSONField(name = "coldCurrent")
    private Integer coldCurrent;
    @JSONField(name = "brightness")
    private Integer brightness;
    // 设备上报的时间戳（毫秒）
    private Long timestamp;
}
