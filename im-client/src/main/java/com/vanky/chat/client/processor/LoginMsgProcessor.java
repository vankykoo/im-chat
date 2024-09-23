package com.vanky.chat.client.processor;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.vanky.chat.client.utils.MsgGenerator;
import com.vanky.chat.client.utils.SendAckMsgUtil;
import com.vanky.chat.common.bo.OfflineMsgDetailBo;
import com.vanky.chat.common.cache.OnlineCache;
import com.vanky.chat.common.constant.TypeEnum;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import com.vanky.chat.common.utils.CommonConverter;
import com.vanky.chat.common.utils.CommonMsgGenerator;
import com.vanky.chat.common.utils.RedisUtil;
import io.netty.channel.socket.nio.NioSocketChannel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author vanky
 * @create 2024/9/22 11:19
 */
@Component
@Slf4j
public class LoginMsgProcessor {

    @Resource
    private MsgGenerator msgGenerator;

    /**
     * 向服务端发送登录消息
     * @param userId
     * @param channel
     */
    public void sendLoginMsg(Long userId, NioSocketChannel channel){
        //这里放一个登录的消息
        BaseMsgProto.BaseMsg msg = msgGenerator.generateLoginMsg(userId);
        channel.writeAndFlush(msg);
    }

    /**
     * 向服务端发送退出登录消息
     * @param userId
     * @param channel
     */
    public void sendLogoutMsg(Long userId, NioSocketChannel channel){
        BaseMsgProto.BaseMsg msg = msgGenerator.generateLogoutMsg(userId);
        channel.writeAndFlush(msg);
    }

    public void receiveOnlineFriendsIdList(BaseMsgProto.BaseMsg msg){
        String jsonString = CommonConverter.byteString2String(msg.getContent());

        List<Long> onlineFriendsIdList = JSONObject.parseObject(jsonString, new TypeReference<>(){});

        log.info("在线的好友有{}个 ---- {}", onlineFriendsIdList.size(), onlineFriendsIdList);

        // 将好友修改为在线状态
        String friendStatusMapKey = OnlineCache.FRIEND_ID_LIST + msg.getFromUserId();
        for (Long userId : onlineFriendsIdList) {
            RedisUtil.hput(friendStatusMapKey, userId.toString(), TypeEnum.UserStatus.ONLINE.getStatus());
        }
    }


    public void receiveFriendStatusChangeMsg(BaseMsgProto.BaseMsg msg) {
        // 修改 redis 好友的状态
        int status = Integer.parseInt(CommonConverter.byteString2String(msg.getContent()));

        String friendStatusMapKey = OnlineCache.FRIEND_ID_LIST + msg.getToUserId();

        RedisUtil.hput(friendStatusMapKey, Long.toString(msg.getFromUserId()), status);

        String m = "";
        if (status == 1){
            m = "上线了";
        }else {
            m = "下线了";
        }

        log.info("收到用户在线状态改变消息 {} --- {}",msg.getFromUserId(), m);
    }
}
