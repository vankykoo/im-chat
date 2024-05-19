package com.vanky.chat.auth.utils;

import com.vanky.chat.auth.pojo.bo.TokenGenerateBo;
import com.vanky.chat.common.constant.NumberConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * @author vanky
 * @create 2024/5/10 20:54
 */
@Component
public class ImJwtUtil {

    @Value("${im.token-secret}")
    private String tokenSecret;

    /**
     * 生成token
     */
    public TokenGenerateBo createToken(Map<String, Object> map, Long expireIn){
        Date expireDateTime = new Date(System.currentTimeMillis() + expireIn);

        String token = Jwts.builder()
                .setClaims(map)
                .setIssuedAt(new Date())
                .setExpiration(expireDateTime)
                .signWith(SignatureAlgorithm.HS256, tokenSecret)
                .compact();

        return new TokenGenerateBo(token, expireDateTime.getTime());
    }

    /**
     * 解析token
     */
    public Claims parseToken(String token){
        return Jwts.parser()
                .setSigningKey(tokenSecret)
                .parseClaimsJws(token)
                .getBody();
    }

}
