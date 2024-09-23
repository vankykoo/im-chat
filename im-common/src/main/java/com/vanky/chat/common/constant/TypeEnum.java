package com.vanky.chat.common.constant;

import org.bouncycastle.cms.PasswordRecipientId;

/**
 * @author vanky
 * @create 2024/3/29 10:56
 */
public class TypeEnum {

    public enum MsgType{
        LOGIN_MSG(0), //登录消息
        CHAT_MSG(1), //聊天消息
        ACK_MSG(2), //在线ack消息
        PING_MSG(3), //ping
        PONG_MSG(4), //pong
        FORWARD_MSG(5), //转发消息
        PULL_NOTICE_MSG(6), //拉取通知消息
        OFFLINE_PRIVATE_MSG_INFO(7), //私信离线消息信息
        OFFLINE_GROUP_MSG_INFO(8), //群聊离线消息信息
        OFFLINE_PRIVATE_MSG_DETAIL(9), //私信离线消息
        OFFLINE_GROUP_MSG_DETAIL(10), //群聊离线消息
        OFFLINE_ACK_MSG(11),//离线消息ack
        SIMPLE_ACK_MSG(12),//普通ack
        HAS_READ_MSG(13), //表示客户端进入聊天框，阅读了所有已抵达的消息
        LOGOUT_MSG(14),//退出登录消息
        HISTORY_MSG(15),// 历史消息
        ONLINE_FRIEND_LIST_MSG(16),// 在线好友列表消息
        USER_STATUS_CHANGE(17), // 用户在线状态改变
        ;

        private int value;

        MsgType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum ChatType{
        PRIVATE_CHAT(0), //私聊
        GROUP_CHAT(1), //群聊
        OTHER_CHAT(2)
        ;

        private int value;

        ChatType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum MsgStatus{
        HAS_NOT_READ(0), //未读
        HAS_READ(1), //已读
        SENT(2), //已送达服务端
        NOT_SENT(3), //未送达
        ;

        private int value;

        MsgStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum UserStatus{
        OFFLINE(0),// 不在线
        ONLINE(1), // 在线
        ;

        private int status;

        UserStatus(int status){
            this.status = status;
        }

        public int getStatus(){
            return status;
        }
    }

}
