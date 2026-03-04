package com.iot.device.cron.dto;

import com.iot.common.jpa.JpaDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/24 23:38
 * @Version: 1.0
 */

@JpaDTO
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorCronDTO {

    /**
     * redisPrefix
     * */
    private String redisPrefix;

    /**
     * sn
     * */
    private String deviceSn;
}
