package com.vanky.chat.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenInfoVO {

    private String token;

    private String refreshToken;

    //多少秒后过期
    private Long expiresIn;

}