package com.zmz.springcloud.controller;


import com.zmz.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/payment/hystrix/ok/{id}")
    public String paymentOK(@PathVariable("id") Integer id){

        log.info("res========"+paymentService.paymentOK(id));
        return paymentService.paymentOK(id);
    }


    @GetMapping("/payment/hystrix/timeout/{id}")
    public String paymentTimeOut(@PathVariable("id") Integer id){

        log.info("res========"+paymentService.paymentTimeOut(id));
        return paymentService.paymentTimeOut(id);
    }

}
