package com.demo.dark.identity;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.demo.dark.base.BaseJunit;

public class IdentityTest extends BaseJunit{
    
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private IdentityService identityService;
    
    @Test
    @Rollback(value=false)
    public void addUser() {
        String userId = "admin";
        User user = identityService.newUser(userId);
        user.setPassword("admin");
        identityService.saveUser(user);
        logger.debug("保存用户" + user.getId()+"成功，密码是："+ user.getPassword());
    }
}
