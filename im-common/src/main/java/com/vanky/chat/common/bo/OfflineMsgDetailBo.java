package com.vanky.chat.common.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @author vanky
 * @create 2024/4/12 11:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OfflineMsgDetailBo {

    /**
     * 消息递增id
     */
    private Long id;

    /**
     * 消息唯一id
     */
    private String uniqueId;

    /**
     * 发送时间
     */
    private Date createTime;

    /**
     * 内容
     */
    private byte[] content;

}
