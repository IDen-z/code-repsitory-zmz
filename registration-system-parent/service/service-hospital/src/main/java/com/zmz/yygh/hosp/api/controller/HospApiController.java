package com.zmz.yygh.hosp.api.controller;

import com.zmz.yygh.common.result.Result;
import com.zmz.yygh.hosp.api.service.DepartmentService;
import com.zmz.yygh.hosp.api.service.HospitalService;
import com.zmz.yygh.hosp.api.service.ScheduleService;
import com.zmzyygh.model.hosp.Hospital;
import com.zmzyygh.vo.hosp.DepartmentVo;
import com.zmzyygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hosp/hospital")
public class HospApiController {

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private DepartmentService departmentService;


    /**
     * @Description: 提供给用户界面的分页查询医院列表
     * @Author: Zhu Mengze
     * @Date: 2021/7/8 18:45
     */
    @GetMapping("/findHospList/{page}/{limit}")
    public Result index(
            @PathVariable("page") Integer page,
            @PathVariable("limit") Integer limit,
            HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> hospitalPage = hospitalService.findPageHos(page, limit, hospitalQueryVo);
        return Result.ok(hospitalPage);
    }

    /**
     * @Description: 根据医院名称进行模糊查询
     * @Author: Zhu Mengze
     * @Date: 2021/7/8 18:49
     */
    @GetMapping("/findByHosname/{hosname}")
    public Result findByHosname(@PathVariable("hosname") String hosname) {
        List<Hospital> hospitalList = hospitalService.likeFindByHosname(hosname);
        return Result.ok(hospitalList);
    }


    /**
     * @Description: 根据医院编号获取该医院的科室信息
     * @Author: Zhu Mengze
     * @Date: 2021/7/9 11:19
     */
    @GetMapping("/department/{hoscode}")
    public Result index(@PathVariable String hoscode) {
        List<DepartmentVo> departmentVoList = departmentService.findDeptTree(hoscode);
        return Result.ok(departmentVoList);
    }

    /**
     * @Description: 根据医院编号，获取预约挂号详情信息
     * @Author: Zhu Mengze
     * @Date: 2021/7/9 11:25
     */
    @GetMapping("/findHospDetail/{hoscode}")
    public Result item(@PathVariable String hoscode) {
        Map<String, Object> map = hospitalService.item(hoscode);
        return Result.ok(map);
    }
}