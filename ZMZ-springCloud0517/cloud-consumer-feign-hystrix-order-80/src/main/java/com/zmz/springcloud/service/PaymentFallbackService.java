package com.zmz.springcloud.service;

import org.springframework.stereotype.Component;

@Component
public class PaymentFallbackService implements PaymentHystrixService{

    @Override
    public String paymentOK(Integer id) {
        return "fallback of PaymentHystrixService=============>paymentOK";
    }

    @Override
    public String paymentTimeOut(Integer id) {
        return "fallback of PaymentHystrixService=============>paymentTimeOut";
    }
}
