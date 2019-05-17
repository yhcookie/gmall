package com.atguigu.gmall.config;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequire {
    //默认设置为为true  自定义这个东西是给方法标的  意思是只要你访问这个方法就要登录，
    //当然这么简单的一写是不能达到目的的，还要别的操作。
    boolean autoRedirect() default true;
}
