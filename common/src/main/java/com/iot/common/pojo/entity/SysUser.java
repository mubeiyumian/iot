package com.iot.common.pojo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Kai
 * @Description: 系统用户
 * @Date: 2026/02/23 23:15
 * @Version: 1.0
 */
@Getter
@Setter
@Entity
@Table(name = "sys_user")
@EqualsAndHashCode(callSuper = true)
public class SysUser extends BaseEntity {

    /**
     * 用户名
     */
    @Column(name = "username")
    private String username;

    /**
     * 手机号
     */
    @Column(name = "phone")
    private String phone;

    /**
     * 密码
     */
    @Column(name = "password")
    private String password;

    /**
     * 状态 0禁用 1正常
     */
    @Column(name = "status")
    private Integer status;
}
