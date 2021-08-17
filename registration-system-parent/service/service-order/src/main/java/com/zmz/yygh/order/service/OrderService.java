package com.zmz.yygh.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zmzyygh.model.order.OrderInfo;
import com.zmzyygh.vo.order.OrderQueryVo;

public interface OrderService extends IService<OrderInfo> {

    /**
    *  保存订单
    */
    OrderInfo saveOrder(String scheduleId, Long patientId);


    IPage<OrderInfo> selectPage(Long page, Long limit, OrderQueryVo orderQueryVo);

    OrderInfo getOrder(String orderId);
}
