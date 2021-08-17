package com.zmz.yygh.order.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zmz.yygh.common.result.Result;
import com.zmz.yygh.common.util.AuthContextHolder;
import com.zmz.yygh.order.service.OrderService;
import com.zmzyygh.enums.OrderStatusEnum;
import com.zmzyygh.model.order.OrderInfo;
import com.zmzyygh.vo.order.OrderQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/order/orderInfo")
public class OrderApiController {

    @Autowired
    private OrderService orderService;


    /**
    * @Description: 创建订单接口
    * @Author: Zhu Mengze
    * @Date: 2021/8/11 9:18
    */
    @PostMapping("auth/submitOrder/{scheduleId}/{patientId}")
    public Result submitOrder(
            @PathVariable String scheduleId,
            @PathVariable Long patientId) {
        return Result.ok(orderService.saveOrder(scheduleId, patientId));
    }

    //根据订单id查询订单详情
    @GetMapping("auth/getOrders/{orderId}")
    public Result getOrders(@PathVariable String orderId) {
        OrderInfo orderInfo = orderService.getOrder(orderId);
        return Result.ok(orderInfo);
    }


    /**
    * @Description: 获取订单列表，条件查询加分页
    * @Author: Zhu Mengze
    * @Date: 2021/8/16 10:54
    */
    @GetMapping("auth/{page}/{limit}")
    public Result list(@PathVariable Long page,
                       @PathVariable Long limit,
                       OrderQueryVo orderQueryVo, HttpServletRequest request) {
        //设置当前用户id
        orderQueryVo.setUserId(AuthContextHolder.getUserId(request));
        IPage<OrderInfo> pageModel =
                orderService.selectPage(page,limit,orderQueryVo);
        return Result.ok(pageModel);
    }

    /**
    *  获取订单状态
    */
    @GetMapping("auth/getStatusList")
    public Result getStatusList() {
        return Result.ok(OrderStatusEnum.getStatusList());
    }






}
