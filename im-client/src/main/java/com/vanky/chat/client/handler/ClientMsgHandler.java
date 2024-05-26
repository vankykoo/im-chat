package com.vanky.chat.client.handler;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.google.protobuf.ByteString;
import com.vanky.chat.client.processor.ForwardMsgProcessor;
import com.vanky.chat.client.channel.UserChannelMap;
import com.vanky.chat.client.processor.GroupMsgProcessor;
import com.vanky.chat.client.processor.PrivateMsgProcessor;
import com.vanky.chat.client.utils.SendAckMsgUtil;
import com.vanky.chat.common.bo.OfflineMsgInfo;
import com.vanky.chat.common.cache.ReceivedMsgCache;
import com.vanky.chat.common.constant.TypeEnum;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import com.vanky.chat.common.utils.CommonConverter;
import com.vanky.chat.common.utils.MsgEncryptUtil;
import com.vanky.chat.common.utils.StringRedisUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * @author vanky
 */
@Component
@Slf4j
public class ClientMsgHandler{

    @Resource
    @Lazy
    private ForwardMsgProcessor forwardMsgProcessor;

    @Resource
    private MsgEncryptUtil msgEncryptUtil;

    @Resource
    private PrivateMsgProcessor privateMsgProcessor;

    @Resource
    private GroupMsgProcessor groupMsgProcessor;

    protected void handle(ChannelHandlerContext ctx, BaseMsgProto.BaseMsg msg){
        NioSocketChannel nioSocketChannel = (NioSocketChannel) ctx.channel();
        Long currentClientUserId = UserChannelMap.context.get(nioSocketChannel.hashCode());

        //客户端消息去重
        String receivedMsgKey = ReceivedMsgCache.getClientKey(currentClientUserId, msg.getId());
        if (StringRedisUtil.ifExisted(receivedMsgKey)){
            log.warn("已经接收过这条消息了：{}", msg.getId());
            return;
        }

        int msgType = msg.getMsgType();

        switch (msgType){
            case 1: //收到客户端的聊天消息
                if (msg.getChatType() == TypeEnum.ChatType.GROUP_CHAT.getValue()){
                    groupMsgProcessor.receivedGroupMsg(msg, currentClientUserId, nioSocketChannel);
                }else{
                    privateMsgProcessor.receivePrivateMsg(msg, nioSocketChannel);
                }
                break;
            case 2:
                //ack 消息
                log.info("客户端收到服务端ack消息：{}", msg);
                break;
            case 5: //接收到转发消息
                //发ack消息
                SendAckMsgUtil.sendAckMsg(msg, nioSocketChannel, TypeEnum.MsgType.ACK_MSG.getValue());
                //处理转发消息
                forwardMsgProcessor.forwardMsg(msg);
                break;
            case 7:
            case 8:
                //离线私信/群聊消息 的 信息
                //群聊：from：groupId  to：userId
                //收到消息需要解密
                String rowContentJsonString = msgEncryptUtil
                        .msgDecrypt(msg.getContent(), msg.getFromUserId(),
                                msg.getToUserId(), msg.getChatType());

                OfflineMsgInfo offlineMsgInfo = OfflineMsgInfo.json2OfflineMsg(rowContentJsonString);
                if (msg.getChatType() == TypeEnum.ChatType.PRIVATE_CHAT.getValue()){
                    //私聊的消息需要解密,因为服务端没有保存明文
                    byte[] bytes = ByteString.copyFrom(offlineMsgInfo.getContent()).toByteArray();
                    String rowContent = msgEncryptUtil.msgDecrypt(ByteString.copyFrom(bytes), msg.getFromUserId(),
                            msg.getToUserId(), TypeEnum.ChatType.PRIVATE_CHAT.getValue());
                    log.info("私信解码最后一条消息是：{}", rowContent);
                }

                log.info("收到离线消息的基本信息：{}", offlineMsgInfo);

                //发送ack消息
                SendAckMsgUtil.sendAckMsg(msg, nioSocketChannel, TypeEnum.MsgType.SIMPLE_ACK_MSG.getValue());
                break;
            case 9:
                //全量私聊离线消息
                privateMsgProcessor.receiveOfflinePrivateMsgDetail(msg, nioSocketChannel);
                break;
            case 10:
                //全量群聊离线消息
                groupMsgProcessor.receivedOfflineGroupMsgDetail(msg, nioSocketChannel);
                break;
            case 13:
                //发送ack消息
                SendAckMsgUtil.sendAckMsg(msg, nioSocketChannel, TypeEnum.MsgType.SIMPLE_ACK_MSG.getValue());

                List<Long> longList = JSONObject.parseObject(CommonConverter.byteString2String(msg.getContent()), new TypeReference<>() {});

                if (msg.getChatType() == 0){
                    //私聊已读：
                    log.info("私信已读消息id集：{}", longList);
                }else{
                    //群聊已读
                    log.info("群聊已读消息的最大id：{}", longList.get(0));
                }
        }

        StringRedisUtil.put(receivedMsgKey, "", ReceivedMsgCache.CACHE_TIME, TimeUnit.MINUTES);
    }
}