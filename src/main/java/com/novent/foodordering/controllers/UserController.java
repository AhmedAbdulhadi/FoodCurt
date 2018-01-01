package com.novent.foodordering.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.novent.foodordering.entity.Users;
import com.novent.foodordering.service.UserService;
import com.novent.foodordering.util.ResponseObject;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "*")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;
    
	@RequestMapping(method = RequestMethod.GET)
	public ResponseObject getUserByStatus(@RequestParam(value = "status", required=false, defaultValue="true") Boolean status, @RequestParam(value = "sort", required=false, defaultValue="Desc") String sort, HttpServletRequest request,
			                              @PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) Pageable pageable) {
		return userService.getUserByStatus(status, request, sort, pageable);
	}

	@RequestMapping(method = RequestMethod.GET, params = "all")
	public ResponseObject getAllUsers(@RequestParam(value = "all", defaultValue ="true") Boolean all, @RequestParam(value = "sort", required=false, defaultValue ="Desc") String sort, HttpServletRequest request) {
		return userService.getAllUsers(request, sort);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{userId}")
	public ResponseObject getUserById(@PathVariable long userId,  HttpServletRequest request) {
		return userService.getUserById(userId, request);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseObject createUser(@RequestBody Users user) {
		return userService.createUser(user);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{userId}")
	public ResponseObject updateUser(@PathVariable long userId, @RequestBody Users user, HttpServletRequest request) {
			return userService.updateUser(userId, user, request);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{userId}")
	public ResponseObject deleteUser(@PathVariable long userId, HttpServletRequest request) {
	    	return userService.deleteUser(userId, request);
	}
}
