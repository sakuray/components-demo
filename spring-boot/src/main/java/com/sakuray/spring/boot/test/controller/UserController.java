package com.sakuray.spring.boot.test.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sakuray.spring.boot.test.entity.User;
import com.sakuray.spring.boot.test.service.UserService;

@RestController
@RequestMapping(value="user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping(value="getAll")
	public List<User> getAll() {
		return userService.getAll();
	}
	
	@GetMapping(value="add")
	public int addOne(@RequestParam(name="id", required=true)int id,
			@RequestParam(name="name", required=true)String name) {
		return userService.addOne(new User(id, name));
	}
}
