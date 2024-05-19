package com.vanky.chat.server.processor;

import com.vanky.chat.common.bo.OfflineMsgDetailBo;
import com.vanky.chat.common.constant.TypeEnum;
import com.vanky.chat.common.bo.OfflineMsgInfo;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import com.vanky.chat.server.pojo.BaseMsg;
import com.vanky.chat.server.push.PushProxy;
import com.vanky.chat.server.service.BaseMsgService;
import com.vanky.chat.server.service.OfflineMsgService;
import com.vanky.chat.server.utils.MsgGenerator;
import com.vanky.chat.server.utils.SendMsgUtil;
import io.netty.channel.Channel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author vanky
 * @create 2024/3/30 15:14
 */
@Component
@Slf4j
public class PrivateMsgProcessor {

    @Resource
    private OfflineMsgService offlineMsgService;

    @Resource
    private BaseMsgService baseMsgService;

    @Resource
    private MsgGenerator msgGenerator;

    @Resource
    private PushProxy pushProxy;

    /**
     * 转发私信
     * @param msg
     * @param channel
     */
    public void sendPrivateMsg(BaseMsgProto.BaseMsg msg, Channel channel){
        //消息入库
        baseMsgService.saveMsg(msg);

        //返回ack消息
        SendMsgUtil.sendAckMsg(channel, msg, TypeEnum.MsgType.ACK_MSG);

        //放入 kafka 进行推送
        BaseMsg baseMsg = BaseMsg.Proto2BaseMsg(msg);

        pushProxy.privateMsg(baseMsg);
    }

    /**
     * 推送离线私信消息
     * @param userId
     */
    public void pushOfflineMsg(Long userId){
        //1.先推送每个聊天框的离线消息基本信息：未读数、最后一条信息、时间
        List<OfflineMsgInfo> offlineMsgInfoList = offlineMsgService.getOfflineMsgInfoByUserId(userId);

        if (offlineMsgInfoList == null || offlineMsgInfoList.size() == 0){
            return;
        }

        for (OfflineMsgInfo offlineMsgInfo : offlineMsgInfoList) {
            BaseMsg msg = msgGenerator.generateOfflinePrivateMsg(offlineMsgInfo, userId);

            //存入kafka进行推送
            pushProxy.offlineMsg(msg);
        }

        //2.再全量推送每个聊天框的信息
        for (OfflineMsgInfo offlineMsgInfo : offlineMsgInfoList) {
            pushPrivateOfflineMsgDetail(userId, offlineMsgInfo.getFromUserId());
        }
    }

    /**
     * 推送一次离线消息（最多100条）
     * @param userId
     * @param fromUserId
     */
    public void pushPrivateOfflineMsgDetail(Long userId, Long fromUserId){
        //1.获取全量消息
        List<OfflineMsgDetailBo> offlineMsgList = offlineMsgService.get100OfflineMsg(fromUserId,userId);

        if (offlineMsgList == null || offlineMsgList.size() == 0){
            return;
        }

        //2. 生成基本格式，一次推送100条
        BaseMsg baseMessage = msgGenerator
                .generateOfflinePrivateMsgDetail(offlineMsgList, fromUserId, userId);

        //3. 推送消息
        pushProxy.offlineMsg(baseMessage);
    }

    /**
     * 将已抵达的消息进行已读处理
     * @param fromUserId
     * @param toUserId
     */
    public void setMsgHasRead(Channel channel, long fromUserId, long toUserId) {
        List<Long> hasReadMsgIds = baseMsgService.setMsgHasReadByUser(fromUserId, toUserId);

        //发送已读消息的uniqueId到客户端
        BaseMsgProto.BaseMsg baseMsg = msgGenerator.generateHasReadMsgUniqueIdBaseMsg(hasReadMsgIds, fromUserId, toUserId, TypeEnum.ChatType.PRIVATE_CHAT);

        //等待ack
        SendMsgUtil.sendMsg4Ack(channel, baseMsg);
    }
}
