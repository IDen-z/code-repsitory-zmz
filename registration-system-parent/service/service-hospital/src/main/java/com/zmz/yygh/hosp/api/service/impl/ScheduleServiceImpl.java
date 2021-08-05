package com.zmz.yygh.hosp.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zmz.yygh.common.exception.YyghException;
import com.zmz.yygh.common.result.ResultCodeEnum;
import com.zmz.yygh.hosp.api.repository.ScheduleRepository;
import com.zmz.yygh.hosp.api.service.DepartmentService;
import com.zmz.yygh.hosp.api.service.HospitalService;
import com.zmz.yygh.hosp.api.service.ScheduleService;
import com.zmzyygh.model.hosp.BookingRule;
import com.zmzyygh.model.hosp.Department;
import com.zmzyygh.model.hosp.Hospital;
import com.zmzyygh.model.hosp.Schedule;
import com.zmzyygh.vo.hosp.BookingScheduleRuleVo;
import com.zmzyygh.vo.hosp.ScheduleQueryVo;
import io.swagger.models.auth.In;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


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
        baseMap.put("hosname", hosname);

        Map<String, Object> res = new HashMap<>();
        res.put("bookingScheduleRuleVoList", bookingScheduleRuleVoList);
        res.put("total", total);
        res.put("baseMap", baseMap);
        return res;


    }

    /**
     * 获取排班信息
     */
    @Override
    public List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate) {
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        Schedule schedule = new Schedule();
        schedule.setHoscode(hoscode);
        schedule.setDepcode(depcode);
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(workDate);
            schedule.setWorkDate(date);
        } catch (ParseException e) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        Example<Schedule> example = Example.of(schedule, exampleMatcher);
        List<Schedule> scheduleList = scheduleRepository.findAll(example);
        //把以上的到的list结果进行遍历，塞一些前端有用的值例如医院名称，科室名称，星期等
        scheduleList.stream().forEach(item -> {
            this.packgeSchedule(item);
        });
        return scheduleList;
    }

    /**
     * 获取可预约的排班数据
     */
    @Override
    public Map<String, Object> getBookingScheduleRule(Integer page, Integer limit, String hoscode, String depcode) {
        HashMap<String, Object> result = Maps.newHashMap();
        //根据医院编号，获取具体的预约规则
        Hospital hospital = hospitalService.getByHoscode(hoscode);
        if (Objects.isNull(hospital)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        BookingRule bookingRule = hospital.getBookingRule();
        //获取可预约日期的数据
        IPage<Date> iPage = this.getListDate(page, limit, bookingRule);
        List<Date> dateList = iPage.getRecords();
        //获取可预约日期里面科室的剩余预约数
        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode).and("workDate").in(dateList);
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate").first("workDate").as("workDate")
                        .count().as("docCount").sum("reservedNumber").as("reservedNumber")
                        .sum("availableNumber").as("availableNumber"));
        AggregationResults<BookingScheduleRuleVo> aggregationResults = mongoTemplate.aggregate(aggregation, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> scheduleVoList = aggregationResults.getMappedResults();

        //合并数据 将统计数据ScheduleVo根据“安排日期”合并到BookingRuleVo
        Map<Date, BookingScheduleRuleVo> scheduleVoMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(scheduleVoList)) {
            scheduleVoMap = scheduleVoList.stream().collect(
                    Collectors.toMap(BookingScheduleRuleVo::getWorkDate, item -> item));
        }

        //获取可预约排班规则
        List<BookingScheduleRuleVo> bookingScheduleRuleVoList = new ArrayList<>();
        for (int i = 0, len = dateList.size(); i < len; i++) {
            Date date = dateList.get(i);

            BookingScheduleRuleVo bookingScheduleRuleVo = scheduleVoMap.get(date);
            if (null == bookingScheduleRuleVo) { // 说明当天没有排班医生
                bookingScheduleRuleVo = new BookingScheduleRuleVo();
                //就诊医生人数
                bookingScheduleRuleVo.setDocCount(0);
                //科室剩余预约数  -1表示无号
                bookingScheduleRuleVo.setAvailableNumber(-1);
            }
            bookingScheduleRuleVo.setWorkDate(date);
            bookingScheduleRuleVo.setWorkDateMd(date);
            //计算当前预约日期为周几
            String dayOfWeek = this.getDayOfWeek(new DateTime(date));
            bookingScheduleRuleVo.setDayOfWeek(dayOfWeek);

            //最后一页最后一条记录为即将预约   状态 0：正常 1：即将放号 -1：当天已停止挂号
            if (i == len - 1 && page == iPage.getPages()) {
                bookingScheduleRuleVo.setStatus(1);
            } else {
                bookingScheduleRuleVo.setStatus(0);
            }
            //当天预约如果过了停号时间， 不能预约
            if (i == 0 && page == 1) {
                DateTime stopTime = this.getDateTime(new Date(), bookingRule.getStopTime());
                if (stopTime.isBeforeNow()) {
                    //停止预约
                    bookingScheduleRuleVo.setStatus(-1);
                }
            }
            bookingScheduleRuleVoList.add(bookingScheduleRuleVo);
        }

        //可预约日期规则数据
        result.put("bookingScheduleList", bookingScheduleRuleVoList);
        result.put("total", iPage.getTotal());
        //其他基础数据
        Map<String, String> baseMap = new HashMap<>();
        //医院名称
        baseMap.put("hosname", hospitalService.getHosName(hoscode));
        //科室
        Department department = departmentService.getDepartment(hoscode, depcode);
        //大科室名称
        baseMap.put("bigname", department.getBigname());
        //科室名称
        baseMap.put("depname", department.getDepname());
        //月
        baseMap.put("workDateString", new DateTime().toString("yyyy年MM月"));
        //放号时间
        baseMap.put("releaseTime", bookingRule.getReleaseTime());
        //停号时间
        baseMap.put("stopTime", bookingRule.getStopTime());
        result.put("baseMap", baseMap);
        return result;


    }

    private IPage<Date> getListDate(Integer page, Integer limit, BookingRule bookingRule) {
        //获取当天放号时间 年月日 小时 分钟
        DateTime releaseTime = this.getDateTime(new Date(), bookingRule.getReleaseTime());
        //获取可预约的周期
        Integer cycle = bookingRule.getCycle();
        //如果今天已经过了挂号时间，预约周期加一
        if (releaseTime.isBefore(new DateTime())) {
            cycle++;
        }
        //显示可预约所有日期，最后一天显示即将放号
        ArrayList<Date> dateList = Lists.newArrayList();
        for (int i = 0; i < cycle; i++) {
            DateTime curDateTime = new DateTime().plusDays(i);
            String dateString = curDateTime.toString("yyyy-MM-dd");
            dateList.add(new DateTime(dateString).toDate());
        }
        //分页显示，最多七个
        List<Date> pageDateList = Lists.newArrayList();
        int start = (page - 1) * limit;
        int end = start + limit;
        //end就是当前页+limit 就是满7  比size 大就说明直接显示即可
//        if (end > dateList.size()) {
//        }
        end = Math.min(end, dateList.size());
        for (int i = start; i < end; i++) {
            pageDateList.add(dateList.get(i));
        }
        IPage<Date> iPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, limit, dateList.size());
        iPage.setRecords(pageDateList);
        return iPage;
    }

    /**
     * 将Date日期（yyyy-MM-dd HH:mm）转换为DateTime
     */
    private DateTime getDateTime(Date date, String timeString) {
        String dateTimeString = new DateTime(date).toString("yyyy-MM-dd") + " " + timeString;
        DateTime dateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").parseDateTime(dateTimeString);
        return dateTime;
    }


    private void packgeSchedule(Schedule schedule) {
        //设置医院名称
        schedule.getParam().put("hosname", hospitalService.getHosName(schedule.getHoscode()));
        //设置科室名称
        schedule.getParam().put("depname", departmentService.getDepName(schedule.getHoscode(), schedule.getDepcode()));
        //设置日期对应星期
        schedule.getParam().put("dayOfWeek", this.getDayOfWeek(new DateTime(schedule.getWorkDate())));

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
