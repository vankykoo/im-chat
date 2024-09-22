package com.vanky.chat.server.handler;

import com.google.protobuf.ByteString;
import com.vanky.chat.common.cache.ReceivedMsgCache;
import com.vanky.chat.common.constant.TypeEnum;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import com.vanky.chat.common.utils.CommonConverter;
import com.vanky.chat.common.utils.StringRedisUtil;
import com.vanky.chat.server.processor.AckMsgProcessor;
import com.vanky.chat.server.processor.GroupMsgProcessor;
import com.vanky.chat.server.processor.LoginMsgProcessor;
import com.vanky.chat.server.processor.PrivateMsgProcessor;
import com.vanky.chat.server.utils.SendMsgUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author vanky
 * @create 2024/3/29 16:50
 */
@Component
@Slf4j
public class ServerMsgHandler{

    @Resource
    private LoginMsgProcessor loginMsgProcessor;

    @Resource
    private PrivateMsgProcessor privateMsgProcessor;

    @Resource
    private GroupMsgProcessor groupMsgProcessor;

    @Resource
    private AckMsgProcessor ackMsgProcessor;

    //@Override
    protected void handle(ChannelHandlerContext ctx, BaseMsgProto.BaseMsg msg) throws Exception {
        int msgType = msg.getMsgType();

        //消息去重
        if (StringRedisUtil.ifExisted(ReceivedMsgCache.getServerKey(msg.getId()))){
            log.warn("已经接收过这条消息了：{}", msg.getId());
            return;
        }

        switch (msgType){
            case 0:
                //登录消息
                loginMsgProcessor.registerLoginUser(msg, (NioSocketChannel) ctx.channel());
                //推送离线消息给用户
                privateMsgProcessor.pushOfflineMsg(msg.getFromUserId());
                groupMsgProcessor.pushOfflineMsg(msg.getFromUserId());
                break;
            case 1:
                //聊天消息
                if (msg.getChatType() == TypeEnum.ChatType.PRIVATE_CHAT.getValue()){
                    //私聊
                    privateMsgProcessor.sendPrivateMsg(msg, ctx.channel());
                }else {
                    //群聊消息
                    groupMsgProcessor.receiveGroupMsg(msg, ctx.channel());
                }
                break;
            case 2:
                //ack消息
                ackMsgProcessor.processAckMsg(msg);
                break;
            case 5:
                //服务端收到转发消息
                break;
            case 11:
                //收到离线消息拉取成功的ack消息
                ackMsgProcessor.offlineMsgAck(msg);
                break;
            case 12:
                ackMsgProcessor.simpleAck(msg);
                break;
            case 13:
                SendMsgUtil.sendAckMsg(ctx.channel(), msg, TypeEnum.MsgType.ACK_MSG);
                //客户端进入聊天框，将已经送达的消息已读处理
                if (msg.getChatType() == TypeEnum.ChatType.PRIVATE_CHAT.getValue()){
                    //私聊
                    privateMsgProcessor.setMsgHasRead(ctx.channel(), msg.getFromUserId(), msg.getToUserId());
                }else{
                    //群聊
                    groupMsgProcessor.setGroupMsgHasRead(ctx.channel(), msg.getFromUserId(), msg.getToUserId());
                }
                break;
            case 14:
                loginMsgProcessor.userLogout(msg);
                break;
            case 15:
                // 拉取历史记录
                privateMsgProcessor.pushHistoryMsg(msg.getFromUserId(), msg.getToUserId(), Long.parseLong(CommonConverter.byteString2String(msg.getContent())));
                break;
        }

        //处理
        StringRedisUtil.put(ReceivedMsgCache.getServerKey(msg.getId()), "",
                ReceivedMsgCache.CACHE_TIME, TimeUnit.MINUTES);
    }
}
