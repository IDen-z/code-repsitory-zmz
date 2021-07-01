package com.zmz.yygh.hosp.api.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zmz.yygh.hosp.api.repository.HospitalRepository;
import com.zmz.yygh.hosp.api.service.HospitalService;
import com.zmzyygh.model.hosp.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Service
public class HosptialServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;


    @Override
    public void saveHospotal(Map<String, Object> parameterMap) {
        String parameterMapStr = JSONObject.toJSONString(parameterMap);
        Hospital hospital = JSONObject.parseObject(parameterMapStr, Hospital.class);
        //判断是否存在相同的数据
        Hospital existHosp =hospitalRepository.getHospitalByHoscode(hospital.getHoscode());

        if (Objects.nonNull(existHosp)){
            //存在则更新
            hospital.setStatus(existHosp.getStatus());
            hospital.setCreateTime(existHosp.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }else {
            //否则添加
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }


    }

    @Override
    public Hospital getByHoscode(String hoscode) {
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        return hospital;
    }
}
