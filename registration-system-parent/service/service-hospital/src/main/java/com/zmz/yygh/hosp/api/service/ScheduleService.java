package com.zmz.yygh.hosp.api.service;

import com.zmzyygh.model.hosp.Schedule;
import com.zmzyygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ScheduleService {


    void saveSchedule(Map<String, Object> paramMap);

    Page<Schedule> findSchedule(Integer page, Integer limit, ScheduleQueryVo scheduleQueryVo);

    void removeSchedule(String hoscode, String hosScheduleId);

    Map<String, Object> getRuleSchedule(Long page, Long limit, String hoscode, String depcode);

    List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate);

    Map<String, Object> getBookingScheduleRule(Integer page, Integer limit, String hoscode, String depcode);
}
