package com.zmz.yygh.hosp.api.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zmz.yygh.common.exception.YyghException;
import com.zmz.yygh.common.result.ResultCodeEnum;
import com.zmzyygh.vo.hosp.DepartmentVo;
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

import java.util.*;
import java.util.stream.Collectors;

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
        Department department = new Department();
        BeanUtils.copyProperties(departmentQueryVo, department);
        department.setIsDeleted(0);
        Example<Department> example = Example.of(department, exampleMatcher);
        Page<Department> pageRes = departmentRepository.findAll(example, pageable);
        return pageRes;
    }

    //删除科室
    @Override
    public void removeDepartment(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        //注意，这里一定要做判断，有可能查询结果为空！
        if (Objects.isNull(department)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        departmentRepository.delete(department);
    }

    @Override
    public List<DepartmentVo> findDeptTree(String hoscode) {
        Department department = new Department();
        department.setHoscode(hoscode);
        Example<Department> example = Example.of(department);
        List<Department> departmentList = departmentRepository.findAll(example);
        //以上只是得到了对应hoscode的所有科室信息，但是和前端要求的不同
        //前端要求的VO是要json类型的树形结构
        //需要对其进行分组
        Map<String, List<Department>> listMap = departmentList.stream().collect(Collectors.groupingBy(Department::getBigcode));
        //分组之后，key为bigcode，
        List<DepartmentVo> res = new ArrayList<>();
        for (Map.Entry<String, List<Department>> entry : listMap.entrySet()) {

            String bigcode = entry.getKey();

            List<Department> curentDepList = entry.getValue();
            DepartmentVo departmentVo = new DepartmentVo();
            departmentVo.setDepcode(bigcode);
            departmentVo.setDepname(curentDepList.get(0).getBigname());//get(0)是因为这个list里的所有bigname都相同，所有随便拿一个即可
            List<DepartmentVo> voListForChildren = new ArrayList<>();
            //接下来封装这个大科室节点下的所有小科室
            //也就是DepartmentVo中children，这个children也是一个list集合
            for (Department childDeptment : curentDepList) {
                DepartmentVo childDepartmentVo = new DepartmentVo();
                childDepartmentVo.setDepcode(childDeptment.getDepcode());
                childDepartmentVo.setDepname(childDeptment.getDepname());
                voListForChildren.add(childDepartmentVo);
            }
            departmentVo.setChildren(voListForChildren);
            res.add(departmentVo);
        }

        return res;
    }

    @Override
    public String getDepName(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (Objects.isNull(department)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        return department.getDepname();
    }

    @Override
    public Department getDepartment(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (Objects.isNull(department)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        return department;
    }


}
