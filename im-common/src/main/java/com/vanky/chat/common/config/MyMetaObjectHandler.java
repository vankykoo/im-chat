package com.vanky.chat.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

// 自定义处理器实现MetaObjectHandler接口
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("autoFill insert...");

        Class<?> createTime = metaObject.getObjectWrapper().getSetterType("createTime");
        if ("java.util.Date".equals(createTime.getName())){
            this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
        }else {
            this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("autoFill update...");

        Class<?> updateTime = metaObject.getObjectWrapper().getSetterType("updateTime");

        if ("java.util.Date".equals(updateTime.getName())){
            this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
        }else {
            this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        }
    }
}