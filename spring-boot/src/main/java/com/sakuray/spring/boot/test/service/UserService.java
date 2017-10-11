package com.sakuray.spring.boot.test.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sakuray.spring.boot.test.dao.UserDao;
import com.sakuray.spring.boot.test.entity.User;

@Service
public class UserService {
	
	@Autowired
	private UserDao userDao;
	
	@Transactional(readOnly=true)
	public List<User> getAll() {
		return userDao.getAll();
	}
	
	@Transactional
	public int addOne(User user) {
		return userDao.addOne(user);
	}
}
