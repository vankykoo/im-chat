package com.vanky.chat.common;

/**
 * @author vanky
 * @create 2024/3/31 21:24
 */
public class ApplicationContext {

    public static final ThreadLocal<Long> user_id = new ThreadLocal<>();

    public static void setUserId(Long userId){
        user_id.set(userId);
    }

    public static Long getUserId(){
        return user_id.get();
    }

}
