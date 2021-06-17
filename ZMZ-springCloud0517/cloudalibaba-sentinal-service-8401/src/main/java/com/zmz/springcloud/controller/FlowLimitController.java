package com.zmz.springcloud.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FlowLimitController {


    @GetMapping("/testA")
    public String testA() {
        /*两种模式QPS和线程数，QPS是每秒的请求数量，而线程有点类似银行柜员，可以放请求进来，但是办公人员有限*/
        /*try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        return "------testA";
    }

    @GetMapping("/testB")
    public String testB() {
        return "------testB";
    }


}
