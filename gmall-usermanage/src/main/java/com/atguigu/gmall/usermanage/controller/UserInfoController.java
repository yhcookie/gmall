package com.atguigu.gmall.usermanage.controller;

import com.atguigu.gmall.UserInfo;
import com.atguigu.gmall.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping("/findAll")
    @ResponseBody //返回json字符串 将数据显示在页面上
    public List<UserInfo> findAll(){
        return userInfoService.findAll();
    }
}
