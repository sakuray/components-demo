package com.sakuray.spring.boot.config.bean;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sakuray.spring.boot.common.filter.LoginFilter;

@Configuration
public class FilterBean {

	@Bean
	public FilterRegistrationBean login() {
		FilterRegistrationBean bean = new FilterRegistrationBean();
		bean.setFilter(new LoginFilter());
		bean.addUrlPatterns("/*");
		bean.setName("login");
		return bean;
	}
}
