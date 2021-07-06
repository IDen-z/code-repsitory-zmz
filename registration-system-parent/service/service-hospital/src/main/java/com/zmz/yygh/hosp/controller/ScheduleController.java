package com.zmz.yygh.hosp.controller;

import com.zmz.yygh.common.result.Result;
import com.zmz.yygh.hosp.api.service.ScheduleService;
import com.zmzyygh.model.hosp.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/admin/hosp/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    /**
     * @Description: 根据hoscode以及depcode查询排版信息，同时满足分页要求
     * @Author: Zhu Mengze
     * @Date: 2021/7/6 17:10
     */
    @GetMapping("getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getScheduleRule(
            @PathVariable("page") Long page,
            @PathVariable("limit") Long limit,
            @PathVariable("hoscode") String hoscode,
            @PathVariable("depcode") String depcode
    ) {
        Map<String, Object> map = scheduleService.getRuleSchedule(page, limit, hoscode, depcode);
        return Result.ok(map);
    }


}
