package com.novent.foodordering.service;

import org.springframework.stereotype.Service;

import com.novent.foodordering.entity.Users;
import com.novent.foodordering.util.ResponseObject;

@Service
public interface UserService {
	
	public ResponseObject getAllUser();
	
	public ResponseObject getUserById(long userId);
	
	public ResponseObject createUser(Users user);
	
	public ResponseObject updateUser(long userId,Users user);
	
	public ResponseObject deleteUser(long userId);

}
