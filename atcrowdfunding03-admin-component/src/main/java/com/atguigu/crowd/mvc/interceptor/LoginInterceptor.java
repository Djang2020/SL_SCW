package com.atguigu.crowd.mvc.interceptor;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.exception.AccessForbiddenException;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Django
 * @date 2022-02-22 18:55
 * @DESC: 登录拦截器
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    //如果实现HandlerInterceptor接口，就得实现3个方法，但是这里没必要，因为只需要用到第一个，所以就继承HandlerInterceptorAdapter
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //1.想通过request对象获取Session对象
        HttpSession session = httpServletRequest.getSession();
        //2.尝试从session域中获取admin对象
        if (!StringUtils.isEmpty(session)) {
            Admin admin = (Admin) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN);
            //3.判断admin是否为空
            if (StringUtils.isEmpty(admin)) {
                //未登录状态，抛出异常
                throw new AccessForbiddenException(CrowdConstant.MESSAGE_ACCESS_FORBIDEN);
            } else {
                //未抛异常就返回true
                return true;
            }
        } else {
            return false;
        }

    }

}
