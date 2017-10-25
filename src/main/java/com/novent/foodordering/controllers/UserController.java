package com.novent.foodordering.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.novent.foodordering.entity.Users;
import com.novent.foodordering.service.UserService;
import com.novent.foodordering.util.ResponseObject;

@RestController
@RequestMapping("api/v1/myrestaurant/user")
@CrossOrigin(origins = "*")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseObject getAllUser() {
		return userService.getAllUser();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{userId}")
	public ResponseObject getUserById(@PathVariable long userId) {
		return userService.getUserById(userId);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseObject createUser(@RequestBody Users user) {
		return userService.createUser(user);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{userId}")
	public ResponseObject updateUser(@RequestBody Users user, @PathVariable long userId) {
		return userService.updateUser(userId, user);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{userId}")
	public ResponseObject deleteUser(@PathVariable long userId) {
		return userService.deleteUser(userId);
	}

}
