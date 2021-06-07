package com.zmz.springcloud.service;

import com.zmz.springcloud.entity.CommenResult;
import com.zmz.springcloud.entity.Payment;
import feign.Param;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "CLOUD-PROVIDER-PAYMENT")
@EnableEurekaClient
public interface PaymentFeignService {

    //对应了8001提供端暴露的controller层的接口
    @GetMapping("/payment/get/{id}")
    public CommenResult getPaymentById(@PathVariable("id")  Long id) ;

    @GetMapping("/payment/feign/timeout")
    public String paymentTimeout();

}
