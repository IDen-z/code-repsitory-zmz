package com.zmz.yygh.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zmzyygh.model.order.OrderInfo;

public interface OrderService extends IService<OrderInfo> {

    /**
    *  保存订单
    */
    OrderInfo saveOrder(String scheduleId, Long patientId);


}
