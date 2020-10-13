package com.lovecyy.web.socket.netty;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Objects;

public class AuthHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Logger log= LoggerFactory.getLogger(AuthHandler.class);
    public static final AttributeKey<ValidateResult> TOKEN =
            AttributeKey.valueOf("TOKEN");

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        //将http参数绑定到管道  比如token
        Channel channel = ctx.channel();
        HttpHeaders headers = request.headers();
        String token = headers.get("token");
        ValidateResult validateResult = new ValidateResult(token);
        ctx.channel().attr(TOKEN).set(validateResult);
        // 传递到下一个handler：升级握手
        ctx.fireChannelRead(request.retain());
    }



    @Getter
    @AllArgsConstructor
    public static class ValidateResult{

        private String token;
    }
}
