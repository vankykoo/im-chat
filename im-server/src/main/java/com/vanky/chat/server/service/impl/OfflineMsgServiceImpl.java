package com.vanky.chat.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vanky.chat.common.bo.OfflineMsgDetailBo;
import com.vanky.chat.common.constant.TypeEnum;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import com.vanky.chat.server.mapper.BaseMsgMapper;
import com.vanky.chat.server.pojo.BaseMsg;
import com.vanky.chat.server.pojo.OfflineMsg;
import com.vanky.chat.common.bo.OfflineMsgInfo;
import com.vanky.chat.server.service.OfflineMsgService;
import com.vanky.chat.server.mapper.OfflineMsgMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author 86180
* @description 针对表【offline_msg】的数据库操作Service实现
* @createDate 2024-03-30 16:42:36
*/
@Service
public class OfflineMsgServiceImpl extends ServiceImpl<OfflineMsgMapper, OfflineMsg>
    implements OfflineMsgService{

    @Resource
    private BaseMsgMapper baseMsgMapper;

    @Resource
    private OfflineMsgMapper offlineMsgMapper;

    /**
     * 保存消息为离线消息
     * @param msg
     */
    @Override
    public void saveOfflineMsg(BaseMsgProto.BaseMsg msg) {
        OfflineMsg offlineMsg = OfflineMsg.builder()
                .id(msg.getId())
                .uniqueId(msg.getUniqueId())
                .fromUserId(msg.getFromUserId())
                .toUserId(msg.getToUserId())
                .createTime(msg.getCreateTime())
                .build();

        this.save(offlineMsg);
    }

    @Override
    public void saveOfflineMsg(BaseMsg baseMsg) {
        BaseMsgProto.BaseMsg msg = BaseMsg.BaseMsg2Proto(baseMsg);
        saveOfflineMsg(msg);
    }

    @Override
    public List<OfflineMsgInfo> getOfflineMsgInfoByUserId(Long userId) {
        //1.先查询有哪些用户发送了新消息
        List<Long> userIds = offlineMsgMapper.getUserIdWhoSendNewMsg(userId);

        //2.根据用户id获取信息
        List<OfflineMsgInfo> offlineMsgInfoList = new ArrayList<>();
        for (Long id : userIds) {
            //消息条数
            int count = offlineMsgMapper.getOfflineMsgCountOfOne(id, userId);
            //最后一条消息
            OfflineMsg lastMsg = offlineMsgMapper.getLastOfflineMsgOfOne(id, userId);
            BaseMsg baseMsg = baseMsgMapper.selectByUniqueId(lastMsg.getUniqueId());
            //第一条消息
            OfflineMsg firstMsg = offlineMsgMapper.getFirstOfflineMsgOfOne(id, userId);

            OfflineMsgInfo offlineMsgInfo = OfflineMsgInfo.builder()
                    .fromUserId(id)
                    .gotoTimestamp(firstMsg.getCreateTime())
                    .lastTimestamp(lastMsg.getCreateTime())
                    .msgCount(count)
                    .content(baseMsg.getContent())
                    .msgType(TypeEnum.MsgType.OFFLINE_PRIVATE_MSG_INFO.getValue())
                    .build();

            offlineMsgInfoList.add(offlineMsgInfo);
        }

        //3.转为proto
        return offlineMsgInfoList;
    }

    @Override
    public void deleteOfflineMsgByUniqueId(List<Long> uniqueIds) {
        offlineMsgMapper.deleteByUniqueId(uniqueIds);
    }

    @Override
    public List<OfflineMsgDetailBo> get100OfflineMsg(Long fromUserId, Long toUserId) {
        List<OfflineMsgDetailBo> list = offlineMsgMapper.selectAllOfflineMsgByInfo(fromUserId, toUserId);

        if (list == null || list.size() == 0){
            return null;
        }

        return list;
    }
}




