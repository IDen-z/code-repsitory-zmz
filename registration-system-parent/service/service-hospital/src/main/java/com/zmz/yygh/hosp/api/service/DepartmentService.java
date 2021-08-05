package com.zmz.yygh.hosp.api.service;

import com.zmzyygh.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;
import com.zmzyygh.model.hosp.Department;
import com.zmzyygh.vo.hosp.DepartmentQueryVo;

import java.util.List;
import java.util.Map;

public interface DepartmentService {
    void saveDeptment(Map<String, Object> paramMap);

    Page<Department> findPageDepartment(Integer page, Integer limit, DepartmentQueryVo departmentQueryVo);

    void removeDepartment(String hoscode, String depcode);

    List<DepartmentVo> findDeptTree(String hoscode);

    String getDepName(String hoscode, String depcode);

    Department getDepartment(String hoscode, String depcode);
}
