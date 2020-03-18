package com.lianghd.myblog.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 拦截 admin/*以下下页面
public class LoginInterceptor extends HandlerInterceptorAdapter {

    // 请求未到达之前进行拦截 pre
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        if(request.getSession().getAttribute("user") == null){
            response.sendRedirect("/admin");

            return false;
        }
        return true;
    }
}
