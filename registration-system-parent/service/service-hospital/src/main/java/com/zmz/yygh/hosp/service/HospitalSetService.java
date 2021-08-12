package com.zmz.yygh.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zmzyygh.model.hosp.HospitalSet;
import com.zmzyygh.vo.order.SignInfoVo;

public interface HospitalSetService extends IService<HospitalSet> {
    HospitalSet getByHoscode(String hoscode);

    SignInfoVo getSignInfoVo(String hoscode);
}
