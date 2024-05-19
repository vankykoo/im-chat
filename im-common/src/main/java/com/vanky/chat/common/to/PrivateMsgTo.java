package com.vanky.chat.common.to;

import lombok.Data;

/**
 * @author vanky
 * @create 2024/3/30 11:12
 */
@Data
public class PrivateMsgTo {

    /**
     * 发送方id
     */
    private Long fromUserId;

    /**
     * 接收方id
     */
    private Long toUserId;

    /**
     * 消息主体内容
     */
    private String content;

}
