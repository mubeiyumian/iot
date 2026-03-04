package com.iot.device.repository;

import com.iot.common.pojo.entity.IotDeviceBinding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/24 22:26
 * @Version: 1.0
 */
public interface IotDeviceBindingRepository extends JpaRepository<IotDeviceBinding, Long> {

    List<IotDeviceBinding> findByUserId(Long userId);

    Boolean existsByUserIdAndDeviceId(Long userId, Long deviceId);

    @Query(value = """
            select b.* from iot_device_binding b 
            join iot_device d on b.device_id = d.id
            where d.device_sn = :sn
            """, nativeQuery = true)
    IotDeviceBinding findBySn(String sn);
}
