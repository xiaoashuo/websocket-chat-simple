package com.lovecyy.web.socket.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lovecyy.web.socket.netty.Message;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.CharsetUtil;

public class NettyClient {
    public static void main(String[] args) throws InterruptedException, JsonProcessingException {
        Bootstrap bootstrap = new Bootstrap();

        NioEventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {

            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                //websocket基于 http协议,需要有 http 的编解码器
                ch.pipeline().addLast(new HttpServerCodec())
                        //对于大数据流的支持
                        .addLast(new ChunkedWriteHandler());
                //2、socket编程中需要对字符串进行编码解码

            }
        });

        ChannelFuture future = bootstrap.connect("192.168.1.206", 9001).sync();
        Message message = new Message();
        message.setType(1);
        message.setExt("ssss");
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(message);
        future.channel().writeAndFlush(s);
        future.await();
    }
}
