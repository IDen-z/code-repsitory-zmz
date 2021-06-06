package com.zmz.producer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(Source.class)
public class MessageProducer {

    @Autowired
    private Source source;

    /**
     * Author: Mengze Zhu
     * Description:
     * Param:
     * Return:
     */
    public void sendMessage(String message){
        /*source.output方法会拿到一个messageChannel对象，
        调用这个对象下的send方法，然后send会将包装好的message发送到队列中去
        这个队列又是和你的交换机是绑定在一起的
        */
        source.output().send(MessageBuilder.withPayload(message).build());

    }

}
