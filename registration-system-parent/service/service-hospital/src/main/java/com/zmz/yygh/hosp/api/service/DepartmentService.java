package com.zmz.yygh.hosp.api.service;

import org.springframework.data.domain.Page;
import com.zmzyygh.model.hosp.Department;
import com.zmzyygh.vo.hosp.DepartmentQueryVo;

import java.util.Map;

public interface DepartmentService {
    void saveDeptment(Map<String, Object> paramMap);

    Page<Department> findPageDepartment(Integer page, Integer limit, DepartmentQueryVo departmentQueryVo);

    void removeDepartment(String hoscode, String depcode);
}
