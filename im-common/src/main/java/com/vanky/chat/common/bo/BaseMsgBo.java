package com.vanky.chat.common.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author vanky
 * @create 2024/3/30 11:09
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BaseMsgBo {

    private Long id;
    /**
     * 消息唯一id
     */
    private Long uniqueId;

    /**
     * 发送方id
     */
    private Long fromUserId;

    /**
     * 接收方id
     */
    private Long toUserId;

    /**
     * 创建时间戳
     */
    private Date createTime;

    /**
     * 聊天类型
     */
    private Integer chatType;

    /**
     * 消息主体内容
     */
    private byte[] content;

    /**
     * 消息类型
     */
    private Integer msgType;

    /**
     * 是否已读
     */
    private Integer status;

}
