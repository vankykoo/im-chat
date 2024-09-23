package com.vanky.chat.server.push;

import com.google.protobuf.ByteString;
import com.vanky.chat.common.bo.BaseMsgBo;
import com.vanky.chat.common.bo.GlobalChatSessionBo;
import com.vanky.chat.common.cache.RedisCacheKey;
import com.vanky.chat.common.constant.TypeEnum;
import com.vanky.chat.common.utils.MsgEncryptUtil;
import com.vanky.chat.common.utils.RedisUtil;
import com.vanky.chat.server.pojo.BaseMsg;
import com.vanky.chat.server.pojo.GroupMsg;
import com.vanky.chat.server.service.GroupMsgService;
import com.vanky.chat.server.service.GroupUserService;
import com.vanky.chat.server.service.OfflineMsgService;
import com.vanky.chat.server.session.ChatSessionMap;
import com.vanky.chat.server.utils.MsgGenerator;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

/**
 * 从 kafka 获取消息，判断用户是否在线
 * 在线的话交给 pushWorker 处理
 * @author vanky
 * @create 2024/4/17 20:19
 */
@Component
@Slf4j
public class PushServer {

    @Resource
    private GroupUserService groupUserService;

    @Resource
    private PushWorker pushWorker;

    @Resource
    private OfflineMsgService offlineMsgService;

    @Resource
    private GroupMsgService groupMsgService;

    @Resource
    private MsgGenerator msgGenerator;

    @Resource
    private MsgEncryptUtil msgEncryptUtil;

    // 重试 5 次，重试间隔 100 毫秒,最大间隔 1 秒
    @RetryableTopic(
            attempts = "5",
            backoff = @Backoff(delay = 100, maxDelay = 1000)
    )

    /**
     * 获取 kafka 中privateMsg topic中的消息
     * 判断用户是否在线 来选择 走在线推送还是离线推送
     */
    @KafkaListener(topics = "privateMsg")
    public void privateMsgListener(@Payload BaseMsgBo baseMsgBo, Acknowledgment acknowledgment){
        //判断用户是否在线
        GlobalChatSessionBo globalChatSessionBo = ifUserOnline(baseMsgBo.getToUserId());

        BaseMsg baseMsg = new BaseMsg();
        BeanUtils.copyProperties(baseMsgBo, baseMsg);
        baseMsg.setContent(baseMsgBo.getContent());

        if (globalChatSessionBo == null){
            //不在线，保存为离线消息
            log.warn("用户不在线，私聊消息保存为离线消息");
            offlineMsgService.saveOfflineMsg(baseMsg);
        }else {
            //判断是否在当前服务端
            if (isCurrentServer(baseMsgBo.getToUserId())){
                //在当前客户端，直接发消息
                pushWorker.pushPrivateMsg(baseMsg);
            }else {
                String address = globalChatSessionBo.getHost() + ":" + globalChatSessionBo.getPort();
                BaseMsg forwardMsg = msgGenerator.generateForwardMsg(address, baseMsg, baseMsg.getToUserId());

                pushWorker.pushForwardMsg(forwardMsg, baseMsg.getToUserId());
            }
        }

        //手动提交
        acknowledgment.acknowledge();
    }

    /**
     * 判断用户是否在线选择是否推送历史消息
     * @param baseMsgBo
     * @param acknowledgment
     */
    @KafkaListener(topics = "historyMsg")
    public void historyMsgListener(@Payload BaseMsgBo baseMsgBo, Acknowledgment acknowledgment){
        // 1. 判断用户是否在线
        GlobalChatSessionBo globalChatSessionBo = ifUserOnline(baseMsgBo.getToUserId());

        // todo 2. 在线：直接推送
        if (globalChatSessionBo != null){
            BaseMsg baseMsg = new BaseMsg();
            BeanUtils.copyProperties(baseMsgBo, baseMsg);

            pushWorker.pushHistoryMsg(baseMsg);
        }else {
             //3. 不在线： 不推送了
            log.warn("用户不在线，历史消息无法推送~");
        }

        //手动提交
        acknowledgment.acknowledge();
    }

    /**
     * 监听用户状态改变通知消息
     * @param baseMsgBo
     * @param acknowledgment
     */
    @KafkaListener(topics = "userStatusChangeMsg")
    public void userStatusChangeMsgListener(@Payload BaseMsgBo baseMsgBo, Acknowledgment acknowledgment){
        BaseMsg baseMsg = new BaseMsg();
        BeanUtils.copyProperties(baseMsgBo, baseMsg);

        pushWorker.pushUserStatusChangeMsg(baseMsg);

        // 手动提交
        acknowledgment.acknowledge();
    }

    /**
     * 获取 kafka groupMsg topic中的消息
     * 判断用户是否在线 来选择 走在线推送还是离线推送
     */
    @KafkaListener(topics = "groupMsg")
    public void groupMsgListener(@Payload BaseMsgBo baseMsgBo, Acknowledgment acknowledgment){
        //获取需要推送的消息的uniqueId
        String msgContent = new String(baseMsgBo.getContent());
        long uniqueId = Long.parseLong(msgContent);
        GroupMsg groupMsg = groupMsgService.getGroupMsgByUniqueId(uniqueId);

        if (groupMsg == null){
            throw new NullPointerException("查询内容为空！");
        }
        //转为BaseMsg，需要保存到redis
        BaseMsg baseMsg = new BaseMsg();
        BeanUtils.copyProperties(baseMsgBo, baseMsg);
        //这个content需要拿接收方的共享密钥进行加密
        //4.通过与接收方的共享密钥进行消息加密
        ByteString byteStringContent = msgEncryptUtil.msgEncrypt(groupMsg.getContent(),
                baseMsg.getFromUserId(), baseMsg.getToUserId(),
                TypeEnum.ChatType.GROUP_CHAT.getValue());

        //判断是否在当前服务端
        if (isCurrentServer(baseMsg.getToUserId())){
            //在当前客户端，直接发消息
            BaseMsg msg = GroupMsg.groupMsg2BaseMsg(groupMsg, byteStringContent.toByteArray());
            pushWorker.pushGroupMsg(msg, baseMsg.getToUserId(), baseMsg);
            log.info("消息推送成功！{}", baseMsg);
        }else {
            GlobalChatSessionBo globalChatSessionBo = RedisUtil
                    .get(RedisCacheKey.CHAT_SESSION_KEY + baseMsg.getToUserId(), GlobalChatSessionBo.class);

            if (globalChatSessionBo == null){
                log.info("用户不在线！");
            }else{
                String address = globalChatSessionBo.getHost() + ":" + globalChatSessionBo.getPort();
                BaseMsg forwardMsg = msgGenerator.generateForwardMsg(address, baseMsg, baseMsg.getToUserId());

                pushWorker.pushForwardMsg(forwardMsg, baseMsg.getToUserId());
                log.info("消息推送成功！{}", baseMsg);
            }
        }

        if (groupMsg.getUserId().equals(baseMsgBo.getToUserId())){
            //发送成功后，修改发送方用户的last_ack_id
            groupUserService.updateLastAckId(baseMsgBo.getToUserId(),
                    baseMsgBo.getFromUserId(), baseMsgBo.getUniqueId());
        }

        //如果不在线，群聊消息不用保存离线消息，等待用户上线自动拉取

        //手动提交
        acknowledgment.acknowledge();
    }

    /**
     * 用于判断用户是否在线
     * @return
     */
    private GlobalChatSessionBo ifUserOnline(Long userId){
        String key = RedisCacheKey.CHAT_SESSION_KEY + userId;
        return RedisUtil.get(key, GlobalChatSessionBo.class);
    }

    /**
     * 如果用户在线，判断连接是不是在当前服务器
     * @return
     */
    private boolean isCurrentServer(Long userId){
        return ChatSessionMap.chatSessionMap.containsKey(userId);
    }

    /**
     * 获取 kafka offlineMsgInfo topic中的消息
     * 进行推送离线消息内容
     */
    @KafkaListener(topics = "offlineMsgInfo")
    public void offlineMsgInfoListener(@Payload BaseMsgBo baseMsgBo, Acknowledgment acknowledgment){
        BaseMsg baseMsg = new BaseMsg();
        BeanUtils.copyProperties(baseMsgBo, baseMsg);
        pushWorker.pushOfflineMsgInfo(baseMsg);

        //手动提交
        acknowledgment.acknowledge();
    }

    /**
     * 获取 kafka offlinePrivateMsg topic中的消息
     * 进行推送离线消息内容
     */
    @KafkaListener(topics = "offlinePrivateMsg")
    public void offlinePrivateMsgListener(BaseMsgBo baseMsgBo, Acknowledgment acknowledgment){
        BaseMsg baseMsg = new BaseMsg();
        BeanUtils.copyProperties(baseMsgBo, baseMsg);
        pushWorker.pushOfflinePrivateMsg(baseMsg);

        //手动提交
        acknowledgment.acknowledge();
    }

    /**
     * 获取 kafka offlineGroupMsg topic中的消息
     * 进行推送离线消息内容
     */
    @KafkaListener(topics = "offlineGroupMsg")
    public void offlineGroupMsgListener(BaseMsgBo baseMsgBo, Acknowledgment acknowledgment){
        BaseMsg baseMsg = new BaseMsg();
        BeanUtils.copyProperties(baseMsgBo, baseMsg);
        pushWorker.pushOfflineGroupMsg(baseMsg, baseMsg.getToUserId());

        //手动提交
        acknowledgment.acknowledge();
    }
}
