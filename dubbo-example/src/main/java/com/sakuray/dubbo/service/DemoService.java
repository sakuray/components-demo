package com.sakuray.dubbo.service;

import java.util.List;

import com.sakuray.dubbo.entity.User;

public interface DemoService {
	
	public String greet(String name);
	public List<User> getUsers();
	
}
