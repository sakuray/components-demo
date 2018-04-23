package com.demo.dark.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 判断是否登录
 */
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession httpsession = req.getSession();
        String url = req.getServletPath();
        System.out.println(url);
        if(url.equals("/activiti") || url.equals("/")|| url.equals("/login.jsp")) {
            chain.doFilter(request, response);
        } else {
            if(null != httpsession.getAttribute("user")) {
                chain.doFilter(request, response);
            } else {
                res.sendRedirect(req.getScheme()+"://"+req.getServerName()+":"+req.getServerPort()+req.getContextPath()+"/login.jsp");
            }
        }
    }

    @Override
    public void destroy() {
        
    }

}
