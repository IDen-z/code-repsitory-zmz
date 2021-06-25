package com.zmz.yygh.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zmz.yygh.common.result.Result;
import com.zmz.yygh.common.util.MD5;
import com.zmz.yygh.hosp.service.HospitalSetService;
import com.zmzyygh.model.hosp.HospitalSet;
import com.zmzyygh.vo.hosp.HospitalSetQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/admin/hosp/hospitalSet")
@CrossOrigin
public class HospitalSetControlelr {

    //注入service
    @Autowired
    private HospitalSetService hospitalSetService;


    /**
     * @Description: 查询医院设置表的所有信息
     * @Author: Zhu Mengze
     * @Date: 2021/6/22 10:33
     */
    @GetMapping("/findAll")
    public Result<List<HospitalSet>> findAllHospitalSet() {
        List<HospitalSet> hospitalSetList = hospitalSetService.list();
        return Result.ok(hospitalSetList);
    }

    /**
     * @Description: 指定ID删除removeHospitalSet
     * @Author: Zhu Mengze
     * @Date: 2021/6/22 10:47
     */
    @PostMapping("/{id}")
    public Result<Boolean> removeHospitalSet(@PathVariable("id") Long id) {
        boolean flag = hospitalSetService.removeById(id);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    /**
     * @Description: 条件查询带分页功能
     * @Author: Zhu Mengze
     * @Date: 2021/6/22 14:24
     */
    @PostMapping("/findPageHospSet/{current}/{limit}")
    public Result<Page<HospitalSet>> findPageHospSet(
            @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo,
            @PathVariable("current") long current,
            @PathVariable("limit") long limit
    ) {
        //获取分页对象,传递当前页，每页的记录数
        Page<HospitalSet> page = new Page<>(current, limit);
        QueryWrapper<HospitalSet> queryWrapper = new QueryWrapper<>();
        String hosname = hospitalSetQueryVo.getHosname();
        String hoscode = hospitalSetQueryVo.getHoscode();
        //调用方法实现分页查询
        //queryWrapper是条件构造器
        if (!StringUtils.isEmpty(hoscode)) {
//            注意，这里前端表单提交的时候，如果表单不填写，字符串是空值，不是null
            queryWrapper.eq("hoscode", hoscode);
        }
        if (!StringUtils.isEmpty(hosname)) {
            queryWrapper.like("hosname", hosname);
        }
        Page<HospitalSet> hospitalSetPage = hospitalSetService.page(page, queryWrapper);

        return Result.ok(hospitalSetPage);
    }


    /**
     * @Description: 添加医院设置
     * @Author: Zhu Mengze
     * @Date: 2021/6/22 15:27
     */
    @PostMapping("/saveHospitalSet")
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet) {
        //传入HospitalSet对象，添加医院设置
        //这里要注意，签名密钥和状态需要单独设置
        hospitalSet.setStatus(1);
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis() + "" + new Random().nextInt(1000)));
        boolean flag = hospitalSetService.save(hospitalSet);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }


    }

    /**
     * @Description: 根据id获取医院设置
     * @Author: Zhu Mengze
     * @Date: 2021/6/22 15:27
     */
    @GetMapping("/getHospSet/{id}")
    public Result<HospitalSet> getHospSet(@PathVariable("id") Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }


    /**
     * @Description: 修改医院设置
     * @Author: Zhu Mengze
     * @Date: 2021/6/22 15:27
     */
    @PostMapping("/updateHospitalSet")
    public Result updateHospitalSet(@RequestBody HospitalSet hospitalSet) {
        boolean flag = hospitalSetService.updateById(hospitalSet);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }


    }


    /**
     * @Description: 批量删除医院设置
     * @Author: Zhu Mengze
     * @Date: 2021/6/22 15:27
     */
    @PostMapping("/batchRemove")
    public Result batchRemoveHospitalSet(@RequestBody List<Long> idList) {
        hospitalSetService.removeByIds(idList);
        return Result.ok();
    }

    /**
     * @Description: 锁定和解锁医院设置
     * @Author: Zhu Mengze
     * @Date: 2021/6/22 18:38
     */
    @PostMapping("/lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable("id") Long id, @PathVariable("status") Integer status) {

        HospitalSet hospitalSet = hospitalSetService.getById(id);
        hospitalSet.setStatus(status);
        boolean flag = hospitalSetService.updateById(hospitalSet);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    /**
    * @Description: 发送签名密钥
    * @Author: Zhu Mengze
    * @Date: 2021/6/22 18:44
    */
    @PostMapping("/sendKey/{id}")
    public Result lockHospitalSet(@PathVariable("id") Long id){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
        //TODO 发送短信
        return Result.ok();
    }



}
