package com.iot.device.config;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/23 20:41
 * @Version: 1.0
 */

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class MyWebsocketHandler implements WebSocketHandler {
    private static final Map<String, WebSocketSession> SESSIONS = new ConcurrentHashMap<>();
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 从session属性中获取经过验证的用户信息
        Long userId = (Long) session.getAttributes().get("userId");
        if (userId == null) {
            log.info("WebSocket连接建立失败：缺少用户信息");
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("用户信息缺失"));
            return;
        }
        String key = "user_" + userId;

        SESSIONS.put(key, session);
    }
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String msg = message.getPayload().toString();
        log.info(msg);
    }
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.info("连接出错");
        if (session.isOpen()) {
            session.close();
        }
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.info("连接已关闭,status:" + closeStatus);
    }
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
    /**
     * 指定发消息
     *
     * @param message
     */
    public static void sendMessage(String key, JSONObject message) {
        WebSocketSession webSocketSession = SESSIONS.get(key);
        if (webSocketSession == null || !webSocketSession.isOpen()) return;
        try {
            webSocketSession.sendMessage(new TextMessage(message.toJSONString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 群发消息
     *
     * @param message
     */
    public static void fanoutMessage(JSONObject message) {
        SESSIONS.keySet().forEach(us -> sendMessage(us, message));
    }
}
