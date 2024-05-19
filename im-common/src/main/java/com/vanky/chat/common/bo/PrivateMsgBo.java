package com.vanky.chat.common.bo;

import com.google.protobuf.ByteString;
import com.vanky.chat.common.to.PrivateMsgTo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author vanky
 * @create 2024/4/23 20:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrivateMsgBo {

    private Long fromUserId;

    private Long toUserId;

    private ByteString content;

    public static PrivateMsgBo to2bo(PrivateMsgTo privateMsgTo, ByteString content){
        return new PrivateMsgBo(privateMsgTo.getFromUserId(), privateMsgTo.getToUserId(), content);
    }

}
