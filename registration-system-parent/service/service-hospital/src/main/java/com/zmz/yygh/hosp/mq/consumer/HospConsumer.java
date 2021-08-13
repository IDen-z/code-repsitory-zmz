package com.zmz.yygh.hosp.mq.consumer;

import com.zmz.yygh.hosp.api.service.ScheduleService;
import com.zmzyygh.constant.MqConst;
import com.zmzyygh.model.hosp.Schedule;
import com.zmzyygh.vo.msm.MsmVo;
import com.zmzyygh.vo.order.OrderMqVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
* @Description: 即使消费者也是生产者
* @Author: Zhu Mengze
* @Date: 2021/8/13 15:20
*/
@Component
@EnableBinding(HospSink.class)
public class HospConsumer {


    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private HospMqService hospMqService;


    @StreamListener(MqConst.QUEUE_ORDER)
    public void receiver(Message<OrderMqVo> orderMqVoMessage) throws IOException {
        OrderMqVo orderMqVo = orderMqVoMessage.getPayload();
        //下单成功更新预约数
        Schedule schedule = scheduleService.getById(orderMqVo.getScheduleId());
        schedule.setReservedNumber(orderMqVo.getReservedNumber());
        schedule.setAvailableNumber(orderMqVo.getAvailableNumber());
        scheduleService.update(schedule);
        //发送短信
        MsmVo msmVo = orderMqVo.getMsmVo();
        if (null != msmVo) {
            boolean sendRes = hospMqService.sendMsmMq(msmVo);
        }
    }
}
