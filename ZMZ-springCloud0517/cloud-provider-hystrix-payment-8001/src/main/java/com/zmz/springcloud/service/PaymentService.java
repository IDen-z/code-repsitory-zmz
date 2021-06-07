package com.zmz.springcloud.service;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    public String paymentOK(Integer id){

        return "线程池==:"+ Thread.currentThread().getName()+"paymentOK ,id"+id+"哈哈！";

    }

    @HystrixCommand(fallbackMethod = "paymentTimeOutHandler",commandProperties = {
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="100")
    })
    public String paymentTimeOut(Integer id){
//        int i= 10/0;
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return "线程池==:"+ Thread.currentThread().getName()+"paymentTimeOut ,id"+id+"耗时三秒！";

    }

    public String paymentTimeOutHandler(Integer id){
        return "线程池==:"+ Thread.currentThread().getName()+"paymentTimeOut ,id"+id+"系统此时繁忙。我是兜底方法";
    }

}
