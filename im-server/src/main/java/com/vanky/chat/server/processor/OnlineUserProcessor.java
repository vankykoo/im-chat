package com.vanky.chat.server.processor;

import com.vanky.chat.common.cache.RedisCacheKey;
import com.vanky.chat.common.exception.MyException;
import com.vanky.chat.common.feign.userFeign.RelationFeignClient;
import com.vanky.chat.common.response.Result;
import com.vanky.chat.common.utils.RedisUtil;
import com.vanky.chat.server.pojo.BaseMsg;
import com.vanky.chat.server.push.PushProxy;
import com.vanky.chat.server.session.ChannelUserMap;
import com.vanky.chat.server.utils.MsgGenerator;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 维护用户在线状态
 * @author vanky
 * @create 2024/9/22 16:30
 */
@Component
@Slf4j
public class OnlineUserProcessor {

    @Resource
    private RelationFeignClient relationFeignClient;

    @Resource
    private MsgGenerator msgGenerator;

    @Resource
    private PushProxy pushProxy;

    // 1.获取用户在线的好友
    public List<Long> getOnlineUser(Long userId){
        //1. 先到mysql 查该用户的好友
        Result<List<Long>> result = relationFeignClient.getFriendsByUserId(userId);

        if (!result.isSuccess()){
            throw new MyException.FeignProcessException();
        }

        List<Long> onlineUserIds = new ArrayList<>();

        //2. 根据好友列表，到 redis 查哪些好友在线
        List<Long> friendsId = result.getData();
        for (Long id : friendsId) {
            boolean hasExisted = RedisUtil.hasExisted(RedisCacheKey.CHAT_SESSION_KEY + id);

            if (hasExisted){
                onlineUserIds.add(id);
            }
        }

        return onlineUserIds;
    }

    /**
     * 用户在线状态改变，需要推送给好友
     * @param userId
     * @param status
     */
    public void userStatusChange(Long userId, int status){
        // 获取在线的好友
        List<Long> onlineUser = getOnlineUser(userId);

        // 推送消息
        for (Long friendId : onlineUser) {
            BaseMsg baseMsg = msgGenerator.generateUserStatusChangeMsg(userId, friendId, status);

            pushProxy.pushMsg(baseMsg, "userStatusChangeMsg", "userStatusChangeKey");
        }
    }

    /**
     * 用户状态改变（针对离线）
     * @param channelId
     * @param status
     */
    public void userStatusChange(String channelId, int status){
        Long userId = ChannelUserMap.channelUserMap.get(channelId);

        userStatusChange(userId, status);
    }

}
