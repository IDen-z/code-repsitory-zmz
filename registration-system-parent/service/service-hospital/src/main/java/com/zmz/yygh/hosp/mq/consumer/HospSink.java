package com.zmz.yygh.hosp.mq.consumer;

import com.zmzyygh.constant.MqConst;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface HospSink {

    @Input(MqConst.QUEUE_ORDER)
    SubscribableChannel hospReceive();

}
