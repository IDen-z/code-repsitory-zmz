package com.zmz.yygh.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmz.yygh.user.mapper.PatientMapper;
import com.zmz.yygh.user.service.PatientService;
import com.zmzyygh.model.user.Patient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {

    @Override
    public List<Patient> findAllByUserId(Long userId) {
        //只传patient的列表不是很够，因为这里面都是省市证件号码的id
        //我们需要去查数据字典表将对应的name拿出来

        List<Patient> patientList = baseMapper.selectList(new QueryWrapper<Patient>().eq("user_id", userId));
        return patientList;
    }
}
