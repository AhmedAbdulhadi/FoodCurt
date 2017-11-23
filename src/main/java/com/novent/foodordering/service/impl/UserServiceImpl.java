package com.novent.foodordering.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.novent.foodordering.constatnt.ResponseCode;
import com.novent.foodordering.constatnt.ResponseMessage;
import com.novent.foodordering.constatnt.ResponseStatus;
import com.novent.foodordering.dao.UserDao;
import com.novent.foodordering.entity.Users;
import com.novent.foodordering.service.UserService;
import com.novent.foodordering.util.ResponseObject;
import com.novent.foodordering.util.ResponseObjectAll;
import com.novent.foodordering.util.ResponseObjectCrud;
import com.novent.foodordering.util.ResponseObjectData;

@Service
@Component
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserDao userDao;
	
	@Override
	public ResponseObject getUserByStatus(boolean status) {
		ResponseObject response = null;
		List<Users> allUsers = userDao.findByStatus(status);
		if(!allUsers.isEmpty()){
			response = new ResponseObjectAll<>(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, allUsers);
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_GETTING_MESSAGE);
		}
		return response;
	}

	@Override
	public ResponseObject getUserById(long userId) {
		ResponseObject response = null;
		Users user = userDao.findByUserId(userId);
		if (user != null){
			response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, user);
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_GETTING_MESSAGE);
		}
		return response;
	}

	@Override
	public ResponseObject createUser(Users user) {
		ResponseObject response = null;
		PhoneNumber  phone = null;
		boolean isValidNumber = false;
		boolean isJONumber = false;
		long id = 0;
		
		Users phoneNumberUser = userDao.findByPhoneNumber(user.getPhoneNumber());
		Users userNameUser = userDao.findByUserName(user.getUserName());
		Users emailUser = userDao.findByEmail(user.getEmail());
		
		boolean valid = ((phoneNumberUser == null ) && (userNameUser == null) && (emailUser == null)) ;
		
		PhoneNumberUtil pnUtil = PhoneNumberUtil.getInstance();
		String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"; 
		
		String phoneNumber = user.getPhoneNumber();
		String userName = user.getUserName();
		String fullName = user.getFullName();
		String password = user.getPassword();
		String email = user.getEmail();
		
		try{
			phone = pnUtil.parse(phoneNumber,"");
			isValidNumber = pnUtil.isValidNumber(phone);
		    isJONumber = pnUtil.getRegionCodeForNumber(phone).equals("JO");
		} catch (Exception e) {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_ERROR);
		}
		
		if (phoneNumber == null || phoneNumber.equals("")){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PHONENUMBER_REQUIRED_ERROR);				
		} else if (userName == null || userName.equals("")){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_USERNAME_REQUIRED_ERROR);				
		}  else if (fullName == null || fullName.equals("")){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_FULLNAME_REQUIRED_ERROR);				
		}  else if (password == null || password.equals("")){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PASSWORD_REQUIRED_ERROR);				
		}  else if (email == null || email.equals("")){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_EMAIL_ADDRESS_REQUIRED_ERROR);				
		}  else if(phoneNumber != null && phoneNumber != "" && !isValidNumber && phoneNumber.substring(0, 2).equals("00")){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PREFIX_FORMAT_ERROR);			
		} else if (!isJONumber || !isValidNumber){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PHONENUMBER_FORMAT_ERROR);
		} else if(phoneNumberUser != null){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PHONENUMBER_ALREADY_EXIST_ERROR);
		}  else if(userName.length() > 20 || userName.length() < 6){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_USERNAME_LENGTH_ERROR);
		} else if(userNameUser != null){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_USERNAME_ALREADY_EXIST_ERROR);
		} else if (fullName.length() > 40 || fullName.length() <10){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_FULLNAME_LENGTH_ERROR);
		} else if(password.length() < 6 || password.length() > 10){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PASSWORD_LENGTH_ERROR);
		} else if(emailUser != null){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_EMAIL_ALREADY_EXIST_ERROR);
		} else if (!email.matches(regex)) {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_EMAIL_FORMAT_ERROR);
		} else if(valid){
			userDao.save(user);
			id =user.getUserId();
			response = new ResponseObjectCrud(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_CREATE_CODE, ResponseMessage.SUCCESS_CREATING_MESSAGE, id);
		} else{
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_CREATING_MESSAGE);
		} 
		return response;
	}

	@Override
	public ResponseObject updateUser(long userId,Users user) {
		ResponseObject response = null;
		PhoneNumber  phone = null;
		boolean isValidNumber = false;
		boolean isJONumber = false;
		boolean valid = true;
		
		Users userToUpdate = userDao.findByUserId(userId);
		String phoneNumber = user.getPhoneNumber();
		String userName = user.getUserName();
		String fullName = user.getFullName();
		String password = user.getPassword();
		String email = user.getEmail();
		
		if(userToUpdate == null ){
			valid = false ;
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_NO_USER_ERROR);
		} else if(!userToUpdate.isStatus()){
			valid = false ;
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_UPDATE_USER_ERROR);
		}
		
		PhoneNumberUtil pnUtil = PhoneNumberUtil.getInstance();
	    if (phoneNumber != null && phoneNumber != "" && valid){
			try{
				phone = pnUtil.parse(phoneNumber,"");
				isValidNumber = pnUtil.isValidNumber(phone);
			    isJONumber = pnUtil.getRegionCodeForNumber(phone).equals("JO");
			} catch (Exception e) {
				valid = false ;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_ERROR);
			}
			
			Users phoneNumberUser = userDao.findByPhoneNumber(user.getPhoneNumber());
			if(phoneNumberUser != null && !userToUpdate.equals(phoneNumberUser)){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PHONENUMBER_ALREADY_EXIST_ERROR);
			} else if (!isValidNumber && phoneNumber.substring(0, 2).equals("00")){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PREFIX_FORMAT_ERROR);			
			} else if (!isJONumber || !isValidNumber){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PHONENUMBER_FORMAT_ERROR);
			} else if(valid){
				userToUpdate.setPhoneNumber(phoneNumber);
				userToUpdate.setUpdatedAt(new Date());
				userDao.save(userToUpdate);
				response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, userToUpdate);
			}	
		}
		
	    
	    if(userName != null && !userName.equals("") && valid){
			Users userNameUser = userDao.findByUserName(userName);
			if(userName.length() > 20 ){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_USERNAME_LENGTH_LESS_ERROR);
			} else if (userName.length() < 6){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_USERNAME_LENGTH_GREATER_ERROR);
			} else if(userNameUser != null && !userToUpdate.equals(userNameUser)){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_USERNAME_ALREADY_EXIST_ERROR);
			} else if(valid){
				userToUpdate.setUserName(userName);
				userToUpdate.setUpdatedAt(new Date());
				userDao.save(userToUpdate);
				response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, userToUpdate);
			}
		}
		
	    
	    if(fullName != null && !fullName.equals("") && valid){
			if (fullName.length() > 40 ){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_FULLNAME_LENGTH_LESS_ERROR);
			} else if (fullName.length() < 10){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_FULLNAME_LENGTH_GREATER_ERROR);
			} else if(valid){
				userToUpdate.setFullName(fullName);
				userToUpdate.setUpdatedAt(new Date());
				userDao.save(userToUpdate);
				response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, userToUpdate);
			}
		}
	    
	    if (password != null && !password.equals("") && valid){
			if(password.length() < 6 ){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PASSWORD_LENGTH_GREATER_ERROR);
			} else if (password.length() > 10){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PASSWORD_LENGTH_LESS_ERROR);
			} else if(valid){
				userToUpdate.setPassword(password);
				userToUpdate.setUpdatedAt(new Date());
				userDao.save(userToUpdate);
				response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, userToUpdate);	
			} 
		}
	    
	    
	    if (email != null && !email.equals("") && valid){
			Users emailUser = userDao.findByEmail(user.getEmail());
			String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
			if (!email.matches(regex)) {
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_EMAIL_FORMAT_ERROR);
			} else if(emailUser != null && !userToUpdate.equals(emailUser)){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_EMAIL_ALREADY_EXIST_ERROR);
			} else if(valid){
				userToUpdate.setEmail(email);
				userToUpdate.setUpdatedAt(new Date());
				userDao.save(userToUpdate);
				response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, userToUpdate);	
			}
		}
		return response;
	}

	@Override
	public ResponseObject deleteUser(long userId) {
		ResponseObject response = null;
		Users user = userDao.findByUserId(userId);
		if(user != null && user.isStatus()){
			user.setStatus(false);
			user.setDeletedAt(new Date());
			userDao.save(user);
			response = new ResponseObjectCrud(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_DELETTING_MESSAGE, userId);
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_DELETTING_MESSAGE);
		}
		return response;
	}

	

}
