package com.vanky.chat.common.utils;

import com.vanky.chat.common.bo.DHPrivateKeyBO;
import com.vanky.chat.common.bo.KeyPairBo;

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.DHPrivateKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;

/**
 * 应用DH算法
 * ①获取共享密钥
 * ②获取公钥私钥
 * @author vanky
 * @create 2024/4/22 22:19
 */
public class DHKeyUtil {

    /**
     * 生成密钥对：公钥和私钥
     * @return
     * @throws Exception
     */
    public static KeyPairBo generateKeyPair() throws Exception {
        // 创建DH算法的“秘钥对”生成器
        KeyPairGenerator kpGen = KeyPairGenerator.getInstance("DH");
        kpGen.initialize(512);
        //生成一个"密钥对"
        KeyPair keyPair = kpGen.generateKeyPair();
        //密钥转为String格式
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        return new KeyPairBo(publicKey.getEncoded(), privateKey);
    }

    /**
     * 将 String 格式的密钥 转化为byte数组格式
     * @param rowKey
     * @return
     */
    public static String RowKey2String(byte[] rowKey){
        return new BigInteger(rowKey).toString();
    }

    /**
     * 将 String 格式的密钥 转化为byte数组格式
     * @param rowKey
     * @return
     */
    public static byte[] String2RowKeyTo(String rowKey){
        byte[] publicOfOtherByteArray = new BigInteger(rowKey).toByteArray();

        // 从byte[]恢复PublicKey:
        return publicOfOtherByteArray;
    }

    /**
     * 按照 "对方的公钥" => 生成"共享密钥"
     * @param publicOfOther
     * @param privateKeySelf
     * @return
     */
    public static SecretKey generateShareSecretKey(String publicOfOther, DHPrivateKeyBO privateKeySelf) {
        try {
            byte[] publicOfOtherByteArray = String2RowKeyTo(publicOfOther);

            // 从byte[]恢复PublicKey:
            X509EncodedKeySpec publicOfOtherSpec = new X509EncodedKeySpec(publicOfOtherByteArray);
            DHPrivateKeySpec dhPrivateKeySpec = new DHPrivateKeySpec(privateKeySelf.getX(), privateKeySelf.getP(), privateKeySelf.getG());

            // 根据DH算法获取KeyFactory
            KeyFactory kf = KeyFactory.getInstance("DH");
            // 通过KeyFactory创建公钥
            PublicKey receivedPublicKey = kf.generatePublic(publicOfOtherSpec);
            PrivateKey privateKey = kf.generatePrivate(dhPrivateKeySpec);

            // 创建秘钥协议对象(用于秘钥协商)
            KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
            keyAgreement.init(privateKey); // 初始化"自己的PrivateKey"
            keyAgreement.doPhase(receivedPublicKey, true); // 根据"对方的PublicKey"

            //转为AES密钥
            SecretKey secretKey = convertByteArrayToAESKey(keyAgreement.generateSecret());

            // 生成SecretKey本地密钥(共享公钥)
            return secretKey;
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    // 将byte[]数组转换为SecretKey对象
    public static SecretKey convertByteArrayToAESKey(byte[] keyBytes) {
        return new SecretKeySpec(keyBytes,0, 32, "AES");
    }

}
