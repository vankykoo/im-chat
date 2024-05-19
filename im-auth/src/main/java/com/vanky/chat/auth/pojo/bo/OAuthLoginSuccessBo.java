package com.vanky.chat.auth.pojo.bo;

import lombok.Data;

/**
 * @author vanky
 * @create 2024/5/18 20:51
 */
@Data
public class OAuthLoginSuccessBo {

    private String accessToken;

    private String tokenType;

    private Long expiresIn;

    private String refreshToken;

    private String scope;

    private Long createdAt;

}
