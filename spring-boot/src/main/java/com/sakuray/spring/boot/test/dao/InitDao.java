package com.sakuray.spring.boot.test.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class InitDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Transactional
	public void createUserTable() {
		jdbcTemplate.execute("create table user(id int primary key, name varchar(255))");
	}
}
