package com.vanky.chat.client.controller;

import com.vanky.chat.client.netty.NettyClient;
import com.vanky.chat.client.channel.UserChannelMap;
import com.vanky.chat.client.utils.MsgGenerator;
import com.vanky.chat.common.ApplicationContext;
import com.vanky.chat.common.exception.MyException;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import com.vanky.chat.common.response.Result;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    private MsgGenerator msgGenerator;

    @GetMapping ("/connect")
    @Operation(summary = "用户登录")
    public Result connect(@RequestParam("userId") Long userId){
        NioSocketChannel channel = nettyClient.connect();

        UserChannelMap.userChannel.put(userId, channel);
        UserChannelMap.channelUserMap.put(channel.id().asLongText(), userId);

        //登录前检查本地有没有私钥，如果没有就要生成，而且把公钥传给客户端

        //这里放一个登录的消息
        BaseMsgProto.BaseMsg msg = msgGenerator.generateLoginMsg(userId);
        channel.writeAndFlush(msg);

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

        }

        UserChannelMap.channelUserMap.remove(channel.id().asLongText());

        //这里放一个登出的消息
        BaseMsgProto.BaseMsg msg = msgGenerator.generateLogoutMsg(userId);
        channel.writeAndFlush(msg);

        channel.shutdown();

        return Result.success();
    }

}
