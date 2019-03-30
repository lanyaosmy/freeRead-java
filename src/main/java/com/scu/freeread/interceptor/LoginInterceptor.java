package com.scu.freeread.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        log.info("---------------------开始进入请求地址拦截----------------------------");
        String uri=request.getRequestURI();
        log.info("url:"+uri);
        HttpSession session = request.getSession();
        session.setAttribute("userid",1);
//        Map<String,Object> urlMap=new HashMap<>();
//        urlMap.put("/user/getkey",1);
//        urlMap.put("/user/login",1);
//        urlMap.put("/user/register",1);
//        if(urlMap.containsKey(uri)){
//            return true;
//        }
//        HttpSession session = request.getSession();
//        Object userid= session.getAttribute("userid");
//        if(userid==null){
//            return false;
//        }
        return true;

    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

}
