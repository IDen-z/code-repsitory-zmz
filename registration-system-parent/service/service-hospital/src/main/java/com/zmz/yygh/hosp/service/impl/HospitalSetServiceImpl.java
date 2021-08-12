package com.zmz.yygh.hosp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmz.yygh.common.exception.YyghException;
import com.zmz.yygh.common.result.ResultCodeEnum;
import com.zmz.yygh.hosp.mapper.HospitalSetMapper;
import com.zmz.yygh.hosp.service.HospitalSetService;
import com.zmzyygh.model.hosp.HospitalSet;
import com.zmzyygh.vo.order.SignInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;
import java.util.Objects;

@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {
    @Override
    public HospitalSet getByHoscode(String hoscode) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("hoscode", hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(queryWrapper);
        return hospitalSet;
    }

    /**
     * 获取医院签名信息
     */
    @Override
    public SignInfoVo getSignInfoVo(String hoscode) {
        HospitalSet hospitalSet = baseMapper.selectOne(new QueryWrapper<HospitalSet>().eq("hoscode",hoscode));
        if (Objects.isNull(hospitalSet)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        SignInfoVo signInfoVo = SignInfoVo.builder()
                .signKey(hospitalSet.getSignKey())
                .apiUrl(hospitalSet.getApiUrl())
                .build();
        return signInfoVo;
    }


    //本来service调用m-p中的mapper实现增删改查操作
    //@Autowired
    //private HospitalSetMapper hospitalSetMapper;

    //但是我们继承了ServiceImpl，它已经帮我们自动注入了baseMapper，我们可以不用注入
    //利用basemapper进行增删改查操作


}
