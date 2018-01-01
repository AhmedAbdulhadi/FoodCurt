package com.novent.foodordering.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.novent.foodordering.util.Login;
import com.novent.foodordering.util.ResponseObject;

@Service
public interface LoginService {
	
	public ResponseObject login(Login login, HttpServletRequest request);
	
//	public ResponseObject adminLogin(Login login);
//	
//	public ResponseObject administratorLogin(Login login);
//	
//	public ResponseObject restaurantLogin(Login login);

}
