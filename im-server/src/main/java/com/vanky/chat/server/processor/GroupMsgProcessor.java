package com.vanky.chat.server.processor;

import com.google.protobuf.ByteString;
import com.vanky.chat.common.bo.OfflineGroupMsgDetailBo;
import com.vanky.chat.common.bo.OfflineMsgInfo;
import com.vanky.chat.common.cache.OnlineCache;
import com.vanky.chat.common.constant.TypeEnum;
import com.vanky.chat.common.feign.leafFeign.IdGeneratorFeignClient;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import com.vanky.chat.common.utils.CommonConverter;
import com.vanky.chat.common.utils.MsgEncryptUtil;
import com.vanky.chat.common.utils.RedisUtil;
import com.vanky.chat.server.pojo.BaseMsg;
import com.vanky.chat.server.push.PushProxy;
import com.vanky.chat.server.service.GroupMsgService;
import com.vanky.chat.server.service.GroupUserService;
import com.vanky.chat.server.utils.MsgGenerator;
import com.vanky.chat.server.utils.SendMsgUtil;
import com.vanky.chat.server.utils.WaitAckUtil;
import io.netty.channel.Channel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vanky
 * @create 2024/3/31 11:06
 */
@Component
@Slf4j
public class GroupMsgProcessor {

    @Resource
    private GroupMsgService groupMsgService;

    @Resource
    private WaitAckUtil waitAckUtil;

    @Resource
    private IdGeneratorFeignClient idGeneratorFeignClient;

    @Resource
    private MsgGenerator msgGenerator;

    @Resource
    private GroupUserService groupUserService;

    @Resource
    private PushProxy pushProxy;

    @Resource
    private MsgEncryptUtil msgEncryptUtil;

    /**
     * 接收到客户端发送的群消息
     * @param msg
     */
    public void receiveGroupMsg(BaseMsgProto.BaseMsg msg, Channel channel){
        //1.通过与发送方的共享密钥进行消息解码
        ByteString content = msg.getContent();
        String rowContent = msgEncryptUtil.msgDecrypt(content, msg.getToUserId(), msg.getFromUserId(),
                TypeEnum.ChatType.GROUP_CHAT.getValue());
        log.info("群消息解码成功：{}", rowContent);

        //2.消息入库
        BaseMsgProto.BaseMsg groupMsg = updateUniqueIdAndContent(msg, rowContent);
        groupMsgService.saveMsg(groupMsg);

        //3.ack
        SendMsgUtil.sendAckMsg(channel, msg, TypeEnum.MsgType.ACK_MSG);

        //5.存入kafka进行推送
        //查询群内的所有在线的用户
        String key = OnlineCache.GROUP_ONLINE_USER + msg.getToUserId();
        List<Long> memberIds = RedisUtil.sget(key, Long.class).stream().toList();

        BaseMsg groupMsg2Push = msgGenerator.generateGroupMsg2Push(groupMsg, 0L);

        for (Long memberId : memberIds){
            groupMsg2Push.setToUserId(memberId);

            pushProxy.groupMsg(groupMsg2Push);
        }
    }

    private BaseMsgProto.BaseMsg updateUniqueIdAndContent(BaseMsgProto.BaseMsg msg, String rowContent){
        BaseMsg baseMsg = BaseMsg.Proto2BaseMsg(msg);
        baseMsg.setUniqueId(idGeneratorFeignClient.nextId().getData());
        baseMsg.setContent(rowContent.getBytes());
        return BaseMsg.BaseMsg2Proto(baseMsg);
    }

    private BaseMsgProto.BaseMsg updateContent(BaseMsgProto.BaseMsg msg, ByteString byteStringContent){
        BaseMsg baseMsg = BaseMsg.Proto2BaseMsg(msg);
        baseMsg.setContent(byteStringContent.toByteArray());
        return BaseMsg.BaseMsg2Proto(baseMsg);
    }

    /**
     * 推送群聊离线消息
     * @param userId
     */
    public void pushOfflineMsg(long userId) {
        //1.获取群聊离线消息的基本信息
        List<OfflineMsgInfo> offlineMsgInfoList = groupMsgService.getOfflineMsgInfo(userId);

        if (offlineMsgInfoList == null || offlineMsgInfoList.size() == 0){
            return ;
        }

        //2.存入kafka， 发送离线消息的基本信息
        for (OfflineMsgInfo offlineMsgInfo : offlineMsgInfoList) {
            BaseMsg msg = msgGenerator.generateOfflineGroupMsg(offlineMsgInfo, userId);
            //这个加过密
            pushProxy.offlineMsg(msg);
        }

        //3.获取全量离线消息,存入kafka进行推送
        for (OfflineMsgInfo offlineMsgInfo : offlineMsgInfoList) {
            pushOfflineMsgDetail(userId, offlineMsgInfo.getFromUserId(), offlineMsgInfo.getGotoTimestamp());
        }
    }

    /**
     * 推送离线消息，每100条一次
     * @param userId
     * @param groupId
     * @param lastAckId
     */
    public void pushOfflineMsgDetail(long userId, Long groupId, Long lastAckId){
        //3.1 获取该群聊的离线消息的前100条，其他的等得到ack后一条一条发送
        List<OfflineGroupMsgDetailBo> offlineMsgs = groupMsgService
                .getFollowGroupMsg4LastAckIdLt100(groupId, lastAckId);

        if (offlineMsgs == null || offlineMsgs.size() == 0){
            return;
        }

        //3.2 分装，每100条为一个
        BaseMsg baseMsg = msgGenerator
                .generateOfflineGroupMsgDetail(offlineMsgs, groupId, userId);

        //3.3 存入kafka
        pushProxy.offlineMsg(baseMsg);
    }

    /**
     * 将群内消息已读，修改群用户的last_read_id
     * @param channel
     * @param groupId
     * @param userId
     */
    public void setGroupMsgHasRead(Channel channel, long groupId, long userId) {
        Long lastReadId = groupUserService.setLastReadMsgId(groupId, userId);
        List<Long> list = new ArrayList<>();
        list.add(lastReadId);

        BaseMsgProto.BaseMsg baseMsg = msgGenerator.generateHasReadMsgUniqueIdBaseMsg(list, groupId, userId, TypeEnum.ChatType.GROUP_CHAT);

        SendMsgUtil.sendMsg4Ack(channel, baseMsg);
    }

}
