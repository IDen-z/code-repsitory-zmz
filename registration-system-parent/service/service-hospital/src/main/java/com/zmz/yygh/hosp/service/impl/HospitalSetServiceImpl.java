package com.zmz.yygh.hosp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmz.yygh.hosp.mapper.HospitalSetMapper;
import com.zmz.yygh.hosp.service.HospitalSetService;
import com.zmzyygh.model.hosp.HospitalSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;

@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {
    @Override
    public HospitalSet getByHoscode(String hoscode) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("hoscode",hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(queryWrapper);
        return hospitalSet;
    }
    //本来service调用m-p中的mapper实现增删改查操作
    //@Autowired
    //private HospitalSetMapper hospitalSetMapper;

    //但是我们继承了ServiceImpl，它已经帮我们自动注入了baseMapper，我们可以不用注入
    //利用basemapper进行增删改查操作




}
