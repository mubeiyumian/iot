package com.iot.device.repository;

import com.iot.common.pojo.entity.IotDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/24 22:25
 * @Version: 1.0
 */
public interface IotDeviceRepository extends JpaRepository<IotDevice, Long> {

    Optional<IotDevice> findByDeviceSn(String deviceSn);
}
