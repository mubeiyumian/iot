package com.iot.device.servcie;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.iot.common.pojo.entity.IotDeviceBinding;
import com.iot.device.config.MyWebsocketHandler;
import com.iot.device.pojo.dto.SeniorReport;
import com.iot.device.pojo.report.LampReport;
import com.iot.device.repository.IotDeviceBindingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/21 17:17
 * @Version: 1.0
 */
@Service
@Slf4j
public class MqttService {

    @Autowired
    private WriteApi writeApi;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private IotDeviceBindingRepository iotDeviceBindingRepository;

    public void connected(String payload) {
        JSONObject jsonObject = JSON.parseObject(payload);
        String clientId = (String) jsonObject.get("clientid");
        Integer currentConnected = 1;
        Integer connected = (Integer) redisTemplate.opsForHash().get("iot:device:" + clientId, "connected");
        if (!currentConnected.equals(connected)) {
            redisTemplate.opsForHash().put("iot:device:" + clientId, "connected", currentConnected);
            redisTemplate.opsForHash().put("iot:device:" + clientId, "timestamp", System.currentTimeMillis());
            IotDeviceBinding binding = iotDeviceBindingRepository.findBySn(clientId);
            JSONObject json = new JSONObject();
            json.put("code", 200);
            JSONObject message = new JSONObject();
            message.put("deviceOnline", true);
            message.put("sn", clientId);
            message.put("timestamp", System.currentTimeMillis());
            json.put("data", message);
            MyWebsocketHandler.sendMessage("user_" + binding.getUserId(), json);
        }
    }

    public void disconnected(String payload) {
        JSONObject jsonObject = JSON.parseObject(payload);
        String clientId = (String) jsonObject.get("clientid");
        Integer currentConnected = 0;
        Integer connected = (Integer) redisTemplate.opsForHash().get("iot:device:" + clientId, "connected");
        if (!currentConnected.equals(connected)) {
            redisTemplate.opsForHash().put("iot:device:" + clientId, "connected", currentConnected);
            redisTemplate.opsForHash().put("iot:device:" + clientId, "timestamp", System.currentTimeMillis());
            IotDeviceBinding binding = iotDeviceBindingRepository.findBySn(clientId);
            JSONObject json = new JSONObject();
            json.put("code", 200);
            JSONObject message = new JSONObject();
            message.put("deviceOffline", true);
            message.put("sn", clientId);
            message.put("timestamp", System.currentTimeMillis());
            json.put("data", message);
            MyWebsocketHandler.sendMessage("user_" + binding.getUserId(), json);
        }
    }

    public void lampReport(String payload) {
        LampReport report = JSON.parseObject(payload, LampReport.class);
        if (report == null || report.getSn() == null || report.getTimestamp() == null) {
            log.error("消息格式错误，缺少核心字段：{}", payload);
            return;
        }
        Point point = Point.measurement("lamp_report")
                .addTag("sn", report.getSn())
                .addField("onSwitch", report.getOnSwitch())
                .addField("warmCurrent", report.getWarmCurrent())
                .addField("coldCurrent", report.getColdCurrent())
                .addField("brightness", report.getBrightness())
                .time(Instant.ofEpochMilli(report.getTimestamp()), WritePrecision.MS);
        writeApi.writePoint(point);
        log.info("时序数据入库成功 | sn：{} | 时间戳：{}", report.getSn(), report.getTimestamp());
    }

    public void sensorReport(String payload) {
        SeniorReport seniorReport = JSON.parseObject(payload, SeniorReport.class);
        if (seniorReport == null || seniorReport.getSn() == null || seniorReport.getTimestamp() == null) {
            log.error("消息格式错误，缺少核心字段：{}", payload);
            return;
        }
        Point point = Point.measurement("sensor_report")
                .addTag("sn", seniorReport.getSn())
                .addField("temperature", seniorReport.getTemperature())
                .addField("humidity", seniorReport.getHumidity())
                .time(Instant.ofEpochMilli(seniorReport.getTimestamp()), WritePrecision.MS);
        writeApi.writePoint(point);
        log.info("时序数据入库成功 | sn：{} | 时间戳：{}", seniorReport.getSn(), seniorReport.getTimestamp());
    }

}
