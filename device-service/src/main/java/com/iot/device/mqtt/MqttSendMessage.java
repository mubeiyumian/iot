package com.iot.device.mqtt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

/**
 * @Author: Kai
 * @Description: MQTT消息发送服务，提供统一的消息发送接口
 * @Date: 2026/02/18 00:16
 * @Version: 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MqttSendMessage {

    private final MqttClient mqttClient;
    private final MqttProperties mqttProperties;

    /**
     * 发送MQTT消息（使用默认配置）
     */
    public void sendMessage(String topic, String payload) {
        sendMessage(topic, payload, mqttProperties.getDefaultQos(), mqttProperties.isDefaultRetained());
    }

    /**
     * 发送MQTT消息（自定义QoS和保留标志）
     */
    public void sendMessage(String topic, String payload, int qos, boolean retained) {
        if (!mqttClient.isConnected()) {
            log.error("MQTT客户端未连接，无法发送消息 - 主题: {}", topic);
            return;
        }

        try {
            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(qos);
            message.setRetained(retained);

            mqttClient.publish(topic, message);
            log.info("MQTT消息发送成功 - 主题: {}, QoS: {}, 保留标志: {}, 内容: {}",
                    topic, qos, retained, payload);
        } catch (MqttException e) {
            log.error("发送MQTT消息失败 - 主题: {}", topic, e);
        }
    }

    /**
     * 取消订阅主题
     */
    public void unsubscribe(String topic) {
        try {
            mqttClient.unsubscribe(topic);
            log.info("取消订阅MQTT主题成功: {}", topic);
        } catch (MqttException e) {
            log.error("取消订阅MQTT主题失败", e);
        }
    }

    /**
     * 订阅主题
     */
    public void subscribe(String topic, int qos) {
        try {
            mqttClient.subscribe(topic, qos);
            log.info("订阅MQTT主题成功: {}, QoS: {}", topic, qos);
        } catch (MqttException e) {
            log.error("订阅MQTT主题失败", e);
        }
    }
}
