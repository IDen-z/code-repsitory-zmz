package com.zmz.springcloud.service;

import com.zmz.springcloud.entity.Payment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {

    int creat(Payment payment);

    Payment getPaymentById(@Param("id") Long id);
}
