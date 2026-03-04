package com.iot.user.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/14 00:27
 * @Version: 1.0
 */

@Configuration
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtConfig {

    @Value("${spring.jwt.user-secret-key}")
    private String userSecretKey;

    @Value("${spring.jwt.user-ttl}")
    private Long ttl;

    @Value("${spring.jwt.user-token-name}")
    private String tokenName;
}
