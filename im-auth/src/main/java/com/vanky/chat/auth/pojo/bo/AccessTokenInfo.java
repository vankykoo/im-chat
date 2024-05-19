package com.vanky.chat.auth.pojo.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author vanky
 * @create 2024/5/15 21:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenInfo {

    private Long userId;

    private List<String> permissions;

    private String token;

    private Long expireIn;

    private boolean isUpdated = false;

}
