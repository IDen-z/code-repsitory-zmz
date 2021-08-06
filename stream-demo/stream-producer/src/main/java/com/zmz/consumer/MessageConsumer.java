package com.zmz.consumer;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(Sink.class)
public class MessageConsumer {

    @StreamListener(Sink.INPUT)
    public void receiveMes(String message) {
        System.err.println(Thread.currentThread().getName());
        System.err.println("消费者接受的message" + message);
        int i = 0;
        while (true){
            System.out.println("消费者： " + i++);
            if (i == 10) {
                break;
            }
        }


    }

//    @StreamListener("input")
//    public void receiveMesTest(String message) {
//        System.err.println(Thread.currentThread().getName());
//
//        System.out.println("消费者接受的message======" + message);
//        int j = 0;
//        while (true) {
//            System.out.println("消费者： " + j++);
//            if (j == 100) {
//                break;
//            }
//        }
//    }

}
