package com.vanky.chat.common.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenInfoBO {

    //token内的用户信息
    private Long userId;

    //权限
    private List<String> permissions;

    //token值
    private String token;

    //是否更新
    private boolean isUpdated = false;

}