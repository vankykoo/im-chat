package com.vanky.chat.server.netty;

import com.vanky.chat.server.handler.ServerMultiProtocolDecoder;
import com.vanky.chat.server.handler.ServerMultiProtocolEncoder;
import com.vanky.chat.server.handler.ServerMultiProtocolHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author vanky
 * @create 2024/3/26 15:45
 */
@Component
@Slf4j
public class NettyServer {

    @Resource
    private ServerMultiProtocolHandler serverMultiProtocolHandler;

    @Value("${netty-server.port}")
    private int port;

    public void run(){
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            /**
                             * IdleStateHandler 是 Netty 提供的一个处理器，用于检测通道的空闲状态，
                             * 并在满足特定的空闲条件时触发一个 IdleStateEvent 事件。它可以帮助我们
                             * 实现对连接的心跳检测，以便在连接空闲超时时采取相应的处理措施，例如发送
                             * 心跳消息或关闭连接。
                             */
                            pipeline.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.MINUTES));
                            //解码器
                            pipeline.addLast("serverMultiProtocolDecoder", new ServerMultiProtocolDecoder());
                            //处理器
                            pipeline.addLast("serverMultiProtocolHandler", serverMultiProtocolHandler);
                            //编码器
                            pipeline.addLast("serverMultiProtocolEncoder", new ServerMultiProtocolEncoder());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            log.info("服务器启动成功，在【{}】进行监听。", channelFuture.channel().localAddress());
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
