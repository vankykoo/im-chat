package com.vanky.chat.server.processor;

import com.vanky.chat.common.bo.GlobalChatSessionBo;
import com.vanky.chat.common.bo.UserBo;
import com.vanky.chat.common.cache.ChannelCache;
import com.vanky.chat.common.cache.OnlineCache;
import com.vanky.chat.common.cache.RedisCacheKey;
import com.vanky.chat.common.constant.TypeEnum;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import com.vanky.chat.common.utils.CommonMsgGenerator;
import com.vanky.chat.common.utils.RedisUtil;
import com.vanky.chat.server.pojo.GroupUser;
import com.vanky.chat.server.service.GroupUserService;
import com.vanky.chat.server.session.ChannelUserMap;
import com.vanky.chat.server.session.ChatSessionMap;
import com.vanky.chat.server.utils.MsgGenerator;
import com.vanky.chat.server.utils.SendMsgUtil;
import io.netty.channel.socket.nio.NioSocketChannel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * @author vanky
 * @create 2024/3/29 16:54
 */
@Service
@Slf4j
public class LoginMsgProcessor {

    @Resource
    private GroupUserService groupUserService;

    @Resource
    private CommonMsgGenerator commonMsgGenerator;

    @Resource
    private OnlineUserProcessor onlineUserProcessor;

    @Resource
    private MsgGenerator msgGenerator;

    public void registerLoginUser(BaseMsgProto.BaseMsg msg, NioSocketChannel channel){
        //1.保存到本地服务器
        long userId = msg.getFromUserId();
        ChatSessionMap.chatSessionMap.put(userId, channel);
        ChannelUserMap.channelUserMap.put(channel.id().asLongText(), userId);

        //2.保存到redis
        String host = channel.localAddress().getHostString();
        int port = channel.localAddress().getPort();
        String sessionUid = UUID.randomUUID().toString();
        GlobalChatSessionBo globalChatSessionBo = new GlobalChatSessionBo(host, port, sessionUid, userId);

        String key = ChannelCache.GLOBAL_CHAT_SESSION_PREFIX + userId;

        RedisUtil.put(key, globalChatSessionBo, 2, TimeUnit.HOURS);

        //3.保存每个群在线的用户
        List<GroupUser> groupUsers = groupUserService.getByUserId(userId);

        for (GroupUser groupUser : groupUsers) {
            Long groupId = groupUser.getGroupId();
            String cacheKey = OnlineCache.GROUP_ONLINE_USER + groupId;

            RedisUtil.sput(cacheKey, userId);
        }

        //4.发送ack消息
        BaseMsgProto.BaseMsg ackMsg = commonMsgGenerator.generateAckMsg(msg, TypeEnum.MsgType.ACK_MSG.getValue());
        channel.writeAndFlush(ackMsg);

        //5. 推送在线好友id
        List<Long> onlineUserList = onlineUserProcessor.getOnlineUser(userId);
        BaseMsgProto.BaseMsg onlineListMsg = msgGenerator.generateOnlineListMsg(userId, onlineUserList);
        SendMsgUtil.sendMsg4Ack(onlineListMsg, userId);

        //6. 将自己上线的消息推送给自己的好友
        onlineUserProcessor.userStatusChange(userId, TypeEnum.UserStatus.ONLINE.getStatus());

        log.info("收到用户id = 【{}】的登录信息", userId);
    }


    public void userLogout(BaseMsgProto.BaseMsg msg) {
        ChatSessionMap.chatSessionMap.remove(msg.getFromUserId());

        String key = "chatSession:" + msg.getFromUserId();
        RedisUtil.del(key);

        List<GroupUser> groupUserList = groupUserService.getByUserId(msg.getFromUserId());
        for (GroupUser groupUser : groupUserList) {
            String cacheKey = OnlineCache.GROUP_ONLINE_USER + groupUser.getGroupId();

            RedisUtil.sdel(cacheKey, groupUser.getUserId());
        }

        log.info("用户 【{}】 退出登录", msg.getFromUserId());
    }

    public void userChannelDisconnect(String channelId){
        // 1. 个人globalSession
        Long userId = ChannelUserMap.channelUserMap.remove(channelId);

        ChatSessionMap.chatSessionMap.remove(userId);

        RedisUtil.del(RedisCacheKey.CHAT_SESSION_KEY + userId);
        // 2. 个人群
        List<GroupUser> groupUserList = groupUserService.getByUserId(userId);
        for (GroupUser groupUser : groupUserList) {
            String cacheKey = OnlineCache.GROUP_ONLINE_USER + groupUser.getGroupId();

            RedisUtil.sdel(cacheKey, groupUser.getUserId());
        }

        onlineUserProcessor.userStatusChange(userId, TypeEnum.UserStatus.OFFLINE.getStatus());
    }
}
