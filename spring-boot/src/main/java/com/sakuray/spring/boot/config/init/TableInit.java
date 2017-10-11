package com.sakuray.spring.boot.config.init;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sakuray.spring.boot.test.dao.InitDao;

@Component
public class TableInit {
	
	private static final Logger log = LoggerFactory.getLogger(TableInit.class);

	@Autowired
	private InitDao initDao;
	
	@PostConstruct
	public void initTable() {
		initDao.createUserTable();
		log.info("初始化内存数据库表结构成功...");
	}
}
