package com.vanky.chat.common.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenInfoBo {
    //内容
    private String refreshToken;
    //过期时间
    private Long expireIn;
}