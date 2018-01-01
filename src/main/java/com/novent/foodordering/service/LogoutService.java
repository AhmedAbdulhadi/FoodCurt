package com.novent.foodordering.service;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import com.novent.foodordering.util.ResponseObject;

@Service
public interface LogoutService {
	
	public ResponseObject logout(HttpServletRequest request);
	
//	public ResponseObject adminLogout(HttpSession httpSession, Id id);
//
//	public ResponseObject administratorLogout(HttpSession httpSession, Id id);
//	
//	public ResponseObject restaurantLogout(HttpSession httpSession, Id id);

}
