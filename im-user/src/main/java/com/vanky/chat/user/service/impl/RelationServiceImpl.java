package com.vanky.chat.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vanky.chat.user.mapper.RelationMapper;
import com.vanky.chat.user.pojo.po.Relation;
import com.vanky.chat.user.service.RelationService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
* @author 86180
* @description 针对表【relation】的数据库操作Service实现
* @createDate 2024-03-25 21:05:01
*/
@Service
public class RelationServiceImpl extends ServiceImpl<RelationMapper, Relation>
    implements RelationService {

    @Resource
    private RelationMapper relationMapper;

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

    @Override
    public List<Long> getFriendsByUserId(Long userId) {
        // 获取好友列表
        List<Relation> relations = relationMapper.selectFriendsByUserId(userId);

        List<Long> friendsList = new ArrayList<>();

        // 筛选出对方id
        for (Relation relation : relations) {
            if (Objects.equals(relation.getUserId1(), userId)){
                friendsList.add(relation.getUserId2());
            }else{
                friendsList.add(relation.getUserId1());
            }
        }

        return friendsList;
    }

}




