package com.vanky.chat.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vanky.chat.common.bo.DHPrivateKeyBO;
import com.vanky.chat.common.bo.KeyPairBo;
import com.vanky.chat.common.cache.RedisCacheKey;
import com.vanky.chat.common.feign.leafFeign.IdGeneratorFeignClient;
import com.vanky.chat.common.to.GroupTo;
import com.vanky.chat.common.utils.DHKeyUtil;
import com.vanky.chat.common.utils.RedisUtil;
import com.vanky.chat.server.pojo.Group;
import com.vanky.chat.server.service.GroupService;
import com.vanky.chat.server.mapper.GroupMapper;
import com.vanky.chat.server.service.GroupUserService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

/**
* @author 86180
* @description 针对表【group】的数据库操作Service实现
* @createDate 2024-03-30 22:02:47
*/
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group>
    implements GroupService{

    @Resource
    private IdGeneratorFeignClient idGeneratorFeignClient;

    @Resource
    private GroupMapper groupMapper;

    @Resource
    @Lazy
    private GroupUserService groupUserService;

    @Override
    public String getPublicKey(Long groupId) {
        return groupMapper.getPublicKey(groupId);
    }

    @Override
    public void createGroup(GroupTo groupTo) {
        //生成全局唯一id 作为群号
        Long groupUniqueId = idGeneratorFeignClient.nextId().getData();
        Group group = new Group();
        BeanUtils.copyProperties(groupTo, group);
        group.setUniqueId(groupUniqueId);

        //生成公钥私钥
        try {
            KeyPairBo keyPairBo = DHKeyUtil.generateKeyPair();

            //公钥保存到mysql
            byte[] publicKey = keyPairBo.getPublicKey();
            group.setPublicKey(new BigInteger(publicKey).toString());

            //创建群聊
            groupMapper.insertGroup(group);

            //私钥保存在服务端
            DHPrivateKeyBO dhPrivateKeyBO = DHPrivateKeyBO.privateKey2PrivateKeyBo(keyPairBo.getPrivateKey());
            String privateKeyRedisCacheKey = RedisCacheKey.GROUP_PRIVATE_KEY + group.getId();
            RedisUtil.put(privateKeyRedisCacheKey, dhPrivateKeyBO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //创建人加入群聊
        groupUserService.joinGroup(groupTo.getUserId(), group.getId());
    }

    @Override
    public void updateGroupUserNumber(Long groupId) {
        //获取群聊人数
        int number = groupMapper.getGroupUserNumber(groupId);

        groupMapper.updateUserNumber(groupId, number);
    }


}




