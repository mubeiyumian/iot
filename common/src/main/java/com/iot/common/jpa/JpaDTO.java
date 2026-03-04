package com.iot.common.jpa;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Author: Kai
 * @Description: Jpa多重查询自定义接口
 * @Date: 2026/2/24 21:57
 * @Version: 1.0
 */

@Component
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JpaDTO {
}
