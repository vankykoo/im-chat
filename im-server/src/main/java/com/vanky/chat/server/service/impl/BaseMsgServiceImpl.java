package com.vanky.chat.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vanky.chat.common.constant.TypeEnum;
import com.vanky.chat.common.feign.leafFeign.IdGeneratorFeignClient;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import com.vanky.chat.server.pojo.BaseMsg;
import com.vanky.chat.server.service.BaseMsgService;
import com.vanky.chat.server.mapper.BaseMsgMapper;
import com.vanky.chat.server.service.GroupMsgService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 86180
* @description 针对表【base_msg】的数据库操作Service实现
* @createDate 2024-03-30 20:44:18
*/
@Service
public class BaseMsgServiceImpl extends ServiceImpl<BaseMsgMapper, BaseMsg>
    implements BaseMsgService{

    @Resource
    private GroupMsgService groupMsgService;

    @Resource
    private BaseMsgMapper baseMsgMapper;

    @Resource
    private IdGeneratorFeignClient idGeneratorFeignClient;

    @Override
    public void saveMsg(BaseMsgProto.BaseMsg msg) {
        if (msg.getChatType() == TypeEnum.ChatType.GROUP_CHAT.getValue()
                && msg.getMsgType() != TypeEnum.MsgType.ACK_MSG.getValue()){

            groupMsgService.saveMsg(msg);
        }else {
            //私聊消息
            BaseMsg baseMsg = BaseMsg.Proto2BaseMsg(msg);
            baseMsg.setContent(msg.getContent().toByteArray());
            baseMsg.setStatus(TypeEnum.MsgStatus.SENT.getValue());

            baseMsg.setId(idGeneratorFeignClient.nextId().getData());

            this.save(baseMsg);
        }
    }

    @Override
    public BaseMsg getBaseMsgByUniqueId(Long uniqueId) {
        LambdaQueryWrapper<BaseMsg> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BaseMsg::getUniqueId, uniqueId);

        return this.getOne(wrapper);
    }

    @Override
    public void setMsgHasReadByUniqueId(Long uniqueId) {
        LambdaQueryWrapper<BaseMsg> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BaseMsg::getUniqueId, uniqueId);

        BaseMsg baseMsg = BaseMsg.builder()
                .status(TypeEnum.MsgStatus.HAS_READ.getValue())
                .build();

        this.update(baseMsg, wrapper);
    }

    @Override
    public void setMsgHasNotReadByUniqueId(Long uniqueId) {
        baseMsgMapper.setHasNotReadByUniqueId(uniqueId);
    }

    @Override
    public void setMsgHasNotReadByUniqueIds(List<Long> uniqueIds) {
        baseMsgMapper.setHasNotReadByUniqueIds(uniqueIds);
    }

    @Override
    public boolean isMsgExisted(Long uniqueId) {
        LambdaQueryWrapper<BaseMsg> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BaseMsg::getUniqueId, uniqueId);

        return this.exists(wrapper);
    }

    @Override
    public List<Long> setMsgHasReadByUser(long fromUserId, long toUserId) {
        List<Long> uniqueIds = baseMsgMapper.getBaseMsgByFromUserIdAndToUserId(fromUserId, toUserId);

        baseMsgMapper.setHasReadByUniqueId(uniqueIds);

        return uniqueIds;
    }

    /**
     * 用户拉取历史消息，每次100条
     * @param fromUserId
     * @param toUserId
     * @param oldestMsgId
     */
    public List<BaseMsg> getHistoryMsg(Long fromUserId, Long toUserId, Long oldestMsgId){
        List<BaseMsg> baseMsgList = baseMsgMapper.get100HistoryMsg(fromUserId, toUserId, oldestMsgId);

        if (baseMsgList == null || baseMsgList.size() == 0){
            // 提醒消息已经拉取完毕了
            log.error("已经到底了！");
        }

        return baseMsgList;
    }
}




