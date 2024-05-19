package com.vanky.chat.auth.pojo.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author vanky
 * @create 2024/5/16 16:07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenGenerateBo {

    private String token;

    private Long expireIn;

}
