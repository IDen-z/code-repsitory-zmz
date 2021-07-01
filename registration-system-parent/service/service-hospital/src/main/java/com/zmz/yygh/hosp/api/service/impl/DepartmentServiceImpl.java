package com.zmz.yygh.hosp.api.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zmz.yygh.hosp.api.repository.DepartmentRepository;
import com.zmz.yygh.hosp.api.service.DepartmentService;
import com.zmzyygh.model.hosp.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public void saveDeptment(Map<String, Object> paramMap) {
        Department department = JSONObject.parseObject(JSONObject.toJSONString(paramMap), Department.class);
        String hoscode = department.getHoscode();
        String depcode = department.getDepcode();
        //一个医院可能有多个科室
        //因此需要先查hoscode，再查depcode
        //这里只需要写repository接口是因为用的驼峰命名
        Department resDepartment = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);

        if(Objects.isNull(resDepartment)){
            //如果为空则添加
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }else {
            //非空更新
            //主要是更新一下状态
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }



    }
}
