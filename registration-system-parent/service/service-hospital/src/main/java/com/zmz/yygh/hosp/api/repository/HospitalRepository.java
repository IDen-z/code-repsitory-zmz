package com.zmz.yygh.hosp.api.repository;

import com.zmzyygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends MongoRepository<Hospital,String> {

    //判断是否存在数据，只要满足spring data规范的话，不需要写这个
    //实现类，有点类似驼峰命名自动查找(find get read)
    Hospital getHospitalByHoscode(String hoscode);
}
