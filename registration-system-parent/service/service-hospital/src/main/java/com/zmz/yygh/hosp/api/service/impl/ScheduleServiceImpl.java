package com.zmz.yygh.hosp.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.zmz.yygh.common.exception.YyghException;
import com.zmz.yygh.common.result.ResultCodeEnum;
import com.zmz.yygh.hosp.api.repository.ScheduleRepository;
import com.zmz.yygh.hosp.api.service.DepartmentService;
import com.zmz.yygh.hosp.api.service.HospitalService;
import com.zmz.yygh.hosp.api.service.ScheduleService;
import com.zmzyygh.model.hosp.Schedule;
import com.zmzyygh.vo.hosp.BookingScheduleRuleVo;
import com.zmzyygh.vo.hosp.ScheduleQueryVo;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    //repository虽然便于操作mongoDB，但是遇到复杂的聚合，分组操作时
    //可以用更加灵活的template来代替
    private MongoTemplate mongoTemplate;

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private DepartmentService departmentService;


    @Override
    public void saveSchedule(Map<String, Object> paramMap) {
        //转为对象
        Schedule schedule = JSON.parseObject(JSON.toJSONString(paramMap), Schedule.class);
        //先查，查得到就修改，查不到就添加
        Schedule resSchedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(schedule.getHoscode(), schedule.getHosScheduleId());
        if (Objects.isNull(resSchedule)) {
            schedule.setIsDeleted(0);
            schedule.setCreateTime(new Date());
            schedule.setUpdateTime(new Date());
            schedule.setStatus(1);
            scheduleRepository.save(schedule);
        } else {
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
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleQueryVo, schedule);
        schedule.setIsDeleted(0);
        Example<Schedule> example = Example.of(schedule, exampleMatcher);
        return scheduleRepository.findAll(example, pageable);
    }

    @Override
    public void removeSchedule(String hoscode, String hosScheduleId) {
        //先查看能否查到
        Schedule schedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(hoscode, hosScheduleId);
        if (Objects.isNull(schedule)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        scheduleRepository.delete(schedule);
    }

    @Override
    public Map<String, Object> getRuleSchedule(Long page, Long limit, String hoscode, String depcode) {
        //查询排版规则还要分页
        //排班信息中有一个workdate，以这个来分组
        //同时还要求出当天workdate的所有可预约号的总数，和剩余的总数
        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode);
        //这个Aggregation用于mongo中的聚合操作，例如分组，求和等。
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),//匹配规则
                Aggregation.group("workDate")//以什么字段分组
                        .first("workDate").as("workDate")
                        .count().as("docCount")
                        .sum("reservedNumber").as("reservedNumber")
                        .sum("availableNumber").as("availableNumber"),
                Aggregation.sort(Sort.Direction.ASC, "workDate"),
                Aggregation.skip((page - 1) * limit),
                Aggregation.limit(limit)
        );
        AggregationResults<BookingScheduleRuleVo> aggregationResults = mongoTemplate.aggregate(aggregation, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> bookingScheduleRuleVoList = aggregationResults.getMappedResults();
        //以上就是所有的预约规则

        //接下来把日期对应的星期获取出来
        for (BookingScheduleRuleVo ruleVo : bookingScheduleRuleVoList) {
            String dayOfWeek = this.getDayOfWeek(new DateTime(ruleVo.getWorkDate()));
            ruleVo.setDayOfWeek(dayOfWeek);
        }

        Aggregation totalAgg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
        );
        AggregationResults<BookingScheduleRuleVo> totalResults = mongoTemplate.aggregate(totalAgg, Schedule.class, BookingScheduleRuleVo.class);
        int total = totalResults.getMappedResults().size();


        //获取医院名称
        String hosname = hospitalService.getHosName(hoscode);
        Map<String, String> baseMap = new HashMap<>();
        baseMap.put("hosname",hosname);

        Map<String, Object> res = new HashMap<>();
        res.put("bookingScheduleRuleVoList", bookingScheduleRuleVoList);
        res.put("total", total);
        res.put("baseMap",baseMap);
        return res;


    }

    @Override
    public List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate) {
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        Schedule schedule=new Schedule();
        schedule.setHoscode(hoscode);
        schedule.setDepcode(depcode);
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(workDate);
            schedule.setWorkDate(date);
        } catch (ParseException e) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        Example<Schedule> example=Example.of(schedule,exampleMatcher);
        List<Schedule> scheduleList = scheduleRepository.findAll(example);
        //把以上的到的list结果进行遍历，塞一些前端有用的值例如医院名称，科室名称，星期等
        scheduleList.stream().forEach(item->{
            this.packgeSchedule(item);
        });
        return scheduleList;
    }

    private void packgeSchedule(Schedule schedule) {
        //设置医院名称
        schedule.getParam().put("hosname",hospitalService.getHosName(schedule.getHoscode()));
        //设置科室名称
        schedule.getParam().put("depname",departmentService.getDepName(schedule.getHoscode(),schedule.getDepcode()));
        //设置日期对应星期
        schedule.getParam().put("dayOfWeek",this.getDayOfWeek(new DateTime(schedule.getWorkDate())));

    }


    /**
     * 工具类，根据日期获取周几数据
     *
     * @param dateTime
     * @return
     */
    private String getDayOfWeek(DateTime dateTime) {
        String dayOfWeek = "";
        switch (dateTime.getDayOfWeek()) {
            case DateTimeConstants.SUNDAY:
                dayOfWeek = "周日";
                break;
            case DateTimeConstants.MONDAY:
                dayOfWeek = "周一";
                break;
            case DateTimeConstants.TUESDAY:
                dayOfWeek = "周二";
                break;
            case DateTimeConstants.WEDNESDAY:
                dayOfWeek = "周三";
                break;
            case DateTimeConstants.THURSDAY:
                dayOfWeek = "周四";
                break;
            case DateTimeConstants.FRIDAY:
                dayOfWeek = "周五";
                break;
            case DateTimeConstants.SATURDAY:
                dayOfWeek = "周六";
            default:
                break;
        }
        return dayOfWeek;


    }
}
