package com.zmz.springcloud.controller;


import com.zmz.springcloud.entity.CommenResult;
import com.zmz.springcloud.entity.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@Slf4j
public class OrderController {

    /*单机版写法*/
//    public static final String PAYMENT_URL="http://localhost:8001";
    /*集群版本*/
    public static final String PAYMENT_URL = "http://CLOUD-PROVIDER-PAYMENT";


    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/consumer/payment/create")
    public CommenResult<Payment> create(Payment payment) {
        log.info("消费者发起的创建请求开始了=========");
        return restTemplate.postForObject(PAYMENT_URL + "/payment/create", payment, CommenResult.class);

    }

    @GetMapping("/consumer/payment/get/{id}")
    public CommenResult<Payment> getPaymentById(@PathVariable("id") Long id) {
        log.info("消费者发起的查询请求开始了=========");
        return restTemplate.getForObject(PAYMENT_URL + "/payment/get/" + id, CommenResult.class);
    }

    @GetMapping("/consumer/payment/getForEntity/{id}")
    public CommenResult<Payment> getPayment(@PathVariable("id") Long id) {

        ResponseEntity<CommenResult> entity = restTemplate.getForEntity(PAYMENT_URL + "/payment/get/" + id, CommenResult.class);
        if (entity.getStatusCode().is2xxSuccessful()) {
            log.info("getForEntity方法的打印信息"+entity.toString());
            return entity.getBody();
        } else {
            return new CommenResult<Payment>(000001, "操作失败");
        }


    }


}
