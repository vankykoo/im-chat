package com.vanky.chat.client.handler;

import com.vanky.chat.client.processor.ClientMsgProxy;
import com.vanky.chat.client.channel.UserChannelMap;
import com.vanky.chat.common.cache.ReceivedMsgCache;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import com.vanky.chat.common.utils.StringRedisUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author vanky
 */
@Component
@Slf4j
public class ClientMsgHandler{

    @Resource
    private ClientMsgProxy clientMsgProxy;

    protected void handle(ChannelHandlerContext ctx, BaseMsgProto.BaseMsg msg){
        NioSocketChannel nioSocketChannel = (NioSocketChannel) ctx.channel();
        Long currentClientUserId = UserChannelMap.channelUserMap.get(nioSocketChannel.id().asLongText());

        //客户端消息去重
        String receivedMsgKey = ReceivedMsgCache.getClientKey(currentClientUserId, msg.getId());
        if (StringRedisUtil.ifExisted(receivedMsgKey)){
            log.warn("已经接收过这条消息了：{}", msg.getId());
            return;
        }

        switch (msg.getMsgType()){
            case 1:
                // 收到客户端的聊天消息
                clientMsgProxy.receiveSimpleMsg(nioSocketChannel, msg, currentClientUserId);
                break;
            case 2:
                // ack消息
                clientMsgProxy.receiveAckMsg(msg);
                break;
            case 5:
                // 接收到转发消息
                clientMsgProxy.receiveForwardMsg(nioSocketChannel, msg);
                break;
            case 7:
            case 8:
                // 离线私信/群聊消息 的 信息
                clientMsgProxy.receiveOfflineMsgInfo(nioSocketChannel, msg);
                break;
            case 9:
                // 全量私聊离线消息
                clientMsgProxy.receiveOfflinePrivateMsgDetail(nioSocketChannel, msg);
                break;
            case 10:
                // 全量群聊离线消息
                clientMsgProxy.receivedOfflineGroupMsgDetail(nioSocketChannel, msg);
                break;
            case 13:
                // 已读消息
                clientMsgProxy.readMsg(nioSocketChannel, msg);
                break;
            case 15:
                // 历史消息
                clientMsgProxy.receiveHistoryMsg(nioSocketChannel, msg);
                break;
            case 16:
                // 好友在线状态列表
                clientMsgProxy.receiveOnlineFriendsList(nioSocketChannel, msg);
                break;
            case 17:
                // 接收好友在线状态改变
                clientMsgProxy.receiveFriendStatusChangeMsg(nioSocketChannel, msg);
        }

        StringRedisUtil.put(receivedMsgKey, "", ReceivedMsgCache.CACHE_TIME, TimeUnit.MINUTES);
    }
}