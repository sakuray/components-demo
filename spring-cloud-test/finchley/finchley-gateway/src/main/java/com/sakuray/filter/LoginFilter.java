package com.sakuray.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class LoginFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return FilterConstants.ROUTE_TYPE;
    }

    @Override
    public int filterOrder() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean shouldFilter() {
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        String servletPath = request.getServletPath();
        Object token = request.getSession().getAttribute("token");
        return servletPath != null && servletPath.contains("login") && token == null;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        Object response = context.get("zuulResponse");
        String loginResult = null;
        if(response != null) {
            if(response instanceof CloseableHttpResponse) {
                HttpEntity httpEntity = ((CloseableHttpResponse)response).getEntity();
                try {
                    loginResult = EntityUtils.toString(httpEntity);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(response instanceof ClientHttpResponse) {
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] tmp = new byte[128];
                    InputStream is = ((ClientHttpResponse) response).getBody();
                    int length = -1;
                    while((length = is.read(tmp)) != -1) {
                        baos.write(tmp, 0, length);
                    }
                    is.close();
                    loginResult = baos.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                throw new ZuulException("未知的类型", 0, "response unknow");
            }
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(200);
            // 抵消掉SendResponseFilter,不触发该方法
            context.set("zuulResponseHeaders", null);
            context.set("responseDataStream", null);
            context.set("responseBody", null);
            if(loginResult != null) {
                try {
                    context.getResponse().getWriter().write(loginResult);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(loginResult != null) {
            System.out.println("登陆成功"+loginResult);
            context.getRequest().getSession().setAttribute("token", loginResult);
        }
        return null;
    }
}
