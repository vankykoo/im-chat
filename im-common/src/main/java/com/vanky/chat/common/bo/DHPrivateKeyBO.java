package com.vanky.chat.common.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.crypto.interfaces.DHPrivateKey;
import java.math.BigInteger;
import java.security.PrivateKey;

/**
 * @author vanky
 * @create 2024/4/24 22:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DHPrivateKeyBO {

    private BigInteger x;

    private BigInteger p;

    private BigInteger g;

    public static DHPrivateKeyBO privateKey2PrivateKeyBo(PrivateKey privateKey){
        if (!(privateKey instanceof DHPrivateKey)){
            throw new RuntimeException("此privateKey 不是 DHPrivateKey， 无法转换！");
        }

        DHPrivateKey dhPrivateKey = (DHPrivateKey) privateKey;

        return new DHPrivateKeyBO(dhPrivateKey.getX(),
                dhPrivateKey.getParams().getP(),
                dhPrivateKey.getParams().getG());
    }
}