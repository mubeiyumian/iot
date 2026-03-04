package com.iot.device.mqtt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: Kai
 * @Description: MQTT配置属性类，映射application.yml中的配置
 * @Date: 2026/02/18 00:14
 * @Version: 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.mqtt")
public class MqttProperties {
    /** MQTT服务器地址 */
    private String brokerUrl;
    /** 客户端ID */
    private String clientId;
    /** 用户名 */
    private String username;
    /** 密码 */
    private String password;
    /** 连接超时时间（秒） */
    private int connectTimeout = 30;
    /** 会话是否持久化 */
    private boolean cleanSession = true;
    /** 默认QoS级别 */
    private int defaultQos = 1;
    /** 默认是否保留消息 */
    private boolean defaultRetained = false;
    /** 心跳间隔（秒） */
    private int keepAliveInterval = 60;
    /** 订阅的主题列表 */
    private String subscribeTopics;

    /**
     * 获取订阅主题列表（转换为List）
     */
    public List<String> getSubscribeTopicsList() {
        if (subscribeTopics == null || subscribeTopics.isEmpty()) {
            return List.of();
        }
        return Arrays.asList(subscribeTopics.split(","));
    }
}
