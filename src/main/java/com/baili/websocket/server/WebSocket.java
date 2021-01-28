package com.baili.websocket.server;


import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baili.common.utils.Utils;
import com.baili.config.redis.RedisCache;
import com.baili.websocket.entity.WebSocketMessage;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 大屏演示webSocket
 *
 * @author lxq
 */
@Component
@ServerEndpoint("/webSocket/{test}")
@Slf4j
public class WebSocket {

    private static ConcurrentHashMap<String, List<WebSocket>> websocketMap = new ConcurrentHashMap<>();
    private static RedisCache redisCache;
    private String test;
    private Session session;

    @Autowired
    public void setRedisCache(RedisCache redisCache) {
        WebSocket.redisCache = redisCache;
    }

    @OnOpen
    public void onOpen(@PathParam("test") String test, Session session) {
        this.test = test;
        this.session = session;
//        if (!validate(token)) {
//            replyMessage(JSON.toJSONString(Result.error(999, "参数错误，连接中断")));
//            try {
//                session.close();
//            } catch (IOException e) {
//            }
//            return;
//        }
        List<WebSocket> webSocketScreens = websocketMap.get(test);
        if (CollectionUtil.isEmpty(webSocketScreens)) {
            webSocketScreens = new ArrayList<>(10);
            websocketMap.put(test, webSocketScreens);
        }
        webSocketScreens.add(this);
        log.info("[webSocket大屏演示实时刷新-消息]有新的连接,总数:{}", clientCount());
    }

    @OnClose
    public void onClose() {
        List<WebSocket> webSocketScreens = websocketMap.get(this.test);
        try {
            webSocketScreens.remove(this);
        } catch (Exception e) {
        }
        log.info("[webSocket大屏演示实时刷新-消息]连接断开,总数:{}", clientCount());
    }

    @OnMessage
    public void onMsg(String msg) {
        if (Utils.isNullOrEmpty(msg)) {
            return;
        }
        log.debug("【webSocket大屏演示-收到消息】{}", msg);
        WebSocketMessage webSocketMessage = JSON.parseObject(msg, WebSocketMessage.class);
        Integer type = webSocketMessage.getType();
        if (type != null) {
            switch (type) {
                case 0:
                    // 接收心跳消息
                    replyMessage(msg);
                    break;
                case 1:
                    replyMessage(msg);
                    break;
                default:
                    break;
            }
        }
        return;
    }

    @SneakyThrows
    private void sendMessage(String message) {
        this.session.getBasicRemote().sendText(message);
    }

    public void sendInfo(String message) {
        log.debug("【websocket大屏演示实时刷新-消息】广播消息, message={}", message);
        websocketMap.forEach((k, v) -> {
            v.forEach(o -> {
                try {
                    o.session.getAsyncRemote().sendText(message);
                } catch (Exception e) {
                    log.error("会话已结束");
                }
            });
        });
    }

    /**
     * 解析token信息
     *
     * @param tokenKey
     * @return
     */
    public boolean validate(String tokenKey) {
        String token = redisCache.getCacheObject(tokenKey);
        if (null != token) {
            Claims claims = null;
            try {
                // 解析JWT
                claims = Jwts.parser()
                        .setSigningKey("baili")
                        .parseClaimsJws(token)
                        .getBody();
                Long userId = Long.valueOf(claims.getId());
                if (null == userId) {
                    return false;
                }
            } catch (ExpiredJwtException e) {
                return false;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     * 回复消息
     *
     * @param message
     */
    public void replyMessage(String message) {
        log.debug("【websocket大屏演示实时刷新-消息】回复消息, message={}", message);
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("【websocket大屏演示实时刷新-消息】回复消息失败, message={}", message);
            e.printStackTrace();
        }
    }

    /**
     * 连接数
     *
     * @return
     */
    private Integer clientCount() {
        int count = 0;
        for (List<WebSocket> value : websocketMap.values()) {
            if (CollectionUtil.isNotEmpty(value)) {
                count += value.size();
            }
        }
        return count;
    }
}