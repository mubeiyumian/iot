package com.iot.device.repository;

import com.iot.common.pojo.entity.IotProduct;
import com.iot.device.cron.dto.SensorCronDTO;
import com.iot.device.pojo.dto.IotProductDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/24 22:26
 * @Version: 1.0
 */
public interface IotProductRepository extends JpaRepository<IotProduct, Long> {

    @Query(value = """
            select p.id productId, p.product_key productKey, p.category, p.redis_prefix redisPrefix, 
            p.description, p.product_name productName from iot_product p 
            join iot_device d on p.id = d.product_id 
            where d.device_sn = :deviceSn 
            """, nativeQuery = true)
    IotProductDTO findByDeviceSn(String deviceSn);

    @Query(value = """
            select p.id productId, p.product_key productKey, p.category, p.redis_prefix redisPrefix, 
            p.description, p.product_name productName, d.device_sn sn from iot_product p 
            join iot_device d on p.id = d.product_id 
            where d.id in :deviceIds
            """, nativeQuery = true)
    List<IotProductDTO> findByDeviceIdIn(Collection<Long> deviceIds);

    @Query(value = """
            select p.redis_prefix redisPrefix, d.device_sn deviceSn from iot_product p 
            join iot_device d on d.product_id = p.id 
            where p.category = 'sensor' 
            """, nativeQuery = true)
    List<SensorCronDTO> findSensor();
}
