package com.vanky.chat.client.handler;

import com.vanky.chat.client.channel.UserChannelMap;
import com.vanky.chat.client.utils.SendMsgUtil;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateEvent;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
@Slf4j
public class ClientMultiProtocolHandler extends SimpleChannelInboundHandler<Object> {

    @Resource
    private ClientMsgHandler clientMsgHandler;

    @Resource
    private HeatBeatClientHandler heatBeatClientHandler;

    @Resource
    private ReconnectHandler reconnectHandler;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof BaseMsgProto.BaseMsg) {
            BaseMsgProto.BaseMsg baseMsg = (BaseMsgProto.BaseMsg) msg;
            clientMsgHandler.handle(ctx, baseMsg);
        } else if (msg instanceof String) {
            String baseMsg = (String) msg;
            heatBeatClientHandler.handle(ctx, baseMsg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case WRITER_IDLE:
                    SendMsgUtil.sendMsg((NioSocketChannel)ctx.channel(), "ping");
                    break;
                case READER_IDLE:
                    //断线重连
                    log.info("已经 7 分钟未收到服务端消息了，尝试重连服务端...");
                    String oldChannelId = ctx.channel().id().asLongText();
                    Long userId = UserChannelMap.channelUserMap.remove(oldChannelId);
                    ctx.channel().close();
                    reconnectHandler.reconnect(userId, 0);
                    break;
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        System.out.println("连接服务端成功===》" + ctx);
        System.out.println("size = " + UserChannelMap.userChannel.size());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx){
        System.out.println("与服务端连接断开了 ===》 " + ctx);
        System.out.println("size = " + UserChannelMap.userChannel.size());
        //删除连接id 与 用户id 的映射信息，说明断开连接
        //String channelId = ctx.channel().id().asLongText();
        //UserChannelMap.channelUserMap.remove(channelId);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
    }
}
