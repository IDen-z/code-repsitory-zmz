package com.zmz.yygh.hosp.api.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zmz.yygh.client.cmn.DictFeignProvider;
import com.zmz.yygh.hosp.api.repository.HospitalRepository;
import com.zmz.yygh.hosp.api.service.HospitalService;
import com.zmzyygh.model.hosp.Hospital;
import com.zmzyygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Service
public class HosptialServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private DictFeignProvider dictFeignProvider;


    @Override
    public void saveHospotal(Map<String, Object> parameterMap) {
        String parameterMapStr = JSONObject.toJSONString(parameterMap);
        Hospital hospital = JSONObject.parseObject(parameterMapStr, Hospital.class);
        //判断是否存在相同的数据
        Hospital existHosp = hospitalRepository.getHospitalByHoscode(hospital.getHoscode());

        if (Objects.nonNull(existHosp)) {
            //存在则更新
            hospital.setStatus(existHosp.getStatus());
            hospital.setCreateTime(existHosp.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        } else {
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

    /**
     * @Description: 分页查询医院列表
     * @Author: Zhu Mengze
     * @Date: 2021/7/5 9:09
     */
    @Override
    public Page<Hospital> findPageHos(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        //创建分页对象
        Pageable pageable = PageRequest.of(page - 1, limit);
        //创建条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                //设置为模糊查询
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);

        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo, hospital);

        Example<Hospital> example = Example.of(hospital, exampleMatcher);

        Page<Hospital> hospitalPage = hospitalRepository.findAll(example, pageable);
        //查询到这里还没完
        //根据业务需求，查询的时候也需要把医院的等级查询出来
        //但是mongoDB中只存储了hostypeID，对应的值还需要去数据库里拿
        //因此需要openfeign调用provider，查到对应的name也就是医院等级

        //注意，由于Hospital数据库中没有医院等级，但是baseEntity里有map
        //可以存在map里给前端用
        hospitalPage.getContent().stream().forEach(item -> {
            this.setHospitalHostype(item);
        });


        return hospitalPage;
    }

    //获取listj集合，便利进行医院等级set
    private void setHospitalHostype(Hospital hospital) {
        String hostypeString = dictFeignProvider.getNameByDictCodeAndValue("Hostype", hospital.getHostype());
        String provinceString = dictFeignProvider.getNameByDictValue(hospital.getProvinceCode());
        String cityString = dictFeignProvider.getNameByDictValue(hospital.getCityCode());
        String districtString = dictFeignProvider.getNameByDictValue(hospital.getDistrictCode());

        hospital.getParam().put("hostypeString",hostypeString);
        hospital.getParam().put("fullAddress",provinceString+cityString+districtString);
    }
}
