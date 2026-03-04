package com.iot.device.mqtt;

import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * @Author: Kai
 * @Description: MQTT消息处理器接口，用于扩展不同主题的消息处理逻辑
 * @Date: 2026/02/18 00:14
 * @Version: 1.0
 */
public interface MqttMessageHandler {
    /**
     * 判断当前处理器是否支持处理指定主题的消息
     */
    boolean supportsTopic(String topic);

    /**
     * 处理MQTT消息
     */
    void handleMessage(String topic, MqttMessage message);
}
