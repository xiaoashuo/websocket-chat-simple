package com.lovecyy.web.socket.config;

import com.lovecyy.web.socket.filter.MessageHandshakeInterceptor;
import com.lovecyy.web.socket.service.MyHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author ys
 * @topic
 * @date 2019/7/31 16:00
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private MyHandler myHandler;

    @Autowired
    private MessageHandshakeInterceptor messageHandshakeInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(myHandler, "/ws/{uid}").setAllowedOrigins("*")
        .addInterceptors(messageHandshakeInterceptor);

    }
}
