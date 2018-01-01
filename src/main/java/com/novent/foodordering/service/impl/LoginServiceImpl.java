package com.novent.foodordering.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.novent.foodordering.constatnt.ResponseCode;
import com.novent.foodordering.constatnt.ResponseMessage;
import com.novent.foodordering.constatnt.ResponseStatus;
import com.novent.foodordering.constatnt.Type;
import com.novent.foodordering.dao.AdministratorDao;
import com.novent.foodordering.dao.LoginDao;
import com.novent.foodordering.dao.UserDao;
import com.novent.foodordering.entity.Administrator;
import com.novent.foodordering.entity.Logins;
import com.novent.foodordering.entity.Users;
import com.novent.foodordering.service.LoginService;
import com.novent.foodordering.util.CommonsValidator;
import com.novent.foodordering.util.Login;
import com.novent.foodordering.util.ResponseObject;
import com.novent.foodordering.util.ResponseObjectToken;

@Service
@Component
public class LoginServiceImpl implements LoginService{
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private AdministratorDao administratorDao;
	@Autowired
	private LoginDao loginDao;
	

	@Override
	public ResponseObject login(Login login, HttpServletRequest request) {
		ResponseObject response = null; 
		Users user = userDao.findByUserName(login.getUserName());
		HttpSession session = request.getSession();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd hh:mm:ss");
		
		if(user != null && user.isStatus()) {
			if (login.getPassword().equals(user.getPassword())){
				if (user.getIsLoggedIn() == false){
					Logins newLogin = loginDao.findByUserId(user.getId());
					if(newLogin == null){
						newLogin = new Logins();
					}
					newLogin.setPassword(user.getPassword());
					newLogin.setUserName(user.getUserName());
					newLogin.setUserId(user.getId());
					String token = CommonsValidator.getToken();
					newLogin.setToken(token);
					newLogin.setLoggedInAt(new Date());
					loginDao.save(newLogin);
					user.setIsLoggedIn(true);
					userDao.save(user);
					session.setAttribute(user.getUserName(), dateFormat.format(new Date())+"."+login.isRemmberMe());
					response = new ResponseObjectToken(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_LOGIN_MESSAGE,user.getId(), newLogin.getToken());
				} else response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_USER_ALREADY_LOGGEDIN_ERROR);
			} else {
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_PASSWORD_ERROR);
			}
		} else if(user == null){

			if(administratorDao.findAll().isEmpty()){
				Administrator firstAdmin = new Administrator();
				firstAdmin.setFullName("Raghad Almanasrah");
				firstAdmin.setEmail("raghad.almanasrah@yahoo.com");
				firstAdmin.setUserName("RaghadAlmanasrah");
				firstAdmin.setPassword("raghad_00");
				firstAdmin.setPhoneNumber("962785383645");
				firstAdmin.setPrivilege(Administrator.Privilege.SUPER);
				firstAdmin.setRole(Type.ADMINISTRATOR);
				administratorDao.save(firstAdmin);
			}
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_INCORRECT_CREDENTIALS_ERROR);
		} else if (!user.isStatus()){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_UPDATE_USER_ERROR);
		}
		return response;
	}
}