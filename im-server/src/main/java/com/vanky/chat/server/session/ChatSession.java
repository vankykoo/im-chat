package com.vanky.chat.server.session;

import com.vanky.chat.common.bo.UserBo;
import io.netty.channel.Channel;
import lombok.Data;

import java.util.UUID;

/**
 * @author vanky
 * @create 2024/3/29 10:15
 *
 */
@Data
public class ChatSession {

    private Channel channel;

    private UserBo userBo;

    private String sessionUid = UUID.randomUUID().toString();

    public ChatSession(Channel channel, UserBo userBo) {
        this.channel = channel;
        this.userBo = userBo;
    }
}
