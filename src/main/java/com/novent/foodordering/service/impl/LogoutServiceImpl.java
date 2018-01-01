package com.novent.foodordering.service.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.novent.foodordering.constatnt.ResponseCode;
import com.novent.foodordering.constatnt.ResponseMessage;
import com.novent.foodordering.constatnt.ResponseStatus;
import com.novent.foodordering.dao.LoginDao;
import com.novent.foodordering.dao.UserDao;
import com.novent.foodordering.entity.Logins;
import com.novent.foodordering.entity.Users;
import com.novent.foodordering.service.LogoutService;
import com.novent.foodordering.util.ResponseObject;

@Service
@Component
public class LogoutServiceImpl implements LogoutService{

	@Autowired
	private UserDao userDao;
	@Autowired
	private LoginDao loginDao;
	
	@Override
	public ResponseObject logout(HttpServletRequest request) {
			ResponseObject response = null;
			String token = request.getHeader("token");
			HttpSession session = request.getSession();
			boolean validToken = token != null; 
							
			// if the token exist
			if(validToken){
				Logins login = loginDao.findByToken(token);
				Users user = null;
				if(login != null){
					 user = userDao.findByUserName(login.getUserName());
				}
				if(user.getPassword().equals(login.getPassword())){
					if(user.getIsLoggedIn()){
						user.setIsLoggedIn(false);
						session.removeAttribute(user.getUserName());
						userDao.save(user);
					response = new ResponseObject (ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_LOGGEDOUT);
				} else {
					response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_NOTLOGGEDIN); 
				}
			} else {
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_NOTLOGGEDIN); 	
			}
			}
			return response;
		}
}