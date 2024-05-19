package com.vanky.chat.common.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author vanky
 * @create 2024/5/18 19:04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckRedisTokenBo {

    private String accessTokenKey;

    private Map<String, RefreshTokenInfoBo> tokens;

}
