package com.zmz.yygh.msm.mq;


import com.zmz.yygh.msm.service.MsmService;
import com.zmzyygh.constant.MqConst;
import com.zmzyygh.vo.msm.MsmVo;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@EnableBinding(SmsSink.class)
public class SmsConsumer {

    @Resource
    private MsmService msmService;

    @StreamListener(MqConst.QUEUE_MSM_ITEM)
    public void send(Message<MsmVo> msmVoMessage) {
        MsmVo msmVo = msmVoMessage.getPayload();
        msmService.send(msmVo);
    }


}
