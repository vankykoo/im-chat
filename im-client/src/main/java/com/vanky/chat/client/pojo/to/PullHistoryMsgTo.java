package com.vanky.chat.client.pojo.to;

import lombok.Data;

/**
 * @author vanky
 * @create 2024/9/21 16:20
 */
@Data
public class PullHistoryMsgTo {

    private Long fromUserId;

    private Long toUserId;

}
