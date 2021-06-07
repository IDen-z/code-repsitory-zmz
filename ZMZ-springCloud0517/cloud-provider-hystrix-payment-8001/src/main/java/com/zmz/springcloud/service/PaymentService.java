package com.zmz.springcloud.service;


import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    public String paymentOK(Integer id){

        return "线程池==:"+ Thread.currentThread().getName()+"paymentOK ,id"+id+"哈哈！";

    }


    public String paymentTimeOut(Integer id){

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return "线程池==:"+ Thread.currentThread().getName()+"paymentTimeOut ,id"+id+"失败！";

    }

}
