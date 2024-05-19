package com.vanky.chat.common.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.security.PrivateKey;

/**
 * @author vanky
 * @create 2024/4/22 22:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyPairBo {

    /**
     * 公钥
     */
    private byte[] publicKey;

    /**
     * 私钥
     */
    private PrivateKey privateKey;

}
