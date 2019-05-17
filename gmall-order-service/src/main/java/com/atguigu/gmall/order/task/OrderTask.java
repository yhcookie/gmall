package com.atguigu.gmall.order.task;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.OrderInfo;
import com.atguigu.gmall.service.OrderService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@EnableScheduling
@Component
public class OrderTask {

    @Reference
    private OrderService orderService;
    //设置定时扫描 ----【秒】 分 时 日 月 周 年
    //这个是每分钟的第五秒
//    @Scheduled(cron = "5 * * * * ?")
//    public void sayHello(){
//        System.out.println(Thread.currentThread().getName()+"===currentThread01");
//    }


    //关闭过期订单？什么是过期订单？
    //及时解锁啊 要不然一直锁着库存怎么行啊
    // 定时关闭过期的订单 -- 关闭订单7天！ -- 根据下单的成功率！
    @Scheduled(cron = "0/30 * * * * ?")
    public  void checkOrder(){
        // 关闭过期订单？什么是过期的订单 根据过期时间与当前时间，状态是未付款。
        List<OrderInfo> expiredOrderList =  orderService.getExpiredOrderList();
        // 循环遍历当前的集合
        for (OrderInfo orderInfo : expiredOrderList) {
            // 处理过期的每个订单
            orderService.execExpiredOrder(orderInfo);
        }
    }
}
