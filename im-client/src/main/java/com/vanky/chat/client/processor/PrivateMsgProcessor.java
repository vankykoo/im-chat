package com.vanky.chat.client.processor;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.google.protobuf.ByteString;
import com.vanky.chat.client.utils.SendAckMsgUtil;
import com.vanky.chat.client.utils.SendMsgUtil;
import com.vanky.chat.common.bo.OfflineMsgDetailBo;
import com.vanky.chat.common.bo.OfflineMsgDetailBo4Row;
import com.vanky.chat.common.constant.TypeEnum;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import com.vanky.chat.common.utils.AESEncryptUtil;
import com.vanky.chat.common.utils.CommonMsgGenerator;
import com.vanky.chat.common.utils.MsgEncryptUtil;
import com.vanky.chat.common.utils.ShareKeyUtil;
import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.List;

/**
 * @author vanky
 * @create 2024/4/25 17:07
 */
@Component
@Slf4j
public class PrivateMsgProcessor {

    @Resource
    private ShareKeyUtil shareKeyUtil;

    @Resource
    private MsgEncryptUtil msgEncryptUtil;

    @Resource
    private CommonMsgGenerator commonMsgGenerator;

    public String receivePrivateMsg(BaseMsgProto.BaseMsg msg, Channel channel){
        //获取共享密钥
        SecretKey shareKey = shareKeyUtil.getShareKey(msg.getFromUserId(), msg.getToUserId(), TypeEnum.ChatType.PRIVATE_CHAT.getValue());

        //解密
        try {
            String content = AESEncryptUtil.decryptAES(msg.getContent().toByteArray(), shareKey);
            log.info("消息解码成功：{}", content);

            log.info("收到服务端发来私信消息：{}", content);
            BaseMsgProto.BaseMsg baseMsg = commonMsgGenerator.generateAckMsg(msg, TypeEnum.MsgType.ACK_MSG.getValue());
            //channel.writeAndFlush(baseMsg);
            SendMsgUtil.sendMsg((NioSocketChannel) channel, baseMsg);

            return content;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<OfflineMsgDetailBo4Row> receiveOfflinePrivateMsgDetail(BaseMsgProto.BaseMsg msg, Channel channel){
        //全量离线消息
        //全量消息转为json时进行了加密，这里需要解密
        String jsonString = msgEncryptUtil.msgDecrypt(msg.getContent(), msg.getFromUserId(), msg.getToUserId(), msg.getChatType());
        //获取到了list集合，但是消息内容还需要进一步解密
        List<OfflineMsgDetailBo> list = JSONObject.parseObject(jsonString, new TypeReference<>(){});
        List<OfflineMsgDetailBo4Row> rowList = new ArrayList<>();

        for (OfflineMsgDetailBo offlineMsgDetailBo : list) {
            String rowContent = msgEncryptUtil
                    .msgDecrypt(ByteString.copyFrom(offlineMsgDetailBo.getContent()),
                            msg.getFromUserId(), msg.getToUserId(), msg.getChatType());

            OfflineMsgDetailBo4Row offlineMsgDetailBo4Row = new OfflineMsgDetailBo4Row();
            BeanUtils.copyProperties(offlineMsgDetailBo, offlineMsgDetailBo4Row);
            offlineMsgDetailBo4Row.setContent(rowContent);

            rowList.add(offlineMsgDetailBo4Row);
        }

        log.info("收到全量离线消息：{}",rowList);

        //发送ack消息
        SendAckMsgUtil.sendAckMsg(msg, (NioSocketChannel) channel, TypeEnum.MsgType.OFFLINE_ACK_MSG.getValue());

        return rowList;
    }

}
