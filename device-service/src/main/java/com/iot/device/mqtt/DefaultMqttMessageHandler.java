package com.iot.device.mqtt;

import com.influxdb.client.InfluxDBClient;
import com.iot.device.servcie.MqttService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Author: Kai
 * @Description: 默认MQTT消息处理器，处理未匹配到特定处理器的消息
 * @Date: 2026/02/18 00:15
 * @Version: 1.0
 */
@Slf4j
@Component
@Order(Integer.MAX_VALUE) // 优先级最低
public class DefaultMqttMessageHandler implements MqttMessageHandler {

    @Autowired
    private InfluxDBClient client;

    @Autowired
    private MqttService mqttService;

    @Override
    public boolean supportsTopic(String topic) {
        return true;
    }

    @Override
    public void handleMessage(String topic, MqttMessage message) {
        String payload = new String(message.getPayload());
        log.info("收到MQTT消息 - 主题: {}, QoS: {}, 保留标志: {}, 内容: {}",
                topic, message.getQos(), message.isRetained(), payload);
        if (topic.startsWith("$SYS/broker") && topic.endsWith("/connected")) {
            mqttService.connected(payload);
        } else if (topic.startsWith("$SYS/broker") && topic.endsWith("/disconnected")) {
            mqttService.disconnected(payload);
        } else if (topic.contains("lamp/report")) {
            mqttService.lampReport(payload);
        } else if (topic.contains("sensor/report")) {
            mqttService.sensorReport(payload);
        }
    }
}
