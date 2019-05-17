package com.atguigu.gmall.utill;
/*
RedisConfig负责在spring容器启动时自动注入，
而RedisUtil就是被注入的工具类以供其他模块调用*/

import jdk.nashorn.internal.ir.IfNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {

    //读取配置文件中的redis的ip地址
    @Value("${spring.redis.host:disabled}")
    private String host;
    @Value("${spring.redis.port:0}")
    public int port;
    @Value("${spring.redis.database:0}")
    private int database;

    @Bean
    public RedisUtil getRedisUtil(){
        //不想启用这个的时候 就给host赋值disabled
        if (host.equals("disabled")){
            return null;
        }
        RedisUtil redisUtil = new RedisUtil();
        redisUtil.initJedisPool(host,port, database);
        return redisUtil;
    }
}
