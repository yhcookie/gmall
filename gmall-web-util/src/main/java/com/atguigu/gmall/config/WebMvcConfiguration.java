package com.atguigu.gmall.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter{

    @Autowired
    AuthInterceptor authInterceptor;
    //AuthInterceptor只是配置拦截器，这里才是让拦截器生效的。
    public void addInterceptors(InterceptorRegistry registry) {
        //配置拦截所有
        registry.addInterceptor(authInterceptor).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
