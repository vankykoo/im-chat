package com.vanky.chat.server.processor;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.protobuf.ByteString;
import com.vanky.chat.common.bo.OfflineGroupMsgDetailBo;
import com.vanky.chat.common.bo.OfflineMsgDetailBo;
import com.vanky.chat.common.cache.RedisCacheKey;
import com.vanky.chat.common.cache.RedisSimpleCacheName;
import com.vanky.chat.common.constant.TypeEnum;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import com.vanky.chat.common.utils.CommonConverter;
import com.vanky.chat.common.utils.MsgEncryptUtil;
import com.vanky.chat.common.utils.RedisUtil;
import com.vanky.chat.server.pojo.BaseMsg;
import com.vanky.chat.server.pojo.GroupUser;
import com.vanky.chat.server.push.PushProxy;
import com.vanky.chat.server.service.BaseMsgService;
import com.vanky.chat.server.service.GroupUserService;
import com.vanky.chat.server.service.OfflineMsgService;
import com.vanky.chat.server.utils.WaitAckUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author vanky
 * @create 2024/3/31 17:27
 */
@Component
@Slf4j
public class AckMsgProcessor {

    @Resource
    private BaseMsgService baseMsgService;

    @Resource
    private GroupUserService groupUserService;

    @Resource
    private OfflineMsgService offlineMsgService;

    @Resource
    private WaitAckUtil waitAckUtil;

    @Resource
    private GroupMsgProcessor groupMsgProcessor;

    @Resource
    private PrivateMsgProcessor privateMsgProcessor;

    @Resource
    private PushProxy pushProxy;

    @Resource
    private MsgEncryptUtil msgEncryptUtil;

    public void processAckMsg(BaseMsgProto.BaseMsg msg){
        int chatType = msg.getChatType();

        switch (chatType){
            case 0:
                // 私聊消息
                privateMsgAck(msg);
                break;
            case 1:
                // 群聊消息
                groupMsgAck(msg);
                break;
        }
    }

    /**
     * 收到私信ack
     * @param msg
     */
    public void privateMsgAck(BaseMsgProto.BaseMsg msg){

        String content = CommonConverter.byteString2String(msg.getContent());

        //1.删除redis中等待ack的key
        waitAckUtil.deleteWaitingAckMsgDetail(content);

        //2.修改msg状态为已送达
        baseMsgService.setMsgHasNotReadByUniqueId(Long.parseLong(content));

        //3.查询比当前消息id小且没有ack的消息【兜底】

        log.info("服务端收到客户端 私信消息 的ack消息：{}",msg.getContent());
    }

    /**
     * 收到群消息ack
     * @param msg
     */
    public void groupMsgAck(BaseMsgProto.BaseMsg msg){
        //from:用户id
        //to：群id
        LambdaQueryWrapper<GroupUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GroupUser::getGroupId, msg.getToUserId())
                .eq(GroupUser::getUserId, msg.getFromUserId());

        String content = CommonConverter.byteString2String(msg.getContent());

        GroupUser groupUser = groupUserService.getOne(wrapper);

        //修改last_ack_id
        GroupUser user = GroupUser.builder()
                .id(groupUser.getId())
                .lastAckMsgId(Long.parseLong(content)).build();

        groupUserService.updateById(user);

        waitAckUtil.deleteWaitingAckMsgDetail(content);

        log.info("服务端收到客户端 群聊消息 的ack消息：{}",msg.getContent());
    }

    /**
     * 收到历史消息ack
     * @param msg
     */
    public void historyMsgAck(BaseMsgProto.BaseMsg msg){
        // 修改该用户在redis（本地）中的oldestMsgId
        String cacheKey = RedisCacheKey.PRIVATE_OLDEST_MSG_ID_KEY +
                msg.getFromUserId() + RedisSimpleCacheName.UNION_KEY + msg.getToUserId();

        RedisUtil.put(cacheKey, msg.getContent().toString());

        // 删除redis中缓存
        waitAckUtil.deleteWaitingAckMsgDetail(Long.toString(msg.getId()));
    }


    /**
     * 重新发送未接收到的私信消息
     * @param lastAckId
     * @param userId
     */
    private void privateMsgResend(Long lastAckId, Long userId){
        LambdaQueryWrapper<BaseMsg> wrapper = new LambdaQueryWrapper<>();
        wrapper.lt(BaseMsg::getId, lastAckId)
                .eq(BaseMsg::getToUserId, userId)
                .eq(BaseMsg::getStatus, TypeEnum.MsgStatus.SENT.getValue());

        List<BaseMsg> hasNotReadMsgList = baseMsgService.list(wrapper);

        if (hasNotReadMsgList != null && hasNotReadMsgList.size() > 0){
            //重新推送
            for (BaseMsg msg : hasNotReadMsgList) {
                pushProxy.privateMsg(msg);
            }
        }
    }

    /**
     * 离线消息拉取ack处理
     * @param msg
     */
    public void offlineMsgAck(BaseMsgProto.BaseMsg msg) {

        String content = CommonConverter.byteString2String(msg.getContent());

        if (msg.getChatType() == TypeEnum.ChatType.PRIVATE_CHAT.getValue()){
            //私信

            //2.获取拉取的离线消息的uniqueId
            String uniqueId = content;
            BaseMsg baseMsg = waitAckUtil.getWaitingAckMsgDetail(uniqueId);

            //需要解密
            String jsonString = msgEncryptUtil.msgDecrypt(ByteString.copyFrom(baseMsg.getContent()),
                    msg.getFromUserId(), msg.getToUserId(), TypeEnum.ChatType.PRIVATE_CHAT.getValue());

            if (baseMsg != null){
                List<OfflineMsgDetailBo> list = JSONObject
                        .parseObject(jsonString, new TypeReference<>() {});

                List<Long> uniqueIds = list.stream()
                        .map(offlineMsg -> {return Long.parseLong(offlineMsg.getUniqueId());})
                        .collect(Collectors.toList());

                //3.修改msg状态为已送达
                baseMsgService.setMsgHasNotReadByUniqueIds(uniqueIds);

                //4.删除数据库中的离线消息
                offlineMsgService.deleteOfflineMsgByUniqueId(uniqueIds);

                //5.推送剩下没有推送的离线消息
                if (uniqueIds.size() == 100){
                    privateMsgProcessor.pushPrivateOfflineMsgDetail(msg.getToUserId(), msg.getFromUserId());
                }
            }
        }else{
            //如果是群聊的离线消息ack，应该修改群用户的last_ack_id，
            //修改的值为发送的消息的最后一条的消息的unique_id
            String ackMsgId = content;

            //1.到redis中查已经接收的数据，获取最后一条的uniqueId
            BaseMsg waitingAckMsgDetail = waitAckUtil.getWaitingAckMsgDetail(ackMsgId);

            //需要解密
            String jsonString = msgEncryptUtil.msgDecrypt(ByteString.copyFrom(waitingAckMsgDetail.getContent()),
                    waitingAckMsgDetail.getFromUserId(), waitingAckMsgDetail.getToUserId(),
                    waitingAckMsgDetail.getChatType());

            List<OfflineGroupMsgDetailBo> offlineMsgList = JSONObject
                    .parseObject(jsonString, new TypeReference<>() {});

            Long lastAckId = offlineMsgList.get(offlineMsgList.size() - 1).getUniqueId();

            //2.修改用户的lastAckId
            groupUserService.updateLastAckId(msg.getFromUserId(),msg.getToUserId(), lastAckId);

            //3.如果离线消息没有推送完就继续推送
            if (offlineMsgList.size() == 100){
                groupMsgProcessor.pushOfflineMsgDetail(msg.getToUserId(),
                        msg.getFromUserId(), lastAckId + 1);
            }
        }

        waitAckUtil.deleteWaitingAckMsgDetail(Long.toString(msg.getUniqueId()));
    }

    /**
     * 收到ack,只进行删除消息缓存操作
     * @param msg
     */
    public void simpleAck(BaseMsgProto.BaseMsg msg) {
        //只需要删除等待ack的redis内容即可
        waitAckUtil.deleteWaitingAckMsgDetail(CommonConverter.byteString2String(msg.getContent()));
    }
}
