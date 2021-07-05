package com.zmz.yygh.hosp.api.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zmz.yygh.common.exception.YyghException;
import com.zmz.yygh.common.result.ResultCodeEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import com.zmz.yygh.hosp.api.repository.DepartmentRepository;
import com.zmz.yygh.hosp.api.service.DepartmentService;
import com.zmzyygh.model.hosp.Department;
import com.zmzyygh.vo.hosp.DepartmentQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

        if (Objects.isNull(resDepartment)) {
            //如果为空则添加
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        } else {
            //非空更新
            //主要是更新一下状态
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }


    }

    @Override
    public Page<Department> findPageDepartment(Integer page, Integer limit, DepartmentQueryVo departmentQueryVo) {
        //创建PageAble对象，设置当前页和每页记录数 0是第一页
        Pageable pageable = PageRequest.of(page - 1, limit);
        //创建Example对象，设置条件
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        //将VO类转为department
        Department department=new Department();
        BeanUtils.copyProperties(departmentQueryVo,department);
        department.setIsDeleted(0);
        Example<Department> example = Example.of(department, exampleMatcher);
        Page<Department> pageRes = departmentRepository.findAll(example, pageable);
        return pageRes;
    }

    //删除科室
    @Override
    public void removeDepartment(String hoscode, String depcode) {
        Department department=departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode,depcode);
        //注意，这里一定要做判断，有可能查询结果为空！
        if (Objects.isNull(department)){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        departmentRepository.delete(department);
    }


}
