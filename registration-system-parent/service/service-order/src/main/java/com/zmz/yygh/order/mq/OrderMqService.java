package com.zmz.yygh.order.mq;

import com.zmzyygh.vo.order.OrderMqVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@EnableBinding(OrderStream.class)
@Slf4j
public class OrderMqService {

    @Autowired
    private OrderStream orderStream;


    public boolean sendOrderMq(OrderMqVo orderMqVo) {

        boolean send = orderStream.outputOrder().send(MessageBuilder.withPayload(orderMqVo).build());
        log.info("==========订单mq发送结果为 {} ！===========", send);
        return send;

    }


}
