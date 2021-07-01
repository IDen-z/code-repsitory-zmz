package com.zmz.yygh.hosp.api.repository;


import com.zmzyygh.model.hosp.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
* @Description: 这个repository主要是用来操作mongodb的
* @Author: Zhu Mengze
* @Date: 2021/7/1 14:17
*/
@Repository
public interface DepartmentRepository extends MongoRepository<Department,String> {


    Department getDepartmentByHoscodeAndDepcode(String hoscode, String depcode);

}
