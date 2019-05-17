package com.atguigu.gmall.usermanage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.UserAddress;
import com.atguigu.gmall.UserInfo;
import com.atguigu.gmall.service.UserInfoService;
import com.atguigu.gmall.usermanage.mapper.UserAddressMapper;
import com.atguigu.gmall.usermanage.mapper.UserInfoMapper;
import com.atguigu.gmall.utill.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import redis.clients.jedis.Jedis;


import java.util.List;

@Service
public class UserInfoServiceImpl implements UserInfoService{

    public String userKey_prefix="user:";
    public String userinfoKey_suffix=":info";
    public int userKey_timeOut=60*60;

    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserAddressMapper userAddressMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Override
    public List<UserInfo> findAll() {
        return userInfoMapper.selectAll();
    }

    @Override
    public List<UserAddress> getUserAddressList(String userId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        return userAddressMapper.select(userAddress);
    }

    @Override
    public UserInfo login(UserInfo userInfo) {
        //这里边就是要检查这个userinfo和数据库里的userinfo是否一致吗？
        //页面来的是123  数据库了的是md5加密的
        String passwd = userInfo.getPasswd();
        String newPwd = DigestUtils.md5DigestAsHex(passwd.getBytes());
        userInfo.setPasswd(newPwd);//这里边的对象的123就成了md5加密的了
        UserInfo info = userInfoMapper.selectOne(userInfo);
        //登录成功之后就把用户信息存到redis中
        if(info!=null){ //查出来info不为空的时候 就是成功啦哈哈，因为信息不对的话 查不出来。
            Jedis jedis = redisUtil.getJedis();
            //jedisset key v  ，所以  先定义一下子 key和v
            String userKey = userKey_prefix + info.getId() + userinfoKey_suffix;

            //做存储
            jedis.setex(userKey, userKey_timeOut, JSON.toJSONString(info));//这里的value需要的是字符串类型的  我们要把info搞成字符串

            jedis.close();
            return info;//可以把这个返回去
        }

        return null;
    }

    @Override
    public UserInfo verify(String userId) {
        Jedis jedis = redisUtil.getJedis();
        //定义他那key
        String key = userKey_prefix + userId + userinfoKey_suffix;
        String userJson = jedis.get(key);
        //重新设置一下它的过期时间 其实就是延长了它的过期时间 expire是设置 不是延长。
        jedis.expire(key,userKey_timeOut);
        //判断他不为空
        if(userJson!=null && userJson.length()>0){
            //将字符串转换为对象
            UserInfo userInfo = JSON.parseObject(userJson, UserInfo.class);//这个方法写起来很有意思 我的老天。
            return userInfo;//把从redis中拿出来的对象信息返回去
        }
        return null;
    }


}
