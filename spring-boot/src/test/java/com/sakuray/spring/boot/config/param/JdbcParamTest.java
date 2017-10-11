package com.sakuray.spring.boot.config.param;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.sakuray.spring.boot.SpringBootDemoTest;

public class JdbcParamTest extends SpringBootDemoTest {

	@Autowired
	private JdbcParam jdbcParam;
	
	@Test
	public void test() {
		System.out.println(jdbcParam);
	}
}
