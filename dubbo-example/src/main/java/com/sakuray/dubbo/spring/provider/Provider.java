package com.sakuray.dubbo.spring.provider;

import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Provider {

	public static void main(String[] args) throws IOException {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"provider.xml"});
		context.start();
		System.in.read();
	}
}
