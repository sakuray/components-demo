package com.sakuray.dubbo.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.sakuray.dubbo.entity.User;
import com.sakuray.dubbo.service.DemoService;

public class DemoServiceImpl implements DemoService{

	@Override
	public String greet(String name) {
		return "Hello " + name;
	}

	@Override
	public List<User> getUsers() {
		List<User> list = new ArrayList<User>();
        
        User user1 = new User("张三",10,"男");
        User user2 = new User("李四",11,"女");
        User user3 = new User("王五",12,"男");
        
        list.add(user1);
        list.add(user2);
        list.add(user3);
        
        return list;
	}

}
