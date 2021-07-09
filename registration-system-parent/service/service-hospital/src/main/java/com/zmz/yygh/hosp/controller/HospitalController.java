package com.zmz.yygh.hosp.controller;

import com.zmz.yygh.common.result.Result;
import com.zmz.yygh.hosp.api.service.HospitalService;
import com.zmzyygh.model.hosp.Hospital;
import com.zmzyygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/hosp/hospital")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    /**
     * @Description: 设计医院管理接口，医院列表分页查询
     * 注意，这里是条件查询带分页，
     * @Author: Zhu Mengze
     * @Date: 2021/7/2 17:16
     */
    @GetMapping("/list/{page}/{limit}")
    public Result hosList(@PathVariable("page") Integer page,
                          @PathVariable("limit") Integer limit,
                          HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> pageRes =hospitalService.findPageHos(page,limit,hospitalQueryVo);
        return Result.ok(pageRes);
    }

    /**
    * @Description: 更新医院状态
    * @Author: Zhu Mengze
    * @Date: 2021/7/5 16:55
    */
    @GetMapping("updateStatus/{id}/{status}")
    public Result updateStatus(
            @PathVariable("id") String id,
            @PathVariable("status") Integer status){
        hospitalService.updateStatus(id, status);
        return Result.ok();
    }

    /**
    * @Description: 医院详情信息
    * @Author: Zhu Mengze
    * @Date: 2021/7/6 9:28
    */
    @GetMapping("showDetail/{id}")
    public Result showDetail(@PathVariable("id") String id){
        Map<String,Object> map=hospitalService.getById(id);
        return Result.ok(map);
    }




}
