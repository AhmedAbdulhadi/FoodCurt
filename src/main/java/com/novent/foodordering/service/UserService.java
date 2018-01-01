package com.novent.foodordering.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.novent.foodordering.entity.Users;
import com.novent.foodordering.util.ResponseObject;

@Service
public interface UserService {
	
	public ResponseObject getUserByStatus(boolean status, HttpServletRequest request, String sort, Pageable pageable);
	
	public ResponseObject getAllUsers(HttpServletRequest request, String sort);
	
	public ResponseObject getUserById(long userId, HttpServletRequest request);
	
	public ResponseObject createUser(Users user);
	
	public ResponseObject updateUser(long userId, Users user, HttpServletRequest request);
	
	public ResponseObject deleteUser(long userId, HttpServletRequest request);
}
