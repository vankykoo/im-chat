package com.vanky.chat.auth.pojo.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author vanky
 * @create 2024/5/16 16:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenInfo {
    //内容
    private String refreshToken;
    //过期时间
    private Long expireIn;
}
