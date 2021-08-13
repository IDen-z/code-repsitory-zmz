package com.zmz.yygh.msm.service;

import com.zmzyygh.vo.msm.MsmVo;

public interface MsmService {
    Boolean sendMessage(String phoneNum, String code);

    boolean send(MsmVo msmVo);
}
