package com.atguigu.gmall.config;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.impl.Base64UrlCodec;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

@Component //实现继承被spring放到容器中这是很广的一个注解把
public class AuthInterceptor extends HandlerInterceptorAdapter{

    //进入控制器之前
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //将生成的token放到cookie里边
        String token = request.getParameter("newToken");
        if(token!=null){
            //不为空就把他放到cookie中？ 我们的url没有中文 所以Encoding给你个false吧
            CookieUtil.setCookie(request,response,"token",token,WebConst.COOKIE_MAXAGE,false);

        }
        //直接访问登录界面，或者当用户进入其他项目模块中时 没有携带token 不懂后半句
        if(token==null){
            //如果用户登录了 再去访问其他页面的时候没携带newToken ，那么token可能已经在cookie中了
            token = CookieUtil.getCookieValue(request,"token",false);
        }
        //这个不为空是判断的上个里边赋的值。和之前那个不为空不一样
        if (token!=null){
            //取token中的有效数据，解密
            Map map = getUserMapByToken(token);
            //把这个名字拿出来显示到页面上
            String nickName = (String) map.get("nickName");
            request.setAttribute("nickName",nickName);
        }
        //object handler
        //可以获取过的的那个方法，并能获取到它的注解一系列
        HandlerMethod handlerMethod = (HandlerMethod)handler;
        LoginRequire methodAnnotation = handlerMethod.getMethodAnnotation(LoginRequire.class);
        //不为空就是有注解
        if(methodAnnotation!=null){
            //不判断它是true还是false了直接让他登录
            String remoteAddr = request.getHeader("X-forwarded-for");
            //认证控制器在passport项目，所以要远程调用 怎么远程调用呢？这里用HttpClientUtil
            String result = HttpClientUtil.doGet(WebConst.VERIFY_ADDRESS + "?token=" + token + "&currentIp=" + remoteAddr);
            if ("success".equals(result)){
                //认证成功 做什么呢？说明当前用户已经登录了，
                //把他存到request域中吧 页面会用到的
                Map map = getUserMapByToken(token);
                String userId = (String) map.get("userId");
                request.setAttribute("userId",userId);
                return true;
            }else{
                //这是认证失败 重新让他登录。
                if (methodAnnotation.autoRedirect()){
                    // 认证失败！重新登录！
                    /*http://passport.atguigu.com/index?originUrl=http%3A%2F%2Fitem.gmall.com%2F32.html*/
                    String requestURL  = request.getRequestURL().toString(); // http://item.gmall.com/28.html
                    // 进行加密编码
                    String encodeURL = URLEncoder.encode(requestURL, "UTF-8");
                    response.sendRedirect(WebConst.LOGIN_ADDRESS+"?originUrl="+encodeURL);
                    return false;
                }

            }
        }
        return true;
    }

    //解密方法
    private Map getUserMapByToken(String token) {
        //三段的token 解出map段 eyJhbGciOiJIUzI1NiJ9.eyJuaWNrTmFtZSI6Im1hcnJ5IiwidXNlcklkIjoiMTAwMSJ9.TF1RTg_1TnkPNOAkA4Gq549iqwzsBplgeabpHvW15ng
        String tokenUserInfo = StringUtils.substringBetween(token, ".");
        //使用了另一个类 base64
        Base64UrlCodec base64UrlCodec = new Base64UrlCodec();
        byte[] bytes = base64UrlCodec.decode(tokenUserInfo);
        //数组转字符串再转map
        String str = null;
        try {
           str =  new String(bytes,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //将字符串转成map
        Map map = JSON.parseObject(str, Map.class);
        return map;
    }

    //进入控制器之后 视图渲染之前
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }
    //视图渲染之后
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
