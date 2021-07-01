package com.zmz.yygh.hosp.api.service;

import com.zmzyygh.model.hosp.Hospital;

import java.util.Map;

public interface HospitalService {
    void saveHospotal(Map<String,Object> parameterMap);

    Hospital getByHoscode(String hoscode);
}
