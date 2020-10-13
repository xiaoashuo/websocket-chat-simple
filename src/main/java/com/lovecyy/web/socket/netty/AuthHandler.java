package com.lovecyy.web.socket.netty;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
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
    private void response(ChannelHandlerContext ctx, ValidateResult c) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        // 1.设置响应
        FullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer(objectMapper.writeValueAsString(c), CharsetUtil.UTF_8));

        resp.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");

        // 2.发送
        // 注意必须在使用完之后，close channel
        ctx.writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE);
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        Channel channel = ctx.channel();
        HttpHeaders headers = request.headers();
        String token = headers.get("token");
        if (StringUtils.isEmpty(token)){

            ValidateResult validateResult = new ValidateResult(100, "错误");
            ctx.channel().attr(TOKEN).set(validateResult);
            // 传递到下一个handler：升级握手
            ctx.fireChannelRead(request.retain());
        }

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
         if (evt instanceof ValidateResult){
             log.info("验证结果"+evt);
         }
    }

    @Getter
    @AllArgsConstructor
    public static class ValidateResult{
        private Integer code;
        private String msg;
    }
}
