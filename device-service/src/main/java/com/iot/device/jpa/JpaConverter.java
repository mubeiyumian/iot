package com.iot.device.jpa;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * @Author: Kai
 * @Description: Jpa自定义注解实现器
 * @Date: 2026/2/24 21:58
 * @Version: 1.0
 */

@Slf4j
@Configuration
public class JpaConverter {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 初始化注入@JpaDto对应的Converter
     */
    @PostConstruct
    public void init() {
        Map<String, Object> map = applicationContext.getBeansWithAnnotation(JpaDTO.class);
        for (Object o : map.values()) {
            Class c = o.getClass();
            log.info("Jpa添加Converter,class={}", c.getName());
            GenericConversionService genericConversionService = ((GenericConversionService) DefaultConversionService.getSharedInstance());
            genericConversionService.addConverter(Map.class, c, m -> {
                try {
                    Object obj = c.newInstance();
                    return copyMapToObj(m, obj);
                } catch (Exception e) {
                    throw new FatalBeanException("Jpa结果转换出错,class=" + c.getName(), e);
                }
            });
        }
    }

    /**
     * 将map中的值copy到bean中对应的字段上
     * @param map
     * @param target
     * @return
     */
    private Object copyMapToObj(Map<String, Object> map, Object target) {
        if(map == null || target == null || map.isEmpty()){
            return target;
        }
        Class<?> actualEditable = target.getClass();
        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
        for (PropertyDescriptor targetPd : targetPds) {
            if(targetPd.getWriteMethod() == null) {
                continue;
            }
            try {
                String key = targetPd.getName();
                Object value = map.get(key);
                if (value == null) {
                    continue;
                }

                // 获取目标属性类型
                Class<?> targetType = targetPd.getPropertyType();

                // 类型转换
                Object convertedValue = convertValue(value, targetType);

                // 设置值
                Method writeMethod = targetPd.getWriteMethod();
                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                    writeMethod.setAccessible(true);
                }
                writeMethod.invoke(target, convertedValue);
            } catch (Exception ex) {
                log.warn("属性转换失败: property={}, value={}, targetClass={}",
                        targetPd.getName(), map.get(targetPd.getName()), target.getClass().getName(), ex);
            }
        }
        return target;
    }


    /**
     * 类型转换方法
     * @param value 源值
     * @param targetType 目标类型
     * @return 转换后的值
     */
    private Object convertValue(Object value, Class<?> targetType) {
        // 如果类型已经匹配，直接返回
        if (targetType.isInstance(value)) {
            return value;
        }

        // 处理常见的类型转换
        if (targetType == String.class) {
            return value.toString();
        }

        if (targetType == Integer.class || targetType == int.class) {
            if (value instanceof Number) {
                return ((Number) value).intValue();
            } else {
                return Integer.parseInt(value.toString());
            }
        }

        if (targetType == Long.class || targetType == long.class) {
            if (value instanceof Number) {
                return ((Number) value).longValue();
            } else {
                return Long.parseLong(value.toString());
            }
        }

        if (targetType == Double.class || targetType == double.class) {
            if (value instanceof Number) {
                return ((Number) value).doubleValue();
            } else {
                return Double.parseDouble(value.toString());
            }
        }

        if (targetType == Boolean.class || targetType == boolean.class) {
            if (value instanceof Boolean) {
                return value;
            } else {
                return Boolean.parseBoolean(value.toString());
            }
        }

        // 处理日期时间类型转换
        if (targetType == java.time.LocalDateTime.class) {
            if (value instanceof java.util.Date) {
                return ((java.util.Date) value).toInstant()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDateTime();
            }
        }

        if (targetType == java.util.Date.class && value instanceof java.time.LocalDateTime) {
            return java.util.Date.from(((java.time.LocalDateTime) value)
                    .atZone(java.time.ZoneId.systemDefault()).toInstant());
        }

        return value;
    }
}
