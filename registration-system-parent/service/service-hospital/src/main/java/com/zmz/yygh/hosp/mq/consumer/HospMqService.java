package com.zmz.yygh.hosp.mq.consumer;

import com.zmzyygh.vo.msm.MsmVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@EnableBinding(HospSource.class)
@Slf4j
public class HospMqService {

    @Autowired
    private HospSource hospSource;

    public boolean sendMsmMq(MsmVo msmVo) {
        boolean send = hospSource.outputMsm().send(MessageBuilder.withPayload(msmVo).build());
        log.info("=========发送短信mq成功===========");
        return send;
    }


}
