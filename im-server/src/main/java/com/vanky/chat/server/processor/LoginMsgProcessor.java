package com.vanky.chat.server.processor;

import com.vanky.chat.common.bo.GlobalChatSessionBo;
import com.vanky.chat.common.bo.UserBo;
import com.vanky.chat.common.cache.ChannelCache;
import com.vanky.chat.common.cache.OnlineCache;
import com.vanky.chat.common.constant.TypeEnum;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import com.vanky.chat.common.utils.CommonMsgGenerator;
import com.vanky.chat.common.utils.RedisUtil;
import com.vanky.chat.server.pojo.GroupUser;
import com.vanky.chat.server.service.GroupUserService;
import com.vanky.chat.server.session.ChatSession;
import com.vanky.chat.server.session.ChatSessionMap;
import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * @author vanky
 * @create 2024/3/29 16:54
 */
@Service
public class LoginMsgProcessor {

    @Resource
    private GroupUserService groupUserService;

    @Resource
    private CommonMsgGenerator commonMsgGenerator;

    public void registerLoginUser(BaseMsgProto.BaseMsg msg, Channel channel){
        //1.保存到本地服务器
        UserBo userBo = new UserBo();
        ChatSession chatSession = new ChatSession(channel, userBo);
        long userId = msg.getFromUserId();
        ChatSessionMap.chatSessionMap.put(userId, chatSession);

        //2.保存到redis
        NioSocketChannel nioSocketChannel = (NioSocketChannel) channel;
        String host = nioSocketChannel.localAddress().getHostString();
        int port = nioSocketChannel.localAddress().getPort();
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
    }
}
