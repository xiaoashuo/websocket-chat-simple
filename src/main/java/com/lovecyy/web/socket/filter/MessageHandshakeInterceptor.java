package com.lovecyy.web.socket.filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * @author ys
 * @topic
 * @date 2019/8/1 13:51
 */
@Component
public class MessageHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        String path = serverHttpRequest.getURI().getPath();
        System.out.println(path+serverHttpRequest.getURI());
        String[] ss = StringUtils.split(path, '/');
        System.out.println(ss[0]+"---"+ss[1]);
        if(ss.length != 2){
            return false;
        }

        if(!StringUtils.isNumeric(ss[1])){
            return false;
        }
        map.put("uid", Integer.valueOf(ss[1]));
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {

    }
}
