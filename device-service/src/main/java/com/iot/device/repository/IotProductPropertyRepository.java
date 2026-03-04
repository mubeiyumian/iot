package com.iot.device.repository;

import com.iot.common.pojo.entity.IotProductProperty;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/24 22:26
 * @Version: 1.0
 */
public interface IotProductPropertyRepository extends JpaRepository<IotProductProperty, Long> {
}
