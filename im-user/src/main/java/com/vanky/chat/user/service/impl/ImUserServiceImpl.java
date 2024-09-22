package com.vanky.chat.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vanky.chat.common.bo.DHPrivateKeyBO;
import com.vanky.chat.common.bo.KeyPairBo;
import com.vanky.chat.common.cache.RedisCacheKey;
import com.vanky.chat.common.exception.MyException;
import com.vanky.chat.common.feign.leafFeign.IdGeneratorFeignClient;
import com.vanky.chat.common.utils.DHKeyUtil;
import com.vanky.chat.common.utils.RedisUtil;
import com.vanky.chat.user.mapper.PermissionMapper;
import com.vanky.chat.user.pojo.po.*;
import com.vanky.chat.user.pojo.to.UserTo;
import com.vanky.chat.user.service.ImUserService;
import com.vanky.chat.user.mapper.ImUserMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.interfaces.DHPrivateKey;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
* @author 86180
* @description 针对表【im_user】的数据库操作Service实现
* @createDate 2024-05-10 11:20:17
*/
@Service
@Slf4j
public class ImUserServiceImpl extends ServiceImpl<ImUserMapper, ImUser>
    implements ImUserService{

    @Resource
    private ImUserMapper imUserMapper;

    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private IdGeneratorFeignClient idGeneratorFeignClient;

    @Override
    public ImUser getUserByUserName(String username) {
        //先根据用户名查询到用户的基本信息和角色信息
        ImUser imUser = imUserMapper.selectByUsername(username);

        if (imUser == null){
            return null;
        }

        //获取用户角色id
        Set<Long> roleIds = new HashSet<>();
        for (Role role : imUser.getRoles()) {
            roleIds.add(role.getRoleId());
        }
        //通过角色id获取角色关联的权限
        List<Permission> permissions = permissionMapper.selectPermissionByRoleIds(roleIds);
        List<String> permissionStrings = imUser.getPermissions();
        for (Permission permission : permissions) {
            permissionStrings.add(permission.getPerms());
        }

        return imUser;
    }

    @Override
    public boolean register(UserTo userTo) {
        //1.判断用户是否已经存在
        LambdaQueryWrapper<ImUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ImUser::getUsername, userTo.getUsername());
        boolean exists = this.exists(wrapper);

        if (exists){
            //已存在
            throw new MyException.UserExistedException();
        }

        //2.不存在，保存到数据库
        //给密码加密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode(userTo.getPassword());

        //3.生成公钥私钥
        try {
            KeyPairBo keyPairBo = DHKeyUtil.generateKeyPair();
            String publicKey = new BigInteger(keyPairBo.getPublicKey()).toString();

            ImUser user = new ImUser(userTo.getUsername(), password);
            user.setPublicKey(publicKey);
            user.setUserId(idGeneratorFeignClient.nextId().getData());
            this.save(user);

            String privateKeyCache = RedisCacheKey.MY_PRIVATE_KEY + user.getUserId();
            DHPrivateKey privateKey = (DHPrivateKey) keyPairBo.getPrivateKey();
            DHPrivateKeyBO dhPrivateKeyBO = new DHPrivateKeyBO(privateKey.getX(), privateKey.getParams().getP(), privateKey.getParams().getG());
            RedisUtil.put(privateKeyCache, dhPrivateKeyBO);

            log.info("用户注册成功：{} ---> {}", user.getUserId(), user.getUsername());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    @Override
    public String getUserPublicKey(Long userId) {
        return imUserMapper.getUserPublicKey(userId);
    }

    @Override
    public ImUser getByUserId(Long userId) {
        ImUser imUser = imUserMapper.selectByUserId(userId);

        if (imUser == null){
            throw new MyException.DataNotExistedException("根据用户id查询用户失败。");
        }

        //获取用户角色id
        Set<Long> roleIds = new HashSet<>();
        for (Role role : imUser.getRoles()) {
            roleIds.add(role.getRoleId());
        }
        //通过角色id获取角色关联的权限
        List<Permission> permissions = permissionMapper.selectPermissionByRoleIds(roleIds);
        List<String> permissionStrings = imUser.getPermissions();
        for (Permission permission : permissions) {
            permissionStrings.add(permission.getPerms());
        }

        return imUser;
    }
}




