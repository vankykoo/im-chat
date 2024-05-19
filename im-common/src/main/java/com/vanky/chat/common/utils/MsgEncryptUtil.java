package com.vanky.chat.common.utils;

import com.google.protobuf.ByteString;
import com.vanky.chat.common.constant.TypeEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

/**
 * @author vanky
 * @create 2024/4/23 20:45
 */
@Component
@Slf4j
public class MsgEncryptUtil {

    @Resource
    private ShareKeyUtil shareKeyUtil;

    public ByteString msgEncrypt(String content, Long otherIdOrGroupId, Long myIdOrUserId, int chatType){
        //获取共享密钥
        SecretKey secretKey = shareKeyUtil.getShareKey(otherIdOrGroupId, myIdOrUserId, chatType);

        //用共享密钥加密，消息加密
        try {
            byte[] encryptedContent = AESEncryptUtil.encryptAES(content, secretKey);
            return ByteString.copyFrom(encryptedContent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String msgDecrypt(ByteString content, Long otherIdOrGroupId, Long myIdOrUserId, int chatType){
        //发送消息之前，检查有没有对方的共享密钥
        SecretKey secretKey = shareKeyUtil.getShareKey(otherIdOrGroupId, myIdOrUserId, chatType);

        //用共享密钥加密，消息加密
        try {
            byte[] byteArray = content.toByteArray();
            return AESEncryptUtil.decryptAES(byteArray, secretKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
