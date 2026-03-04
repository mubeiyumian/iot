package com.iot.device.pojo.dto;

import com.iot.device.jpa.JpaDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/24 22:34
 * @Version: 1.0
 */

@JpaDTO
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IotProductDTO {

    /**
     * 产品ID
     * */
    private Long productId;

    /**
     * 产品唯一标识
     */
    private String productKey;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 设备分类
     */
    private String category;

    /**
     * redis前缀
     */
    private String redisPrefix;

    /**
     * 描述
     */
    private String description;

    /**
     * 设备sn
     * */
    private String sn;
}
