package com.zmz.springcloud.controller;

import com.zmz.springcloud.entity.CommenResult;
import com.zmz.springcloud.entity.Payment;
import com.zmz.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
@Slf4j
public class PaymentController {

    @Value("${server.port}")
    private String serverPort;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private DiscoveryClient discoveryClient;


    @PostMapping("/payment/create")
    public CommenResult create(@RequestBody Payment payment) {
        int res = paymentService.creat(payment);
        log.info("**********插入结果:" + res);

        if (res > 0) {
            return new CommenResult(000000, "操作数据库成功,serverPort:"+serverPort, res);

        } else {
            return new CommenResult(000001, "操作数据库失败", null);
        }


    }

    @GetMapping("/payment/get/{id}")
    public CommenResult getPaymentById(@PathVariable("id")  Long id) {
        Payment payment = paymentService.getPaymentById(id);

        log.info("**********查询结果:" + payment);

        if (payment !=null) {
            return new CommenResult(000000, "查询成功,serverPort:"+serverPort, payment);

        } else {
            return new CommenResult(000001, "没有对应记录，查询"+id+"失败", null);
        }


    }

    @GetMapping("/payment/discovery")
    public Object discovery(){

        List<String> services = discoveryClient.getServices();
        for (String service:services) {
            log.info("===========element:"+service);
        }
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PROVIDER-PAYMENT");

        for (ServiceInstance instance:instances) {
            log.info(instance.getInstanceId()+"===="+instance.getHost()
                    +"===="+instance.getPort()
                    +"===="+instance.getUri());
        }
        return this.discoveryClient;

    }

    @GetMapping("/payment/feign/timeout")
    public String paymentTimeout(){

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return serverPort;


    }



}
