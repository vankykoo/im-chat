package com.vanky.chat.client.processor;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.google.protobuf.ByteString;
import com.vanky.chat.client.utils.SendAckMsgUtil;
import com.vanky.chat.client.utils.SendMsgUtil;
import com.vanky.chat.common.bo.BaseMsgBo;
import com.vanky.chat.common.bo.OfflineMsgDetailBo;
import com.vanky.chat.common.bo.OfflineMsgDetailBo4Row;
import com.vanky.chat.common.cache.RedisCacheKey;
import com.vanky.chat.common.cache.RedisSimpleCacheName;
import com.vanky.chat.common.constant.TypeEnum;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import com.vanky.chat.common.utils.*;
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

    public void historyMsg(BaseMsgProto.BaseMsg msg) {
        // 取出消息内容，封装，解密
        String jsonString = CommonConverter.byteString2String(msg.getContent());
        List<BaseMsgBo> list = JSONObject.parseObject(jsonString, new TypeReference<>(){});

        if (list == null || list.size() == 0){
            log.warn("已经到底了");
            return;
        }

        Long oldestMsgId = list.get(0).getId();

        for (BaseMsgBo baseMsgBo : list) {
            oldestMsgId = Math.min(oldestMsgId, baseMsgBo.getId());

            String rowMsg = msgEncryptUtil
                    .msgDecrypt(ByteString.copyFrom(baseMsgBo.getContent()),
                        msg.getFromUserId(),
                        msg.getToUserId(),
                        TypeEnum.ChatType.PRIVATE_CHAT.getValue());

            log.info("历史消息【{}】---》{}", baseMsgBo.getId(), rowMsg);
        }

        // 更新 redis 的 oldestMsgId
        String oldestMsgIdKey = RedisCacheKey.PRIVATE_OLDEST_MSG_ID_KEY + msg.getFromUserId() + RedisSimpleCacheName.UNION_KEY + msg.getToUserId();

        RedisUtil.put(oldestMsgIdKey, oldestMsgId);

        log.info("用户的oldestMsgId 更新：【{}】 --- {}", oldestMsgIdKey, oldestMsgId);
    }
}
