package com.vanky.chat.server.handler;

import com.vanky.chat.common.protobuf.BaseMsgProto;
import com.vanky.chat.server.processor.LoginMsgProcessor;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
@Slf4j
public class ServerMultiProtocolHandler extends SimpleChannelInboundHandler<Object> {
    @Resource
    private ServerMsgHandler serverMsgHandler;

    @Resource
    private HeatBeatServerHandler heatBeatServerHandler;

    @Resource
    private LoginMsgProcessor loginMsgProcessor;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof BaseMsgProto.BaseMsg) {
            BaseMsgProto.BaseMsg baseMsg = (BaseMsgProto.BaseMsg) msg;
            serverMsgHandler.handle(ctx, baseMsg);
        } else if (msg instanceof String) {
            String baseMsg = (String) msg;
            heatBeatServerHandler.handle(ctx, baseMsg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case READER_IDLE:
                    log.info("已经 7 min没有收到客户端的消息/心跳包了, 关闭连接");
                    ctx.close();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("上线了！=====》 " + ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("离线了！=====》 " + ctx);
        //删除redis 中的缓存
        loginMsgProcessor.userChannelDisconnect(ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
