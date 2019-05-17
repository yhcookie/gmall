package com.atguigu.gmall.service;

import com.atguigu.gmall.OrderInfo;
import com.atguigu.gmall.enums.ProcessStatus;

import java.util.List;
import java.util.Map;

public interface OrderService {
    //保存订单
    public  String  saveOrder(OrderInfo orderInfo);

    // 生成流水号
    String getTradeNo(String userId);
    // 比较流水号
    boolean checkTradeCode(String userId,String tradeCodeNo);
    // 删除流水号
    void delTradeCode(String userId);
    // 验证库存
    boolean checkStock(String skuId, Integer skuNum);
    //通过orderId查询订单
    OrderInfo getOrderInfo(String orderId);
    //根据订单id修改订单的状态。
    void updateOrderStatus(String orderId, ProcessStatus paid);
    //根据订单id发送订单的状态
    void sendOrderStatus(String orderId);
    //查询过期的订单
    List<OrderInfo> getExpiredOrderList();
    //处理每个订单
    void execExpiredOrder(OrderInfo orderInfo);
    //做拆单。
    List<OrderInfo> splitOrder(String orderId, String wareSkuMap);
    //将orderinfo转换成map
    Map initWareOrder(OrderInfo orderInfo);
}
