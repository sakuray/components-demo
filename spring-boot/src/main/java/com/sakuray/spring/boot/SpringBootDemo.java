package com.sakuray.spring.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringBootDemo {
	
	private static final Logger log = LoggerFactory.getLogger(SpringBootDemo.class);
	
	public static void main(String[] args) {
		log.info("SpringBootDemo正在启动...");
		SpringApplication.run(SpringBootDemo.class, args);
		log.info("SpringBootDemo启动完成!");
	}

}
