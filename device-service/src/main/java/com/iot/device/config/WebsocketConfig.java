package com.iot.device.config;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/23 20:40
 * @Version: 1.0
 */

import com.iot.device.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Configuration
@EnableWebSocket
@Slf4j
public class WebsocketConfig implements WebSocketConfigurer {

    @Autowired
    private JwtConfig jwtConfig;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new MyWebsocketHandler(), "/ws/miniprogram")//设置连接路径和处理
                .setAllowedOrigins("*")
                .addInterceptors(new MyWebSocketInterceptor());//设置拦截器
    }
    /**
     * 自定义拦截器拦截WebSocket请求
     */
    class MyWebSocketInterceptor implements HandshakeInterceptor {
        //前置拦截一般用来注册用户信息，绑定 WebSocketSession
        @Override
        public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                       WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
            log.info("前置拦截~~");
            if (!(request instanceof ServletServerHttpRequest)) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }

            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            String token = servletRequest.getServletRequest().getParameter("token");

            // 如果没有token参数，尝试从header中获取
            if (token == null || token.isEmpty()) {
                token = servletRequest.getServletRequest().getHeader("token");
            }

            // 验证token
            if (token == null || token.isEmpty()) {
                log.info("WebSocket连接失败：缺少认证token");
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }

            try {
                // 解析JWT token
                Claims claims = JwtUtils.parseJWT(jwtConfig.getUserSecretKey(), token);
                Long userId = claims.get("userId", Long.class);

                if (userId == null) {
                    log.info("WebSocket连接失败：token中缺少userId");
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return false;
                }

                // 将用户信息存入attributes，供后续使用
                attributes.put("userId", userId);

                log.info("WebSocket认证成功，userId: " + userId);
                return true;

            } catch (Exception e) {
                log.info("WebSocket连接失败：token验证失败 - " + e.getMessage());
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }
        }
        @Override
        public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Exception exception) {
            log.info("后置拦截~~");
        }
    }
}
