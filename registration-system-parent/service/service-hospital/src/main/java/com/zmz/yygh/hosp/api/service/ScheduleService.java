package com.zmz.yygh.hosp.api.service;

import com.zmzyygh.model.hosp.Schedule;
import com.zmzyygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface ScheduleService {


    void saveSchedule(Map<String, Object> paramMap);

    Page<Schedule> findSchedule(Integer page, Integer limit, ScheduleQueryVo scheduleQueryVo);

    void removeSchedule(String hoscode, String hosScheduleId);
}
