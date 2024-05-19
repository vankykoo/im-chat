package com.vanky.chat.common.utils;

import com.google.protobuf.ByteString;

/**
 * 消息类型转换器
 * @author vanky
 * @create 2024/4/23 20:19
 */
public class CommonConverter {

    public static ByteString string2ByteString(String content){
        return ByteString.copyFromUtf8(content);
    }

    public static String byteString2String(ByteString byteString){
        return byteString.toStringUtf8();
    }

}
