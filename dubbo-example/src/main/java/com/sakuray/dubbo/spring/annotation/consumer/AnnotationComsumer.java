package com.sakuray.dubbo.spring.annotation.consumer;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.sakuray.dubbo.service.DemoService;

@Service
public class AnnotationComsumer {

	@Reference(check=false)
	private DemoService demoService;
	
	public void print() {
        System.out.println(demoService.greet("张三"));
        System.out.println(demoService.getUsers());
    }

}
