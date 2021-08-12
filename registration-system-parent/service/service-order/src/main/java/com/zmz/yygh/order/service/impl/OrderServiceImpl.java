package com.zmz.yygh.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmz.yygh.order.mapper.OrderInfoMapper;
import com.zmz.yygh.order.service.OrderService;
import com.zmzyygh.model.order.OrderInfo;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderService {

    /**
    *  保存订单
    */
    @Override
    public Long saveOrder(String scheduleId, Long patientId) {


        return null;
    }
}
