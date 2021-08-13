package com.zmz.yygh.hosp.mq.consumer;

import com.zmzyygh.constant.MqConst;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface HospSource {

    /**
    *  发送短信mq
    */

    @Output(MqConst.QUEUE_MSM_ITEM)
    MessageChannel outputMsm();

}
