package com.vanky.chat.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vanky.chat.user.mapper.RelationMapper;
import com.vanky.chat.user.pojo.po.Relation;
import com.vanky.chat.user.service.RelationService;
import org.springframework.stereotype.Service;

/**
* @author 86180
* @description 针对表【relation】的数据库操作Service实现
* @createDate 2024-03-25 21:05:01
*/
@Service
public class RelationServiceImpl extends ServiceImpl<RelationMapper, Relation>
    implements RelationService {

    @Override
    public void addFriend(Long fromUserId, Long toUserId) {
        Relation relation = new Relation(null, Math.min(fromUserId, toUserId), Math.max(fromUserId, toUserId),
                null, null);

        this.save(relation);
    }

    @Override
    public Boolean areUsersFriends(Long fromUserId, Long toUserId) {
        LambdaQueryWrapper<Relation> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(Relation::getUserId1, Math.min(fromUserId, toUserId));
        wrapper.eq(Relation::getUserId2, Math.max(fromUserId, toUserId));

        return this.exists(wrapper);
    }

}




