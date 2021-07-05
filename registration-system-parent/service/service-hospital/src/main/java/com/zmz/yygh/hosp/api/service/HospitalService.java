package com.zmz.yygh.hosp.api.service;

import com.zmzyygh.model.hosp.Hospital;
import com.zmzyygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface HospitalService {
    void saveHospotal(Map<String,Object> parameterMap);

    Hospital getByHoscode(String hoscode);

    Page<Hospital> findPageHos(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);
}
