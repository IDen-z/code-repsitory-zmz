package com.zmz.producer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@EnableBinding(Source.class)
public class MessageProducer {

    @Autowired
    private Source source;

    @Autowired
    private BinderAwareChannelResolver resolver;

    /**
     * Author: Mengze Zhu
     * Description:
     * Param:
     * Return:
     */
    @GetMapping("/test")
    public String sendMessage(String message){
        /*source.output方法会拿到一个messageChannel对象，
        调用这个对象下的send方法，然后send会将包装好的message发送到队列中去
        这个队列又是和你的交换机是绑定在一起的
        */
        System.err.println(Thread.currentThread().getName());
        source.output().send(MessageBuilder.withPayload(message).build());
        int i = 0;
        while (true){
            System.out.println("生产者： " + i++);
            if (i == 10) {
                break;
            }
        }
        return "111";
    }

    public void sendMessageTest(String message){
        System.err.println(Thread.currentThread().getName());

        resolver.resolveDestination("input").send(new GenericMessage<>(message));
        int i = 0;
        while (true){
            System.out.println("生产者： " + i++);
            if (i == 100) {
                break;
            }
        }

    }

}
