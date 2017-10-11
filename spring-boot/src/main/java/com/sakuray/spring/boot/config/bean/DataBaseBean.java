package com.sakuray.spring.boot.config.bean;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.sakuray.spring.boot.config.param.JdbcParam;

@Configuration
public class DataBaseBean {

	@Autowired
	private JdbcParam jdbcParam;
	
	@Bean
	public DataSource dataSource() {
		BasicDataSource source = new BasicDataSource();
		source.setDriverClassName(jdbcParam.getDriver());
		source.setUrl(jdbcParam.getUrl());
		source.setUsername(jdbcParam.getUsername());
		source.setPassword(jdbcParam.getPassword());
		return source;
	}
	
	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		return template;
	}
	
	@Bean
	public PlatformTransactionManager transactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
}
