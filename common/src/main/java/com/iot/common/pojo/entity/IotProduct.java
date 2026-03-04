package com.iot.common.pojo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Kai
 * @Description: 产品定义
 * @Date: 2026/02/23 23:17
 * @Version: 1.0
 */
@Getter
@Setter
@Entity
@Table(name = "iot_product")
@EqualsAndHashCode(callSuper = true)
public class IotProduct extends BaseEntity {

    /**
     * 产品唯一标识
     */
    @Column(name = "product_key")
    private String productKey;

    /**
     * 产品名称
     */
    @Column(name = "product_name")
    private String productName;

    /**
     * 设备分类
     */
    @Column(name = "category")
    private String category;

    /**
     * redis前缀
     */
    @Column(name = "redis_prefix")
    private String redisPrefix;

    /**
     * 描述
     */
    @Column(name = "description")
    private String description;
}
