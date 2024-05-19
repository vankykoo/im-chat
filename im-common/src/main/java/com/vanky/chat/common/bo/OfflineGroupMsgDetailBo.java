package com.vanky.chat.common.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @author vanky
 * @create 2024/4/14 20:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OfflineGroupMsgDetailBo {

    /**
     * 消息递增id
     */
    private Long id;

    /**
     * 消息唯一id
     */
    private Long uniqueId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 发送时间
     */
    private Date createTime;

    /**
     * 内容
     */
    private String content;

}
