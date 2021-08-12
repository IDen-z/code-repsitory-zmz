package com.zmz.yygh.client.hosp;

import com.zmzyygh.vo.hosp.ScheduleOrderVo;
import com.zmzyygh.vo.order.SignInfoVo;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("service-hospital")
@RequestMapping("/api/hosp/hospital")
@Component
public interface HospitalFeignProvider {


    /**
     * @Description: 根据排班id获取排版信息
     * @Author: Zhu Mengze
     * @Date: 2021/8/11 9:50
     */
    @GetMapping("/inner/getScheduleOrderVo/{scheduleId}")
    public ScheduleOrderVo getScheduleOrderVo(
            @PathVariable("scheduleId") String scheduleId);

    /**
     * @Description: 获取医院签名信息
     * @Author: Zhu Mengze
     * @Date: 2021/8/12 9:57
     */
    @GetMapping("/inner/getSignInfoVo/{hoscode}")
    public SignInfoVo getSignInfoVo(
            @ApiParam(name = "hoscode", value = "医院code", required = true)
            @PathVariable("hoscode") String hoscode);


}
