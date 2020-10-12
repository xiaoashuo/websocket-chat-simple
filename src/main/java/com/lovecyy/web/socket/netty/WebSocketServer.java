package com.lovecyy.web.socket.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * websocket 初始化
 */
@Component
public class WebSocketServer {

    private EventLoopGroup bossGroup;//主线程

    private EventLoopGroup workerGroup;//工作线程

    private ServerBootstrap server;//服务器

    private ChannelFuture future; //回调

    @PostConstruct
    public void start() {
        future = server.bind(9001);
        System.out.println("netty server - 启动成功");
    }

    @PreDestroy
    public void destroy(){
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        future.channel().close();
        System.out.println("netty server future通道关闭");
    }

    public WebSocketServer() {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        server = new ServerBootstrap();
        server.group(bossGroup,workerGroup)
//                .option(ChannelOption.SO_BACKLOG, 128) // tcp最大缓存链接个数
//                .childOption(ChannelOption.SO_KEEPALIVE, true) //保持连接
//                .handler(new LoggingHandler(LogLevel.INFO)) // 打印日志级别
                .channel(NioServerSocketChannel.class)
                .childHandler(new WebSocketInitializer());

    }

}
