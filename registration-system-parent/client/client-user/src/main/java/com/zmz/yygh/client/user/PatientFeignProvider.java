package com.zmz.yygh.client.user;

import com.zmzyygh.model.user.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("service-user")
@RequestMapping("/api/user/patient")
@Component
public interface PatientFeignProvider {


    /**
     * @Description: 根据ID获取就诊人信息
     * @Author: Zhu Mengze
     * @Date: 2021/8/11 9:35
     */
    @GetMapping("inner/get/{id}")
    public Patient getPatientOrder(
            @PathVariable("id") Long id);

}
