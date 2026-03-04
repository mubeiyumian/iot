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
 * @Description: 设备绑定关系
 * @Date: 2026/02/23 23:18
 * @Version: 1.0
 */
@Getter
@Setter
@Entity
@Table(name = "iot_device_binding")
@EqualsAndHashCode(callSuper = true)
public class IotDeviceBinding extends BaseEntity {

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 设备ID
     */
    @Column(name = "device_id")
    private Long deviceId;

    /**
     * 绑定时间
     */
    @Column(name = "binding_time")
    private LocalDateTime bindingTime;

    /**
     * 状态 0解绑 1已绑定
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;
}
