package com.vanky.chat.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author vanky
 * @create 2024/5/17 15:17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginSuccessVo {

    private Long userId;

    private String username;

    private String token;

}
