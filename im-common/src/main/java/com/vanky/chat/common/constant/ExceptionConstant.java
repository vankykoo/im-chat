package com.vanky.chat.common.constant;

/**
 * @author vanky
 * @create 2024/3/25 21:26
 */
public class ExceptionConstant {

    public static final String USER_EXISTED = "用户已存在！";

    public static final String DATA_NOT_EXISTED = "数据不存在！";

    public static final String PASSWORD_NOT_MATCHED = "密码错误！";

    public static final String MESSAGE_RETRY_MAX = "消息重试多次发送失败";

    public static final String USERNAME_PASSWORD_ERROR = "用户名或密码错误";

    public static final String USER_NOT_LOGIN = "请登录后重试！";

    public static final String FEIGN_PROCESS_EXCEPTION = "feign 远程调用异常！";

    public static final String TOKEN_EXPIRE_EXCEPTION = "用户凭证过期，请重新登录！";

    public static final String AUTHENTICATION_EXCEPTION = "认证失败，请登录！";

    public static final String AUTHORIZATION_EXCEPTION = "无访问权限！";

    public static final String NOT_CONNECTED_EXCEPTION = "未连接异常";

}
