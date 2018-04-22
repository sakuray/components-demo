package com.sakuray.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthFilter extends ZuulFilter {

    private static final Log log = LogFactory.getLog(AuthFilter.class);

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String servletPath = request.getServletPath();
        Object token = request.getSession().getAttribute("token");
        if(servletPath != null && servletPath.contains("login")) {
            if(token != null) {
                context.setSendZuulResponse(false);
                HttpServletResponse response = context.getResponse();
                response.setStatus(200);
                try {
                    response.getWriter().write("you login already");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else { // 需登陆接口
            if(token == null) {
                context.setSendZuulResponse(false);
                HttpServletResponse response = context.getResponse();
                response.setStatus(403);
                try {
                    response.getWriter().write("token is null");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
