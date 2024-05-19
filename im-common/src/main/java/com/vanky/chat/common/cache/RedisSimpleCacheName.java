package com.vanky.chat.common.cache;

/**
 * @author vanky
 * @create 2024/5/5 22:11
 */
public class RedisSimpleCacheName {

    public static final String UNION_KEY = ":";

    public static final String PATH_REQUEST_PERMISSION = "permission:path:";

    /**
     * auth 相关
     */
    public static final String IM_AUTH_PREFIX = "im_auth:";

    /**
     * accessToken存储
     */
    public static final String ACCESS_TOKEN = IM_AUTH_PREFIX + "user_id:";

    /**
     * accessToken 与 refreshToken 映射
     */
    public static final String REFRESH_TOKEN = IM_AUTH_PREFIX + "refresh_token:";

}
