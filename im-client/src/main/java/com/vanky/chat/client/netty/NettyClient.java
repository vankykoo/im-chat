package com.vanky.chat.client.netty;

import com.vanky.chat.client.channel.UserChannelMap;
import com.vanky.chat.client.handler.ClientMultiProtocolDecoder;
import com.vanky.chat.client.handler.ClientMultiProtocolEncoder;
import com.vanky.chat.client.handler.ClientMultiProtocolHandler;
import com.vanky.chat.client.processor.LoginMsgProcessor;
import feign.Client;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author vanky
 * @create 2024/3/26 16:04
 */
@Component
public class NettyClient {

    @Resource
    @Lazy
    private ClientMultiProtocolHandler clientMultiProtocolHandler;

    @Value("${im.server.host}")
    private String host;

    @Value("${im.server.port}")
    private Integer port;

    @Resource
    private LoginMsgProcessor loginMsgProcessor;

    private NioEventLoopGroup group;
    private Bootstrap bootstrap;

    public NettyClient() {
        bootstrap = initBootstrap();
    }

    private Bootstrap initBootstrap(){
        group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        /**
                         * IdleStateHandler 是 Netty 提供的一个处理器，用于检测通道的空闲状态，
                         * 并在满足特定的空闲条件时触发一个 IdleStateEvent 事件。它可以帮助我们
                         * 实现对连接的心跳检测，以便在连接空闲超时时采取相应的处理措施，例如发送
                         * 心跳消息或关闭连接。
                         */
                        pipeline.addLast(new IdleStateHandler(20, 10, 0, TimeUnit.MINUTES));
                        //解码器
                        pipeline.addLast("clientMultiProtocolDecoder", new ClientMultiProtocolDecoder());
                        //处理器
                        pipeline.addLast("clientMultiProtocolHandler", clientMultiProtocolHandler);
                        //编码器
                        pipeline.addLast("clientMultiProtocolEncoder", new ClientMultiProtocolEncoder());
                    }
                });

        return bootstrap;
    }

    /**
     * 客户端与服务端建立连接，并保存用户与连接的映射信息
     * @param host
     * @param port
     * @param userId
     * @return
     */
    public NioSocketChannel connect(String host, Integer port, Long userId){
        if (host == null || port == null){
            host = this.host;
            port = this.port;
        }

        try{
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();

            NioSocketChannel channel = (NioSocketChannel) channelFuture.channel();

            // 保存用户与连接的映射信息
            UserChannelMap.userChannel.put(userId, channel);
            UserChannelMap.channelUserMap.put(channel.id().asLongText(), userId);

            loginMsgProcessor.sendLoginMsg(userId, channel);

            return channel;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /*public NioSocketChannel connect(Long userId){
        try{
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();

            NioSocketChannel channel = (NioSocketChannel) channelFuture.channel();

            // 保存用户与连接的映射信息
            UserChannelMap.userChannel.put(userId, channel);
            UserChannelMap.channelUserMap.put(channel.id().asLongText(), userId);

            //发送登录消息
            loginMsgProcessor.sendLoginMsg(userId, channel);

            return channel;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }*/

    public void destroy(){
        group.shutdownGracefully();
    }

}
