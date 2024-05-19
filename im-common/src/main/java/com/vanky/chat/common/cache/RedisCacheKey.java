package com.vanky.chat.common.cache;

/**
 * @author vanky
 * @create 2024/4/18 22:28
 */
public class RedisCacheKey {

    public static final String CHAT_SESSION_KEY = "chatSession:";

    //自己的私钥：【private_key:(用户id)】
    public static final String MY_PRIVATE_KEY = "private_key:";

    //群聊的私钥：【group_private_key:(群号)】
    public static final String GROUP_PRIVATE_KEY = "group_private_key:";

    //私聊共享密钥：【share_key:private:(自己的id):(对方的id)】
    public static final String PRIVATE_SHARE_KEY = "share_key:private:";

    //群聊共享密钥：【share_key:private:(自己的id):(群聊的id)】
    public static final String GROUP_SHARE_KEY = "share_key:group:";
}
