package com.vanky.chat.common.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author vanky
 * @create 2024/3/30 16:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalChatSessionBo {

    private String host;
    private int port;
    private String sessionUid;
    private Long userId;

}
