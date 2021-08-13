package com.zmz.yygh.order.mq;

import com.zmzyygh.constant.MqConst;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface OrderStream {
    /**
    *  下单 mq通知
    */
    @Output(MqConst.QUEUE_ORDER)
    MessageChannel outputOrder();


}
