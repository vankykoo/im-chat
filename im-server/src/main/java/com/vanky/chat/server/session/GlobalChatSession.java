package com.vanky.chat.server.session;

import lombok.Data;

/**
 * @author vanky
 * @create 2024/3/29 10:23
 * 全局ChatSession，存到redis中的格式
 */
@Data
public class GlobalChatSession {

    private String host;
    private int port;
    private String sessionUid;
    private Long userId;

    public GlobalChatSession(String host, int port, String sessionUid, Long userId) {
        this.host = host;
        this.port = port;
        this.sessionUid = sessionUid;
        this.userId = userId;
    }


}
