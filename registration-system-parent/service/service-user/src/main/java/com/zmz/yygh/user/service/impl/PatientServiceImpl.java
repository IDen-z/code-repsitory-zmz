package com.zmz.yygh.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmz.yygh.client.cmn.DictFeignProvider;
import com.zmz.yygh.common.exception.YyghException;
import com.zmz.yygh.common.result.ResultCodeEnum;
import com.zmz.yygh.user.mapper.PatientMapper;
import com.zmz.yygh.user.service.PatientService;
import com.zmzyygh.enums.DictEnum;
import com.zmzyygh.model.user.Patient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {

    @Autowired
    private DictFeignProvider dictFeignProvider;

    @Override
    public List<Patient> findAllByUserId(Long userId) {
        //只传patient的列表不是很够，因为这里面都是省市证件号码的id
        //我们需要去查数据字典表将对应的name拿出来
        // 这里封装一个打包方法，只要是查patien，就需要调用一下

        List<Patient> patientList = baseMapper.selectList(new QueryWrapper<Patient>().eq("user_id", userId));
        patientList.stream().forEach(item->{
            //调用打包方法
            this.packPatient(item);
        });
        return patientList;
    }

    @Override
    public Patient getPatientId(Long id) {
        Patient patient = baseMapper.selectById(id);
        if (Objects.isNull(patient)){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        this.packPatient(patient);
        return patient;
    }


    /**
    * @Description: 封装就诊人，主要是把一些ID值通过查表转换为name
    * @Author: Zhu Mengze
    * @Date: 2021/7/19 9:56
    */
    private void packPatient(Patient patient) {
        //根据就诊人证件类型编码获取name
        String certificatesName = dictFeignProvider.getNameByDictCodeAndValue(DictEnum.CERTIFICATES_TYPE.getDictCode(), patient.getCertificatesType());
        //根据联系人证件类型编码获取name
        String contactsCertificatesName = dictFeignProvider.getNameByDictCodeAndValue(DictEnum.CERTIFICATES_TYPE.getDictCode(), patient.getContactsCertificatesType());
        //省
        String province = dictFeignProvider.getNameByDictValue(patient.getProvinceCode());
        //市
        String city = dictFeignProvider.getNameByDictValue(patient.getCityCode());
        //区
        String district = dictFeignProvider.getNameByDictValue(patient.getDistrictCode());
        patient.getParam().put("certificatesTypeString", certificatesName);
        patient.getParam().put("contactsCertificatesTypeString", contactsCertificatesName);
        patient.getParam().put("provinceString", province);
        patient.getParam().put("cityString", city);
        patient.getParam().put("districtString", district);
        patient.getParam().put("fullAddress", province + city + district + patient.getAddress());

    }
}
