package com.vanky.chat.common.exception;

import com.vanky.chat.common.constant.ExceptionConstant;

/**
 * @author vanky
 * @create 2024/3/25 21:24
 */
public class MyException {

    /**
     * 用户已存在异常
     */
    public static class UserExistedException extends BaseException{
        public UserExistedException(){
            super(ExceptionConstant.USER_EXISTED);
        }

        public UserExistedException(String msg){
            super(msg);
        }
    }

    /**
     * feign 远程调用异常
     */
    public static class FeignProcessException extends BaseException{
        public FeignProcessException(){
            super(ExceptionConstant.FEIGN_PROCESS_EXCEPTION);
        }

        public FeignProcessException(String msg){
            super(msg);
        }
    }

    /**
     * token 过期异常
     */
    public static class TokenExpiredException extends BaseException{
        public TokenExpiredException(){
            super(ExceptionConstant.TOKEN_EXPIRE_EXCEPTION);
        }

        public TokenExpiredException(String msg){
            super(msg);
        }
    }

    /**
     * 认证异常
     */
    public static class ImAuthenticationException extends BaseException{
        public ImAuthenticationException(){
            super(ExceptionConstant.AUTHENTICATION_EXCEPTION);
        }

        public ImAuthenticationException(String msg){
            super(msg);
        }
    }

    /**
     * 授权异常
     */
    public static class ImAuthorizationException extends BaseException{
        public ImAuthorizationException(){
            super(ExceptionConstant.AUTHORIZATION_EXCEPTION);
        }

        public ImAuthorizationException(String msg){
            super(msg);
        }
    }

    /**
     * 数据库不存在该数据异常
     */
    public static class DataNotExistedException extends BaseException{
        public DataNotExistedException(){
            super(ExceptionConstant.DATA_NOT_EXISTED);
        }

        public DataNotExistedException(String msg){
            super(msg);
        }
    }

    /**
     * 密码错误异常
     */
    public static class PasswordNotMatchedException extends BaseException{
        public PasswordNotMatchedException(){
            super(ExceptionConstant.PASSWORD_NOT_MATCHED);
        }

        public PasswordNotMatchedException(String msg){
            super(msg);
        }
    }

    /**
     * 消息发送失败异常
     */
    public static class MessageSendException extends BaseException{
        public MessageSendException(){
            super(ExceptionConstant.MESSAGE_RETRY_MAX);
        }

        public MessageSendException(String msg){
            super(msg);
        }
    }

    /**
     * 用户账号或密码错误异常
     */
    public static class PasswordErrorException extends BaseException{
        public PasswordErrorException(){
            super(ExceptionConstant.USERNAME_PASSWORD_ERROR);
        }

        public PasswordErrorException(String msg){
            super(msg);
        }
    }

    /**
     * 用户账号或密码错误异常
     */
    public static class UserNotLoginException extends BaseException{
        public UserNotLoginException(){
            super(ExceptionConstant.USER_NOT_LOGIN);
        }

        public UserNotLoginException(String msg){
            super(msg);
        }
    }

    /**
     * 用户未连接到服务端
     */
    public static class NotConnectedException extends BaseException{
        public NotConnectedException(){
            super(ExceptionConstant.NOT_CONNECTED_EXCEPTION);
        }

        public NotConnectedException(String msg){
            super(msg);
        }
    }

}
