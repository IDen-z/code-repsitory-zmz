package com.zmz.yygh.hosp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmz.yygh.hosp.mapper.HospitalSetMapper;
import com.zmz.yygh.hosp.service.HospitalSetService;
import com.zmzyygh.model.hosp.HospitalSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {
    //本来service调用m-p中的mapper实现增删改查操作
    //@Autowired
    //private HospitalSetMapper hospitalSetMapper;

    //但是我们继承了ServiceImpl，它已经帮我们自动注入了baseMapper，我们可以不用注入
    //利用basemapper进行增删改查操作




}
