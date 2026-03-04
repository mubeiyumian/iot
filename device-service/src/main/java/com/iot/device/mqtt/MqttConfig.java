package com.iot.device.mqtt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.UUID;

/**
 * @Author: Kai
 * @Description: MQTT客户端配置类，创建并管理MQTT客户端连接
 * @Date: 2026/02/18 00:15
 * @Version: 1.0
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class MqttConfig {

    private final MqttProperties mqttProperties;
    private final List<MqttMessageHandler> mqttMessageHandlers;

    /**
     * 创建MQTT客户端实例（单例）
     */
    @Bean
    public MqttClient mqttClient() throws MqttException {
        // 创建内存持久化（也可以使用文件持久化）
        MemoryPersistence persistence = new MemoryPersistence();

        String clientId = "spring-" + UUID.randomUUID().toString();

        // 创建MQTT客户端
        MqttClient client = new MqttClient(
                mqttProperties.getBrokerUrl(),
                clientId,
                persistence
        );

        // 配置连接选项
        MqttConnectOptions options = getMqttConnectOptions();

        // 设置回调函数
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                log.error("MQTT连接丢失", cause);
                reconnect(client, options);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                // 分发消息到对应的处理器
                dispatchMessage(topic, message);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                // 消息发送完成回调
                if (token.isComplete()) {
                    log.debug("MQTT消息发送完成 - ID: {}", token.getMessageId());
                }
            }
        });

        // 连接MQTT服务器
        if (!client.isConnected()) {
            client.connect(options);
            log.info("MQTT客户端连接成功 - 服务器: {}, 客户端ID: {}",
                    mqttProperties.getBrokerUrl(), clientId);

            // 订阅配置的主题
            subscribeTopics(client);
        }

        return client;
    }

    /**
     * 创建MQTT连接选项
     */
    private MqttConnectOptions getMqttConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        // 设置用户名密码
        if (mqttProperties.getUsername() != null && !mqttProperties.getUsername().isEmpty()) {
            options.setUserName(mqttProperties.getUsername());
        }
        if (mqttProperties.getPassword() != null && !mqttProperties.getPassword().isEmpty()) {
            options.setPassword(mqttProperties.getPassword().toCharArray());
        }
        // 连接超时
        options.setConnectionTimeout(mqttProperties.getConnectTimeout());
        // 会话是否持久化
        options.setCleanSession(mqttProperties.isCleanSession());
        // 心跳间隔
        options.setKeepAliveInterval(mqttProperties.getKeepAliveInterval());
        // 自动重连
        options.setAutomaticReconnect(true);

        return options;
    }

    /**
     * 订阅配置的主题
     */
    private void subscribeTopics(MqttClient client) {
        List<String> topics = mqttProperties.getSubscribeTopicsList();
        if (topics.isEmpty()) {
            log.warn("未配置需要订阅的MQTT主题");
            return;
        }

        // 构建主题和QoS数组
        String[] topicArray = topics.toArray(new String[0]);
        int[] qosArray = new int[topicArray.length];
        for (int i = 0; i < qosArray.length; i++) {
            qosArray[i] = mqttProperties.getDefaultQos();
        }

        try {
            client.subscribe(topicArray, qosArray);
            log.info("成功订阅MQTT主题: {}", topics);
        } catch (MqttException e) {
            log.error("订阅MQTT主题失败", e);
        }
    }

    /**
     * 消息分发到对应的处理器
     */
    private void dispatchMessage(String topic, MqttMessage message) {
        for (MqttMessageHandler handler : mqttMessageHandlers) {
            if (handler.supportsTopic(topic)) {
                try {
                    handler.handleMessage(topic, message);
                    // 找到匹配的处理器后可以选择是否继续分发
                    break;
                } catch (Exception e) {
                    log.error("处理MQTT消息失败 - 主题: {}", topic, e);
                }
            }
        }
    }

    /**
     * MQTT重连逻辑
     */
    private void reconnect(MqttClient client, MqttConnectOptions options) {
        new Thread(() -> {
            int retryCount = 0;
            while (!client.isConnected() && retryCount < 10) {
                try {
                    retryCount++;
                    log.info("尝试重新连接MQTT服务器（第{}次）", retryCount);
                    client.connect(options);
                    subscribeTopics(client);
                    log.info("MQTT重连成功");
                } catch (MqttException e) {
                    log.error("MQTT重连失败", e);
                    try {
                        // 等待5秒后重试
                        Thread.sleep(5000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }).start();
    }
}
