package com.shsxt.crm.interceptors;

import com.shsxt.crm.exceptions.NoLoginException;
import com.shsxt.crm.service.UserService;
import com.shsxt.crm.utils.LoginUserUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2019/12/4.
 */
public class NoAccessInterceptor extends HandlerInterceptorAdapter {
    @Resource
    private UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*
        * 获取cookie用户id值
        * userId==null||null==userService.queryById  \未登录 重定向index页面
        * */
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
       /* if (null==userId || null==userService.queryById(userId)){
            //用户不存在,重定向到登录页
            response.sendRedirect(request.getContextPath()+"/index");
            return false;
        }*/
        if (null==userId || null==userService.queryById(userId)){
            //用户不存在,抛出未登录异常,给全局异常捕获
           throw   new NoLoginException();
        }
        return true;
    }
}
