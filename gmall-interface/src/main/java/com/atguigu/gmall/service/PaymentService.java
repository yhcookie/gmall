package com.atguigu.gmall.service;

import com.atguigu.gmall.PaymentInfo;

public interface PaymentService {
    //保存
    void  savePaymentInfo(PaymentInfo paymentInfo);
    //通过对象查找paymentInfo
    PaymentInfo getpaymentInfo(PaymentInfo paymentInfo);
    //更新方法
    void updatePaymentInfo(PaymentInfo paymentInfoUpd, String out_trade_no);
    //这是发送支付结果的方法 发送 订单的状态，orderId success
    void sendPaymentResult(PaymentInfo paymentInfo,String result);
    //check支付结果
    boolean checkPayment(PaymentInfo paymentInfoQuery);

    // outTradeNo ： checkPayment(); delaySec:多少秒，checkCount：查询次数
    void sendDelayPaymentResult(String outTradeNo,int delaySec ,int checkCount);
    // 关闭支付交易记录信息
    void closePayment(String id);
}
