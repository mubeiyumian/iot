package com.iot.device.config;

import com.influxdb.LogLevel;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/16 22:22
 * @Version: 1.0
 */
@Configuration
public class InfluxdbConfig {

    @Value("${spring.influxdb.username}")
    private String username;

    @Value("${spring.influxdb.password}")
    private String password;

    @Value("${spring.influxdb.url}")
    private String url;

    @Value("${spring.influxdb.database}")
    private String database;

    @Value("${spring.influxdb.org}")
    private String influxDbOrg;

    @Value("${spring.influxdb.token}")
    private String token;

    @Bean
    public InfluxDBClient influxDB() {
        InfluxDBClient client = InfluxDBClientFactory.create(
                url,
                token.toCharArray(),
                influxDbOrg,
                database
        );
        // 配置写入策略：批量写入+自动刷新
        client.setLogLevel(LogLevel.BASIC);
        return client;
    }

    /**
     * 获取写入API
     */
    @Bean
    public WriteApi influxDbWriteApi(InfluxDBClient influxDBClient) {
        return influxDBClient.makeWriteApi();
    }
}
