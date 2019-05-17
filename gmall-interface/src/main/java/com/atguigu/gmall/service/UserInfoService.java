package com.atguigu.gmall.service;

import com.atguigu.gmall.UserAddress;
import com.atguigu.gmall.UserInfo;

import java.util.List;

public interface UserInfoService {
    List<UserInfo> findAll();

    public List<UserAddress> getUserAddressList(String userId);
    //登录
    UserInfo login(UserInfo userInfo);
    //认证
    UserInfo verify(String userId);
}
