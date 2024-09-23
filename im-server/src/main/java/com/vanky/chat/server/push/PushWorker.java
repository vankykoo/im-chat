package com.vanky.chat.server.push;

import com.vanky.chat.common.protobuf.BaseMsgProto;
import com.vanky.chat.server.pojo.BaseMsg;
import com.vanky.chat.server.session.ChatSessionMap;
import com.vanky.chat.server.utils.SendMsgUtil;
import com.vanky.chat.server.utils.WaitAckUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 实际推送的 类
 * @author vanky
 * @create 2024/4/17 20:19
 */
@Component
@Slf4j
public class PushWorker {

    @Resource
    private WaitAckUtil waitAckUtil;

    public void pushPrivateMsg(BaseMsg baseMsg){
        BaseMsgProto.BaseMsg msg = BaseMsg.BaseMsg2Proto(baseMsg);

        SendMsgUtil.sendMsg4Ack(msg);
    }

    /**
     * @param baseMsg 真正需要推送给客户端的消息
     * @param toUserId
     * @param pushGroupMsgInfo 需要推送的消息的信息，保存在服务端
     */
    public void pushGroupMsg(BaseMsg baseMsg, Long toUserId, BaseMsg pushGroupMsgInfo){
        BaseMsgProto.BaseMsg msg = BaseMsg.BaseMsg2Proto(baseMsg);

        ChatSessionMap.sendMessage(toUserId, msg);
        //等待ack的信息为推送群聊消息请求的消息
        waitAckUtil.saveWaitingAckMsgDetail(BaseMsg.BaseMsg2Proto(pushGroupMsgInfo),
                Long.parseLong(new String(pushGroupMsgInfo.getContent())));
    }

    public void pushOfflineMsgInfo(BaseMsg baseMsg){
        BaseMsgProto.BaseMsg msg = BaseMsg.BaseMsg2Proto(baseMsg);

        SendMsgUtil.sendMsg4Ack(msg);
    }

    public void pushOfflinePrivateMsg(BaseMsg baseMsg){
        BaseMsgProto.BaseMsg msg = BaseMsg.BaseMsg2Proto(baseMsg);

        SendMsgUtil.sendMsg4Ack(msg);
    }

    public void pushOfflineGroupMsg(BaseMsg baseMsg, Long toUserId){
        BaseMsgProto.BaseMsg msg = BaseMsg.BaseMsg2Proto(baseMsg);

        SendMsgUtil.sendMsg4Ack(msg, toUserId);
    }

    public void pushForwardMsg(BaseMsg baseMsg, Long toUserId){
        BaseMsgProto.BaseMsg msg = BaseMsg.BaseMsg2Proto(baseMsg);

        SendMsgUtil.sendMsg4Ack(msg, toUserId);
    }

    public void pushHistoryMsg(BaseMsg baseMsg) {
        BaseMsgProto.BaseMsg msg = BaseMsg.BaseMsg2Proto(baseMsg);

        SendMsgUtil.sendMsg4Ack(msg, baseMsg.getToUserId());
    }

    public void pushUserStatusChangeMsg(BaseMsg baseMsg){
        BaseMsgProto.BaseMsg msg = BaseMsg.BaseMsg2Proto(baseMsg);

        SendMsgUtil.sendMsg4Ack(msg, baseMsg.getToUserId());
    }
}
