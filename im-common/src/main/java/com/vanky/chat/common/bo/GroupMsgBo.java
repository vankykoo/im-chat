package com.vanky.chat.common.bo;

import com.google.protobuf.ByteString;
import com.vanky.chat.common.to.GroupMsgTo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author vanky
 * @create 2024/4/23 21:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupMsgBo {

    private Long fromUserId;

    private Long groupId;

    private ByteString content;

    public static GroupMsgBo to2bo(GroupMsgTo groupMsgTo, ByteString content){
        return new GroupMsgBo(groupMsgTo.getUserId(), groupMsgTo.getGroupId(), content);
    }

}
