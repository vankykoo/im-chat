package com.vanky.chat.common.utils;

import com.vanky.chat.common.bo.DHPrivateKeyBO;
import com.vanky.chat.common.cache.RedisCacheKey;
import com.vanky.chat.common.constant.TypeEnum;
import com.vanky.chat.common.feign.groupfeign.GroupFeignClient;
import com.vanky.chat.common.feign.userFeign.ImUserFeignClient;
import com.vanky.chat.common.response.Result;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;

/**
 * @author vanky
 * @create 2024/4/23 17:01
 */
@Component
public class ShareKeyUtil {

    @Resource
    @Lazy
    private ImUserFeignClient imUserFeignClient;

    @Resource
    @Lazy
    private GroupFeignClient groupFeignClient;

    public SecretKey getShareKey(Long otherIdOrGroupId, Long myIdOrUserId, int chatType){
        //获取共享密钥
        String shareKeyRedisKey = null;
        String privateRedisKey = null;

        if (chatType == TypeEnum.ChatType.PRIVATE_CHAT.getValue()){
            shareKeyRedisKey = RedisCacheKey.PRIVATE_SHARE_KEY + myIdOrUserId + ":" + otherIdOrGroupId;
            privateRedisKey = RedisCacheKey.MY_PRIVATE_KEY + myIdOrUserId;
        }else {
            shareKeyRedisKey = RedisCacheKey.GROUP_SHARE_KEY + myIdOrUserId + ":" + otherIdOrGroupId;
            privateRedisKey = RedisCacheKey.GROUP_PRIVATE_KEY + otherIdOrGroupId;
        }

        //发送消息之前，检查有没有对方的共享密钥
        SecretKey secretKey = null;
        String shareKey = StringRedisUtil.get(shareKeyRedisKey);

        if (!StringUtils.hasText(shareKey)){
            //没有密钥，获取对方的公钥
            Result<String> result = null;
            if (chatType == TypeEnum.ChatType.PRIVATE_CHAT.getValue()){
                result = imUserFeignClient.getUserPublicKey(otherIdOrGroupId);
            }else {
                result = groupFeignClient.getPublicKey(otherIdOrGroupId);
            }

            if (result == null || !StringUtils.hasText(result.getData())){
                throw new NullPointerException("查询内容为空！");
            }
            String publicKey = result.getData();
            //获取自己的私钥
            DHPrivateKeyBO privateKeyBO = RedisUtil.get(privateRedisKey, DHPrivateKeyBO.class);
            secretKey = DHKeyUtil.generateShareSecretKey(publicKey, privateKeyBO);

            //存到redis中
            StringRedisUtil.put(shareKeyRedisKey, DHKeyUtil.RowKey2String(secretKey.getEncoded()));
        }else {
            secretKey = DHKeyUtil.convertByteArrayToAESKey(DHKeyUtil.String2RowKeyTo(shareKey));
        }

        return secretKey;
    }

}
