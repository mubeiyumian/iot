package com.iot.common.pojo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @Author: Kai
 * @Description: 设备实例
 * @Date: 2026/02/23 23:18
 * @Version: 1.0
 */
@Getter
@Setter
@Entity
@Table(name = "iot_device")
@EqualsAndHashCode(callSuper = true)
public class IotDevice extends BaseEntity {

    /**
     * 产品ID
     */
    @Column(name = "product_id")
    private Long productId;

    /**
     * 设备序列号
     */
    @Column(name = "device_sn")
    private String deviceSn;

    /**
     * 设备名称
     */
    @Column(name = "device_name")
    private String deviceName;

    /**
     * 状态 0未激活 1在线 2离线
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 激活时间
     */
    @Column(name = "activate_time")
    private LocalDateTime activateTime;
}
