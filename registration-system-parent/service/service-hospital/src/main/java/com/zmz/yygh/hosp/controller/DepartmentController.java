package com.zmz.yygh.hosp.controller;

import com.zmz.yygh.common.result.Result;
import com.zmz.yygh.hosp.api.service.DepartmentService;
import com.zmzyygh.vo.hosp.DepartmentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/admin/hosp/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    /**
    * @Description: 根据y医院编号，查询所有课室列表
    * @Author: Zhu Mengze
    * @Date: 2021/7/6 15:20
    */
    @GetMapping("/getDeptList/{hoscode}")
    public Result getDeptList(@PathVariable("hoscode") String hoscode){
        List<DepartmentVo> list=departmentService.findDeptTree(hoscode);
        return Result.ok(list);
    }





}
