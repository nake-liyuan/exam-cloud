package com.liyuan.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.liyuan.typeHandler.UserStatus.ENABLE;

/**
 * @author liyuan
 * @date 2022/10/15
 * @project exam-cloud
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        //java字段
        this.setFieldValByName("status", ENABLE, metaObject);
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("modifiedTime", new Date(), metaObject);
        this.setFieldValByName("isDeleted", 0, metaObject);
        this.setFieldValByName("attendUserCount", 1, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("modifiedTime", new Date(), metaObject);
        this.setFieldValByName("loginTime", new Date(), metaObject);
    }
}
