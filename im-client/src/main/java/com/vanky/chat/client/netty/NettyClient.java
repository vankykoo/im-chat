package com.vanky.chat.client.netty;

import com.vanky.chat.client.handler.ClientMsgHandler;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.ReadTimeoutHandler;
import jakarta.annotation.Resource;
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
    private ClientMsgHandler clientMsgHandler;

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

                        pipeline.addLast(new ReadTimeoutHandler(1800, TimeUnit.SECONDS));

                        //in解码
                        ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                        ch.pipeline().addLast(new ProtobufDecoder(BaseMsgProto.BaseMsg.getDefaultInstance()));

                        pipeline.addLast("clientMsgHandler", clientMsgHandler);

                        //out编码
                        ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                        ch.pipeline().addLast(new ProtobufEncoder());
                    }
                });

        return bootstrap;
    }

    public NioSocketChannel connect(String host, int port){
        try{
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();

            //GenericFutureListener<ChannelFuture> listener = new GenericFutureListener<ChannelFuture>() {
            //    @Override
            //    public void operationComplete(ChannelFuture channelFuture) throws Exception {
            //        channelFuture.channel().writeAndFlush("做完了！");
            //    }
            //};
            //channelFuture.addListener(listener);

            return (NioSocketChannel) channelFuture.channel();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void destroy(){
        group.shutdownGracefully();
    }

}
