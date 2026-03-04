package com.iot.device.cron;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import com.iot.device.cron.dto.SensorCronDTO;
import com.iot.device.repository.IotProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/21 18:49
 * @Version: 1.0
 */

@Component
@Slf4j
public class SensorCron {

    @Autowired
    private InfluxDBClient client;

    @Autowired
    private IotProductRepository iotProductRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Scheduled(cron = "0 0/2 * * * ?")
    public void sensorCron() {
        log.info("温湿度定时任务开始执行");
        List<SensorCronDTO> list = iotProductRepository.findSensor();
        if (list.isEmpty()) {
            return;
        }
        for (SensorCronDTO sensorCronDTO : list) {
            try {
                // 1. 构建 Flux 查询：最近1分钟数据，按时间降序排序，取第一条
                String query = buildFluxQuery(sensorCronDTO.getDeviceSn());
                QueryApi queryApi = client.getQueryApi();
                List<FluxTable> tables = queryApi.query(query);

                // 2. 解析数据：提取最新的温湿度值
                Map<String, Double> tempHumidityMap = parseLatestData(tables);

                // 3. 存入 Redis Hash
                if (!tempHumidityMap.isEmpty()) {
                    String redisHashKey = sensorCronDTO.getRedisPrefix() + sensorCronDTO.getDeviceSn();
                    HashOperations<String, String, Double> hashOps = redisTemplate.opsForHash();
                    hashOps.putAll(redisHashKey, tempHumidityMap);
                    log.info("温湿度数据存入Redis成功，Key：{}，数据：{}", redisHashKey, tempHumidityMap);
                } else {
                    log.warn("未查询到{}最近1分钟的温湿度数据", sensorCronDTO.getDeviceSn());
                }
            } catch (Exception e) {
                log.error("温湿度定时任务执行失败", e);
            }
        }
    }

    /**
     * 构建 Flux 查询语句：最近1分钟数据，按时间降序，取第一条
     */
    private String buildFluxQuery(String deviceSn) {
        return String.format("""
                from(bucket: "device_report")
                  |> range(start: -1m)
                  |> filter(fn: (r) => r["_measurement"] == "sensor_report")
                  |> filter(fn: (r) => r["_field"] == "humidity" or r["_field"] == "temperature")
                  |> filter(fn: (r) => r["sn"] == "%s")
                  |> sort(columns: ["_time"], desc: true)
                  |> limit(n: 1)
                  |> yield(name: "results")
                """, deviceSn);
    }

    /**
     * 解析 InfluxDB 返回结果，提取温湿度值
     */
    private Map<String, Double> parseLatestData(List<FluxTable> tables) {
        Map<String, Double> resultMap = new HashMap<>(2);
        // 遍历表和记录，提取字段和值
        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
                // 获取字段名（humidity/temperature）和对应数值
                String field = record.getField();
                Object value = record.getValue();
                if (field != null && value != null) {
                    // 统一转为字符串存储（Redis Hash 值为字符串）
                    resultMap.put(field, (Double) value);
                }
            }
        }
        return resultMap;
    }
}
