package com.demo.dark.base;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

//@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:spring-app.xml"})
public class BaseSpringTransactional extends AbstractTransactionalJUnit4SpringContextTests{
    
    protected DataSource dataSource;

    @Autowired
    @Override
    public void setDataSource(DataSource dataSource) {
        super.setDataSource(dataSource);
        this.dataSource = dataSource;
    }
    
    
}
