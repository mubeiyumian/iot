package com.iot.common.pojo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Kai
 * @Description: 产品物模型属性
 * @Date: 2026/02/23 23:17
 * @Version: 1.0
 */
@Getter
@Setter
@Entity
@Table(name = "iot_product_property")
@EqualsAndHashCode(callSuper = true)
public class IotProductProperty extends BaseEntity {

    /**
     * 产品ID
     */
    @Column(name = "product_id")
    private Long productId;

    /**
     * 属性编码
     */
    @Column(name = "property_code")
    private String propertyCode;

    /**
     * 属性名称
     */
    @Column(name = "property_name")
    private String propertyName;

    /**
     * 数据类型
     */
    @Column(name = "data_type")
    private String dataType;

    /**
     * 单位
     */
    @Column(name = "unit")
    private String unit;

    /**
     * 是否必填
     */
    @Column(name = "is_required")
    private Integer isRequired;
}
