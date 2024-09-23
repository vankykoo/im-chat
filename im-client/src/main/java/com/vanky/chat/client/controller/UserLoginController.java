package com.vanky.chat.client.controller;

import com.vanky.chat.client.netty.NettyClient;
import com.vanky.chat.client.channel.UserChannelMap;
import com.vanky.chat.client.processor.LoginMsgProcessor;
import com.vanky.chat.common.ApplicationContext;
import com.vanky.chat.common.cache.OnlineCache;
import com.vanky.chat.common.constant.TypeEnum;
import com.vanky.chat.common.exception.MyException;
import com.vanky.chat.common.feign.userFeign.RelationFeignClient;
import com.vanky.chat.common.response.Result;
import com.vanky.chat.common.utils.RedisUtil;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author vanky
 * @create 2024/3/26 15:20
 */
@RestController
@RequestMapping("/client")
@Tag(name = "用户登录上线")
@Slf4j
public class UserLoginController {

    @Resource
    private NettyClient nettyClient;

    @Resource
    private LoginMsgProcessor loginMsgProcessor;

    @Resource
    private RelationFeignClient relationFeignClient;

    @GetMapping ("/connect")
    @Operation(summary = "用户登录")
    public Result connect(@RequestParam("userId") Long userId){
        //todo 登录前检查本地有没有私钥，如果没有就要生成，而且把公钥传给客户端

        // 获取好友列表
        Result<List<Long>> result = relationFeignClient.getFriendsByUserId(userId);
        if (!result.isSuccess()){
            throw new MyException.FeignProcessException();
        }
        List<Long> friendsIdList = result.getData();
        String friendsCacheKey = OnlineCache.FRIEND_ID_LIST + userId;

        // 好友列表先列为未登录
        for (Long id : friendsIdList) {
            RedisUtil.hput(friendsCacheKey, id.toString(), TypeEnum.UserStatus.OFFLINE.getStatus());
        }

        // 与默认服务端连接
        nettyClient.connect(null, null, userId);

        ApplicationContext.setUserId(userId);

        return Result.success();
    }

    @GetMapping ("/disconnect")
    @Operation(summary = "用户退出登录")
    public Result logout(@RequestParam("userId") Long userId, @RequestHeader("X-User-id") String context){

        Long headerUserId = Long.parseLong(context);
        if (Long.compareUnsigned(userId, headerUserId) != 0){
            throw new MyException.ImAuthenticationException("认证失败，别用别人的token访问接口！");
        }

        NioSocketChannel channel = UserChannelMap.userChannel.get(userId);

        if (channel == null){
            //已经断线
            throw new MyException.NotConnectedException();
        }

        UserChannelMap.channelUserMap.remove(channel.id().asLongText());

        //这里发送一个登出的消息
        loginMsgProcessor.sendLogoutMsg(userId, channel);

        channel.shutdown();

        return Result.success();
    }

}
