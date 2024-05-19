package com.vanky.chat.auth.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author vanky
 * @create 2024/5/18 21:15
 */
@Component
@Data
public class SecurityOauthGiteeProperties {

    private String grantType = "authorization_code";

    @Value("${spring.security.oauth2.client.registration.gitee.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.gitee.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.gitee.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.provider.gitee.token-uri}")
    private String tokenUri;

    @Value("${spring.security.oauth2.client.provider.gitee.user-info-uri}")
    private String userInfoUri;

}
