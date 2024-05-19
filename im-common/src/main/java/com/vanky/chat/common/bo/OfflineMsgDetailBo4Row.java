package com.vanky.chat.common.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @author vanky
 * @create 2024/4/28 10:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OfflineMsgDetailBo4Row {

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
    private String content;

}
