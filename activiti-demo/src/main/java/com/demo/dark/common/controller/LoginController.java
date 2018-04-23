package com.demo.dark.common.controller;

import javax.servlet.http.HttpSession;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.dark.common.dto.Result;

@Controller
@RequestMapping(value = "login")
public class LoginController {
	
	@Autowired
	private IdentityService identityService;
	
	@RequestMapping(value = "in")
	@ResponseBody
	public Result loginIn(@RequestParam(value="userId")String userId,
			@RequestParam(value="password")String password,HttpSession httpSession) {
		boolean exits = identityService.checkPassword(userId, password);
		if(exits) {
			User user = identityService.createUserQuery().userId(userId).singleResult();
			httpSession.setAttribute("user", user);
		} else {
		    System.out.println("密码或用户名不正确，登录失败"+ userId + ":" +password);
		    return new Result(0,"","");
		}
		System.out.println("登录成功");
		return new Result(1, "", "");
	}
	
	@RequestMapping(value="skip")
	public String skipToMain(HttpSession httpSession,Model model) {
	    return "Main";
	    /*User user = (User) httpSession.getAttribute("user");
	    if(user != null) {
	        return "Main";
	    } else {
	        model.addAttribute("message", "你还没有登录");
	        return "error";
	    }*/
	}
	
	@RequestMapping(value = "out")
	@ResponseBody
	public Result loginOut(HttpSession httpSession) {
		httpSession.removeAttribute("user");
		return new Result(1,"退出成功","");
	}
}
