package com.zmz.yygh.msm.mq;

import com.zmzyygh.constant.MqConst;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface SmsSink {

    @Input(MqConst.QUEUE_MSM_ITEM)
    SubscribableChannel smsReceive();

}
