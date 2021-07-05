package com.zmz.yygh.hosp.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zmz.yygh.common.exception.YyghException;
import com.zmz.yygh.common.result.ResultCodeEnum;
import com.zmz.yygh.hosp.api.repository.ScheduleRepository;
import com.zmz.yygh.hosp.api.service.ScheduleService;
import com.zmzyygh.model.hosp.Department;
import com.zmzyygh.model.hosp.Schedule;
import com.zmzyygh.vo.hosp.ScheduleQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Objects;


@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;


    @Override
    public void saveSchedule(Map<String, Object> paramMap) {
        //转为对象
        Schedule schedule = JSON.parseObject(JSON.toJSONString(paramMap), Schedule.class);
        //先查，查得到就修改，查不到就添加
        Schedule resSchedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(schedule.getHoscode(), schedule.getHosScheduleId());
        if (Objects.isNull(resSchedule)){
            schedule.setIsDeleted(0);
            schedule.setCreateTime(new Date());
            schedule.setUpdateTime(new Date());
            schedule.setStatus(1);
            scheduleRepository.save(schedule);
        }else{
            resSchedule.setIsDeleted(0);
            resSchedule.setUpdateTime(new Date());
            resSchedule.setStatus(1);
            scheduleRepository.save(resSchedule);
        }

    }

    @Override
    public Page<Schedule> findSchedule(Integer page, Integer limit, ScheduleQueryVo scheduleQueryVo) {
        //创建PageAble对象，设置当前页和每页记录数 0是第一页
        Pageable pageable = PageRequest.of(page - 1, limit);
        //创建Example对象，设置条件
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        //将VO类转为department
        Schedule schedule =new Schedule();
        BeanUtils.copyProperties(scheduleQueryVo,schedule);
        schedule.setIsDeleted(0);
        Example<Schedule> example = Example.of(schedule, exampleMatcher);
        return scheduleRepository.findAll(example, pageable);
    }

    @Override
    public void removeSchedule(String hoscode, String hosScheduleId) {
        //先查看能否查到
        Schedule schedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(hoscode, hosScheduleId);
        if (Objects.isNull(schedule)){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        scheduleRepository.delete(schedule);
    }


}
