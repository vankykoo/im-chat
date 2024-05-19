package com.vanky.chat.common.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author vanky
 * @create 2024/3/25 21:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTo {

    private String username;

    private String password;

}
