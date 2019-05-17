package com.atguigu.gmall.passport.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.UserInfo;
import com.atguigu.gmall.passport.util.JwtUtil;
import com.atguigu.gmall.service.UserInfoService;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class PassportController {

    @Value("token.key")
    private String key;
    @Reference
    UserInfoService userInfoService; //用到这个userInfoService
    @RequestMapping("index")
    public String index(HttpServletRequest request){
        String originUrl = request.getParameter("originUrl");
        //保存上一个页面的url 方便登陆后随便跳转
        request.setAttribute("originUrl",originUrl);
        return "index";
    }

    @RequestMapping("login")
    @ResponseBody //那边是异步请求啊？$.post
    public String login(HttpServletRequest request,UserInfo userInfo){//这个参数要和表单传过来的对应
        //用户名密码验证
        UserInfo loginUser = userInfoService.login(userInfo);
        //这个salt就是那个ip 获取的是linux服务器的
        String ip = request.getHeader("X-forwarded-for");

        if(loginUser!=null){ //登陆成功与失败都给页面一个交代
            //这里做token--用jwt  key map salt(盐怎么拿？盐是ip)
            HashMap<String, Object> map = new HashMap<>();
            map.put("userId",loginUser.getId());
            map.put("nickName",loginUser.getNickName());

            String token = JwtUtil.encode(key,map,ip);
            return token;
        }else{
            return "fail";
        }
    }
    @RequestMapping("verify")
    @ResponseBody
    public String verify(HttpServletRequest request){
        //取得token 并揭秘
        String token = request.getParameter("token");
        String salt = request.getHeader("X-forwarded-for");
        //返回的这个map就是包含了用户信息的 {userId=1001, nickName=admin}
        Map<String, Object> map = JwtUtil.decode(token, key, salt);
        //怎么验证呢 就是只用id去redis里边查一下子  不为空 就过了
        if(map!=null && map.size()>0){
            String userId = (String)map.get("userId");
            UserInfo userInfo = userInfoService.verify(userId);
            if(userInfo!=null){
                return "success";
            }else{
                return "fail";
            }
        }
        return "fail";
    }
}
