package com.sakuray.dubbo.spring.annotation;

import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AProvider {

	public static void main(String[] args) throws IOException {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"annotation-provider.xml"});
		context.start();
		System.in.read();
	}
}
