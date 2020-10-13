package com.lovecyy.web.socket.netty;

import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import org.springframework.util.StringUtils;

import javax.net.ssl.HandshakeCompletedEvent;

/**
 * 心跳机制
 */
public class HearBeatHandler extends ChannelInboundHandlerAdapter {
    public static final AttributeKey<AuthHandler.ValidateResult> TOKEN =
            AttributeKey.valueOf("TOKEN");
    //触发用户事件
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE){
            //握手完成后鉴权
            Channel channel = ctx.channel();
            AuthHandler.ValidateResult validateResult = channel.attr(TOKEN).get();
            if (StringUtils.isEmpty(validateResult.getToken())){
                ctx.writeAndFlush(new TextWebSocketFrame("token错误")).addListener(ChannelFutureListener.CLOSE);
            }

        }
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {//读空闲
                //检测到读空闲不做任何的操作
                System.out.println("读空闲事件触发...");
            } else if (idleStateEvent.state() == IdleState.WRITER_IDLE) {//写空闲
                //检测到写空闲不做任何的操作
                System.out.println("写空闲事件触发...");
            } else if (idleStateEvent.state() == IdleState.ALL_IDLE) {//读写空闲
                System.out.println("--------------");
                System.out.println("读写空闲事件触发");
                System.out.println("关闭通道资源");
                ctx.channel().close();
            }
        }
    }
}
