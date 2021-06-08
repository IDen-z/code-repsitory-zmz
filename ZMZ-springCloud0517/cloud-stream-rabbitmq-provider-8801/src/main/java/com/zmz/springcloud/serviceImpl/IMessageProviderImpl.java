package com.zmz.springcloud.serviceImpl;

import com.zmz.springcloud.service.IMessageProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import java.util.UUID;


@EnableBinding(Source.class)//这个source就是定义消息的推送管道
@Slf4j
public class IMessageProviderImpl implements IMessageProvider {

    @Autowired
    private MessageChannel output;
    //注入接口和注入MessageChannel的区别在于发送时需不需要调用接口内的方法

    @Override
    public String send() {
        String uuid = UUID.randomUUID().toString();
        output.send(MessageBuilder.withPayload(uuid).build());
        log.info("+++++++++++++++++uuid+"+uuid);
        return uuid;
    //假设注入了MessageChannel messageChannel; 因为绑定的是Source这个接口，
    //所以会使用其中的唯一产生MessageChannel的方法，那么下边的代码会是
    //messageChannel.send(MessageBuilder.withPayload("Message from MyPipe").build());


    }
}
