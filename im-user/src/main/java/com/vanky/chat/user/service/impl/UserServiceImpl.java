package com.vanky.chat.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vanky.chat.common.bo.DHPrivateKeyBO;
import com.vanky.chat.common.bo.KeyPairBo;
import com.vanky.chat.common.cache.RedisCacheKey;
import com.vanky.chat.common.exception.MyException;
import com.vanky.chat.common.utils.DHKeyUtil;
import com.vanky.chat.common.utils.RedisUtil;
import com.vanky.chat.user.mapper.UserMapper;
import com.vanky.chat.user.pojo.po.User;
import com.vanky.chat.user.pojo.to.UserTo;
import com.vanky.chat.user.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.interfaces.DHPrivateKey;
import java.math.BigInteger;

/**
* @author 86180
* @description 针对表【user】的数据库操作Service实现
* @createDate 2024-03-25 21:05:01
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public String getUserPublicKey(Long userId) {
        return userMapper.getUserPublicKey(userId);
    }
}




