package com.novent.foodordering.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.novent.foodordering.service.LoginService;
import com.novent.foodordering.util.Login;
import com.novent.foodordering.util.ResponseObject;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class LoginController {
	
	@Autowired
	private LoginService loginService;
	
	@RequestMapping(method = RequestMethod.POST, value="/login")
	public ResponseObject login(@RequestBody Login login, HttpServletRequest request) {
		return loginService.login(login, request);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/login/login")
	public String login() {
		return "welcome to food cart login controller";
	}
//	
//	@RequestMapping(method = RequestMethod.POST, value="/admin/login")
//	public ResponseObject adminLogin(@RequestBody Login login) {
//		return loginService.adminLogin(login);
//	}
//	
//	@RequestMapping(method = RequestMethod.POST, value="/administrator/login")
//	public ResponseObject administratorLogin(@RequestBody Login login) {
//		return loginService.administratorLogin(login);
//	}
//	
//	@RequestMapping(method = RequestMethod.POST, value="/restaurant/login")
//	public ResponseObject restaurantLogin(@RequestBody Login login) {
//		return loginService.restaurantLogin(login);
//	}
}