package com.iot.device.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/25 01:04
 * @Version: 1.0
 */

@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void test1() {
        redisTemplate.opsForHash().put("iot:light:" + "000000", "brightnessMin", 20);
        redisTemplate.opsForHash().put("iot:light:" + "000000", "brightnessMax", 100);
        redisTemplate.opsForHash().put("iot:light:" + "000000", "brightness", 50);
        redisTemplate.opsForHash().put("iot:light:" + "000000", "warmCurrent", 2700);
        redisTemplate.opsForHash().put("iot:light:" + "000000", "coldCurrent", 5000);
    }

    @Test
    void test2() {
        Object connected = redisTemplate.opsForHash().get("iot:device" + "LIGHTSN001", "connected");
        System.out.println(connected);
    }
}
