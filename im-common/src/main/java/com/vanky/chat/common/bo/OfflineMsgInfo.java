package com.vanky.chat.common.bo;

import com.alibaba.fastjson2.JSONObject;
import com.vanky.chat.common.protobuf.OfflineMsgProto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 一个用户/群组新消息的信息
 * @author vanky
 * @create 2024/4/9 19:51
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfflineMsgInfo {

    /**
     * 发送方id
     */
    private Long fromUserId;

    /**
     * 最早一条新消息的时间戳[这里修改了，变成递增序列号]
     */
    private Long gotoTimestamp;

    /**
     * 最后一条新消息的时间戳[这里修改了，变成递增序列号]
     */
    private Long lastTimestamp;

    /**
     * 消息条数
     */
    private int msgCount;

    /**
     * 该用户发送的最后一条消息的内容
     */
    private byte[] content;

    /**
     * 消息类型
     */
    private int msgType;

    public static String offlineMsg2Json(OfflineMsgInfo offlineMsgInfo){
        return JSONObject.toJSONString(offlineMsgInfo);
    }

    public static List<String>  offlineMsg2Json(List<OfflineMsgInfo> offlineMsgInfoList){
        return offlineMsgInfoList.stream().map(OfflineMsgInfo::offlineMsg2Json).collect(Collectors.toList());
    }

    public static OfflineMsgInfo json2OfflineMsg(String jsonString){
        return JSONObject.parseObject(jsonString, OfflineMsgInfo.class);
    }

    public static List<OfflineMsgInfo> json2OfflineMsg(List<String> jsonStrings){
        return jsonStrings.stream().map(OfflineMsgInfo::json2OfflineMsg).collect(Collectors.toList());
    }
}
