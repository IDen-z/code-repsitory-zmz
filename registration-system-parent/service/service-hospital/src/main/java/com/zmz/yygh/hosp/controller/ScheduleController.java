package com.zmz.yygh.hosp.controller;

import com.zmz.yygh.common.result.Result;
import com.zmz.yygh.hosp.api.service.ScheduleService;
import com.zmzyygh.model.hosp.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/hosp/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    /**
     * @Description: 根据hoscode以及depcode查询该科室的排班安排同时满足分页要求
     *               主要是将该科室的排版日，剩余挂号等，利用分页展示在页面上半部分，供用户选择
     * @Author: Zhu Mengze
     * @Date: 2021/7/6 17:10
     */
    @GetMapping("/getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getScheduleRule(
            @PathVariable("page") Long page,
            @PathVariable("limit") Long limit,
            @PathVariable("hoscode") String hoscode,
            @PathVariable("depcode") String depcode
    ) {
        Map<String, Object> map = scheduleService.getRuleSchedule(page, limit, hoscode, depcode);
        return Result.ok(map);
    }


    /**
    * @Description: 根据医院编号，科室标号，排版日期
     *              查询当天的详细排班信息
    * @Author: Zhu Mengze
    * @Date: 2021/7/8 9:48
    */
    @GetMapping("/getScheduleDetail/{hoscode}/{depcode}/{workDate}")
    public Result getScheduleDetail( @PathVariable("hoscode") String hoscode,
                                     @PathVariable("depcode") String depcode,
                                     @PathVariable("workDate") String workDate) {
        List<Schedule> list = scheduleService.getDetailSchedule(hoscode,depcode,workDate);
        return Result.ok(list);
    }



}
