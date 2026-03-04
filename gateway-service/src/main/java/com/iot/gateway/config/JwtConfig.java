package com.iot.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/14 00:27
 * @Version: 1.0
 */

@Configuration
public class JwtConfig {

    @Value("${spring.jwt.user-secret-key}")
    private String userSecretKey;

    @Value("${spring.jwt.user-ttl}")
    private Long ttl;

    @Value("${spring.jwt.user-token-name}")
    private String tokenName;

    public String getUserSecretKey() {
        return userSecretKey;
    }

    public void setUserSecretKey(String userSecretKey) {
        this.userSecretKey = userSecretKey;
    }

    public Long getTtl() {
        return ttl;
    }

    public void setTtl(Long ttl) {
        this.ttl = ttl;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public JwtConfig(String userSecretKey, Long ttl, String tokenName) {
        this.userSecretKey = userSecretKey;
        this.ttl = ttl;
        this.tokenName = tokenName;
    }

    public JwtConfig() {
    }
}
