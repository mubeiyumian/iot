package com.iot.gateway.filter;

import com.iot.gateway.config.JwtConfig;
import com.iot.gateway.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/24 23:56
 * @Version: 1.0
 */

//@Order(-1) //方式二
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtConfig jwtConfig;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取请求体
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        if (path.contains("/user/login") || path.contains("/user/client")) {
            return chain.filter(exchange);
        }
        //获取请求头
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String token = headers.getFirst("token");
        //判断请求参数
        if (token != null && !token.isEmpty()) {
            Integer userId = null;
            try {
                // 2. 先验证JWT签名和有效期
                Claims claims = JwtUtils.parseJWT(jwtConfig.getUserSecretKey(), token);
                userId = (Integer) claims.get("userId");
                if (userId == null) {
                    //拦截
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
            } catch (Exception e) {
                //拦截
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            //放行，执行下一个业务
            return chain.filter(exchange);
        }
        //拦截
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() { //方式一：设置过滤器优先级，越小权重越高
        return -1;
    }
}
