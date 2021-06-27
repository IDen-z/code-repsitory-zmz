package com.zmz.yygh.cmn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmz.yygh.cmn.mapper.DictMapper;
import com.zmz.yygh.cmn.service.DictService;
import com.zmzyygh.model.cmn.Dict;
import org.springframework.stereotype.Service;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    //本来service调用m-p中的mapper实现增删改查操作
    //@Autowired
    //private HospitalSetMapper hospitalSetMapper;

    //但是我们继承了ServiceImpl，它已经帮我们自动注入了baseMapper，我们可以不用注入
    //利用basemapper进行增删改查操作




}
