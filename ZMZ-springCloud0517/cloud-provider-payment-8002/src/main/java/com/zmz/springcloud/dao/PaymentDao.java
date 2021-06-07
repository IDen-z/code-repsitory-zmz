package com.zmz.springcloud.dao;

import com.zmz.springcloud.entity.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface PaymentDao {
    /*这里就是一些增删改查*/

    int creat(Payment payment);

    Payment getPaymentById(@Param("id") Long id);


}
