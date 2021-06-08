package com.zmz.springcloud.service;


import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class PaymentService {

    public String paymentOK(Integer id) {

        return "线程池==:" + Thread.currentThread().getName() + "paymentOK ,id" + id + "哈哈！";

    }

    @HystrixCommand(fallbackMethod = "paymentTimeOutHandler", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "100")
    })
    public String paymentTimeOut(Integer id) {
//        int i= 10/0;
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return "线程池==:" + Thread.currentThread().getName() + "paymentTimeOut ,id" + id + "耗时三秒！";

    }

    public String paymentTimeOutHandler(Integer id) {
        return "线程池==:" + Thread.currentThread().getName() + "paymentTimeOut ,id" + id + "系统此时繁忙。我是兜底方法";
    }

    //    ==========下面是服务熔断，上面是服务降级
    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),//是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),//请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),//时间范围（经过多久恢复一次尝试）
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"),//失败率达到多少后跳闸
    })
    public String paymentCircuitBreaker(@PathVariable("id") Integer id) {
        if (id < 0) {
            //给负数就抛一个异常
            throw new RuntimeException("******id 不能负数");
        }
        //利用hutool的jar包，生成一个唯一的流水号
        String serialNumber = IdUtil.simpleUUID();

        return Thread.currentThread().getName() + "\t" + "调用成功，流水号: " + serialNumber;
    }

    public String paymentCircuitBreaker_fallback(@PathVariable("id") Integer id) {
        return "id 不能负数，请稍后再试，/(ㄒoㄒ)/~~   id: " + id;
    }


}
