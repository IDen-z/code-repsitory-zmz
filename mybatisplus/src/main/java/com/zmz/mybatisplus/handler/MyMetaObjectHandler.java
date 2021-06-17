package com.zmz.mybatisplus.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


/**
* @Description: 这是Mybaits-Plus的自动赋值的操作，手写一个类继承MetaObjectHandler类
 *              并且重写对应的方法，主要要注入到spring的容器管理中
* @Author: Zhu Mengze
* @Date: 2021/6/17 15:34
*/
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", LocalDateTime.now(),metaObject);
        this.setFieldValByName("updateTime", LocalDateTime.now(),metaObject);
        this.setFieldValByName("version",1,metaObject);
        this.setFieldValByName("deleteType",0,metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", LocalDateTime.now(),metaObject);
    }
}
