package com.novent.foodordering.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.novent.foodordering.service.LogoutService;
import com.novent.foodordering.util.ResponseObject;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class LogoutController {

	@Autowired
	private LogoutService logoutService;
	
	@RequestMapping(method = RequestMethod.POST, value ="/logout")
	public ResponseObject logout(HttpServletRequest request) {
		return logoutService.logout(request);
	}
	
//	@RequestMapping(method = RequestMethod.POST, value ="/admin/logout")
//	public ResponseObject adminLogout(HttpSession httpSession, @RequestBody Id id) {
//		return logoutService.adminLogout(httpSession, id);
//	}
//	
//	@RequestMapping(method = RequestMethod.POST, value ="/administrator/logout")
//	public ResponseObject administratorLogout(HttpSession httpSession, @RequestBody Id id) {
//		return logoutService.administratorLogout(httpSession, id);
//	}
//	
//	@RequestMapping(method = RequestMethod.POST, value ="/restaurant/logout")
//	public ResponseObject restaurantLogout(HttpSession httpSession, @RequestBody Id id) {
//		return logoutService.restaurantLogout(httpSession, id);
//	}
}