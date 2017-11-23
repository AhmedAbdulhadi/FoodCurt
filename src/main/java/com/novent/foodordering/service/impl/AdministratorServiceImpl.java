package com.novent.foodordering.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.novent.foodordering.constatnt.ResponseCode;
import com.novent.foodordering.constatnt.ResponseMessage;
import com.novent.foodordering.constatnt.ResponseStatus;
import com.novent.foodordering.dao.AdministratorDao;
import com.novent.foodordering.entity.Administrator;
import com.novent.foodordering.entity.Administrator.Privilege;
import com.novent.foodordering.service.AdministratorService;
import com.novent.foodordering.util.Administrators;
import com.novent.foodordering.util.ResponseObject;
import com.novent.foodordering.util.ResponseObjectAll;
import com.novent.foodordering.util.ResponseObjectCrud;
import com.novent.foodordering.util.ResponseObjectData;

@Service
@Component
public class AdministratorServiceImpl implements AdministratorService{
	
	@Autowired
	private AdministratorDao administratorDao;

	@Override
	public ResponseObject getAdministratorsByStatus(boolean status) {
		ResponseObject response = null;
		List<Administrator> allAdmins = administratorDao.findByStatus(status);
		if(!allAdmins.isEmpty()){
			List<Administrators> jsonAdministrator = new ArrayList<Administrators>(); 
			for (Iterator<Administrator> iterator = allAdmins.iterator(); iterator.hasNext();){
				Administrator administrator = iterator.next();
				
				jsonAdministrator.add(new Administrators(administrator.getAdministratorId(),  administrator.getPhoneNumber(), administrator.getUserName(), administrator.getFullName(), administrator.getEmail(), 
						administrator.getPrivilege(), administrator.getCreatedAt(), administrator.getUpdatedAt(), administrator.getDeletedAt(), administrator.isStatus()));
			}	
			response = new ResponseObjectAll<Administrators>(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, jsonAdministrator);
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_GETTING_MESSAGE);
		}
		return response;
	}

	@Override
	public ResponseObject getAdministratorById(long AdministratorId) {
		ResponseObject response = null;
		Administrator administrator = administratorDao.findByAdministratorId(AdministratorId);
		if (administrator != null){
			Administrators administrators =	new Administrators(administrator.getAdministratorId(),  administrator.getPhoneNumber(), administrator.getUserName(), administrator.getFullName(), administrator.getEmail(), 
					administrator.getPrivilege(), administrator.getCreatedAt(), administrator.getUpdatedAt(), administrator.getDeletedAt(), administrator.isStatus());
			response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, administrators);
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_GETTING_MESSAGE);
		}
		return response;
	}

	@Override
	public ResponseObject createAdministrator(Administrator administrator) {
		ResponseObject response = null;
		PhoneNumber  phone = null;
		boolean isValidNumber = false;
		boolean isJONumber = false;
		long id = 0;
		
		Administrator phoneNumberAdmin = administratorDao.findByPhoneNumber(administrator.getPhoneNumber());
		Administrator userNameAdmin = administratorDao.findByUserName(administrator.getUserName());
		Administrator emailAdmin = administratorDao.findByEmail(administrator.getEmail());
		
		boolean valid = (phoneNumberAdmin == null && userNameAdmin == null && emailAdmin == null) ;
	
		PhoneNumberUtil pnUtil = PhoneNumberUtil.getInstance();
		String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"; 
		
		String phoneNumber = administrator.getPhoneNumber();
		String userName = administrator.getUserName();
		String fullName = administrator.getFullName();
		String password = administrator.getPassword();
		String email = administrator.getEmail();
		Privilege privilege = administrator.getPrivilege();
		
		try{
			phone = pnUtil.parse(phoneNumber,"");
			isValidNumber = pnUtil.isValidNumber(phone);
		    isJONumber = pnUtil.getRegionCodeForNumber(phone).equals("JO");
		} catch (Exception e) {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_ERROR);
		}
		
		if(phoneNumber != null && phoneNumber != "" && phoneNumber.charAt(0) == '+'){
			phoneNumber = phoneNumber.replace("+","");
		}
	
		if(phoneNumber != null && phoneNumber != "" && phoneNumber.charAt(3) == '0'){
			phoneNumber = phoneNumber.replace("0","");
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
		}  else if (privilege == null || privilege.equals("")){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PRIVILAGE_REQUIRED_ERROR);				
		} else if(phoneNumber != null && phoneNumber != "" && !isValidNumber && phoneNumber.substring(0, 2).equals("00")){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PREFIX_FORMAT_ERROR);			
		} else if (!isJONumber || !isValidNumber){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PHONENUMBER_FORMAT_ERROR);
		} else if (phoneNumberAdmin != null){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PHONENUMBER_ALREADY_EXIST_ERROR);
		} else if(userName.length() > 20 || userName.length() < 6){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_USERNAME_LENGTH_ERROR);
		} else if(userNameAdmin != null){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_USERNAME_ALREADY_EXIST_ERROR);
		} else if (fullName.length() > 40 || fullName.length() <10){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_FULLNAME_LENGTH_ERROR);
		} else if(password.length() < 6 || password.length() > 10){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PASSWORD_LENGTH_ERROR);
		} else if(emailAdmin != null){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_EMAIL_ALREADY_EXIST_ERROR);
		} else if (!email.matches(regex)) {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_EMAIL_FORMAT_ERROR);
		} else if(valid){
			administrator.setPhoneNumber(phoneNumber);
			administratorDao.save(administrator);
			id =administrator.getAdministratorId();
			response = new ResponseObjectCrud(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_CREATE_CODE, ResponseMessage.SUCCESS_CREATING_MESSAGE, id);
		} else{
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_CREATING_MESSAGE);
		} 
		return response;
	}

	@Override
	public ResponseObject updateAdministrator(long administratorId, Administrator administrator) {
		ResponseObject response = null;
		PhoneNumber  phone = null;
		boolean isValidNumber = false;
		boolean isJONumber = false;
		boolean valid = true;
		
		Administrator administratorToUpdate = administratorDao.findByAdministratorId(administratorId);
		String phoneNumber = administrator.getPhoneNumber();
		String userName = administrator.getUserName();
		String fullName = administrator.getFullName();
		String password = administrator.getPassword();
		String email = administrator.getEmail();
	    Privilege privilege = administrator.getPrivilege();
	    
	    if(administratorToUpdate == null){
	    	valid = false;
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_DELETTING_MESSAGE);
		} else if (!administratorToUpdate.isStatus() ){
			valid = false ;
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_UPDATE_ADMINISTRATOR_ERROR);
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
			
			if(phoneNumber != null && phoneNumber != "" && phoneNumber.charAt(0) == '+'){
				phoneNumber = phoneNumber.replace("+","");
			}
		
			if(phoneNumber != null && phoneNumber != "" && phoneNumber.charAt(3) == '0'){
				phoneNumber = phoneNumber.replace("0","");
			}
		 
			Administrator phoneNumberAdmin = administratorDao.findByPhoneNumber(administrator.getPhoneNumber());
			if(phoneNumberAdmin != null && !administratorToUpdate.equals(phoneNumberAdmin)){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PHONENUMBER_ALREADY_EXIST_ERROR);
			} else if (!isValidNumber && phoneNumber.substring(0, 2).equals("00")){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PREFIX_FORMAT_ERROR);			
			} else if (!isJONumber || !isValidNumber){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PHONENUMBER_FORMAT_ERROR);
			} else if(valid){
				administratorToUpdate.setPhoneNumber(phoneNumber);
				administratorToUpdate.setUpdatedAt(new Date());
				administratorDao.save(administratorToUpdate);
				Administrators jsonAdministrators =	new Administrators(administratorToUpdate.getAdministratorId(),  administratorToUpdate.getPhoneNumber(), administratorToUpdate.getUserName(), administratorToUpdate.getFullName(), administrator.getEmail(), 
						administratorToUpdate.getPrivilege(), administratorToUpdate.getCreatedAt(), administratorToUpdate.getUpdatedAt(), administratorToUpdate.getDeletedAt(), administratorToUpdate.isStatus());
				response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonAdministrators);
			}	
		}
	    
	    
	    if(userName != null && !userName.equals("") && valid){
			Administrator userNameAdministrator = administratorDao.findByUserName(userName);
			if(userName.length() > 20 ){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_USERNAME_LENGTH_LESS_ERROR);
			} else if (userName.length() < 6){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_USERNAME_LENGTH_GREATER_ERROR);
			} else if(userNameAdministrator != null && !administratorToUpdate.equals(userNameAdministrator)){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_USERNAME_ALREADY_EXIST_ERROR);
			} else if(valid){
				administratorToUpdate.setUserName(userName);
				administratorToUpdate.setUpdatedAt(new Date());
				administratorDao.save(administratorToUpdate);
				Administrators jsonAdministrators =	new Administrators(administratorToUpdate.getAdministratorId(),  administratorToUpdate.getPhoneNumber(), administratorToUpdate.getUserName(), administratorToUpdate.getFullName(), administrator.getEmail(), 
						                                               administratorToUpdate.getPrivilege(), administratorToUpdate.getCreatedAt(), administratorToUpdate.getUpdatedAt(), administratorToUpdate.getDeletedAt(), administratorToUpdate.isStatus());
				response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonAdministrators);	
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
				administratorToUpdate.setFullName(fullName);
				administratorToUpdate.setUpdatedAt(new Date());
				administratorDao.save(administratorToUpdate);
				Administrators jsonAdministrators =	new Administrators(administratorToUpdate.getAdministratorId(),  administratorToUpdate.getPhoneNumber(), administratorToUpdate.getUserName(), administratorToUpdate.getFullName(), administratorToUpdate.getEmail(), 
						                                               administratorToUpdate.getPrivilege(), administratorToUpdate.getCreatedAt(), administratorToUpdate.getUpdatedAt(), administratorToUpdate.getDeletedAt(), administratorToUpdate.isStatus());
				response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonAdministrators);
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
				administratorToUpdate.setPassword(password);
				administratorToUpdate.setUpdatedAt(new Date());
				administratorDao.save(administratorToUpdate);
				Administrators jsonAdministrators =	new Administrators(administratorToUpdate.getAdministratorId(),  administratorToUpdate.getPhoneNumber(), administratorToUpdate.getUserName(), administratorToUpdate.getFullName(), administratorToUpdate.getEmail(), 
                        administratorToUpdate.getPrivilege(), administratorToUpdate.getCreatedAt(), administratorToUpdate.getUpdatedAt(), administratorToUpdate.getDeletedAt(), administratorToUpdate.isStatus());
				response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonAdministrators);	
				} 
		}
	    
	    
	    if (email != null && !email.equals("") && valid){
			Administrator emailAdmin = administratorDao.findByEmail(email);
			String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
			if (!email.matches(regex)) {
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_EMAIL_FORMAT_ERROR);
			} else if(emailAdmin != null && !administratorToUpdate.equals(emailAdmin)){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_EMAIL_ALREADY_EXIST_ERROR);
			} else if(valid){
				administratorToUpdate.setEmail(email);
				administratorToUpdate.setUpdatedAt(new Date());
				administratorDao.save(administratorToUpdate);
				Administrators jsonAdministrators =	new Administrators(administratorToUpdate.getAdministratorId(),  administratorToUpdate.getPhoneNumber(), administratorToUpdate.getUserName(), administratorToUpdate.getFullName(), administratorToUpdate.getEmail(), 
                        administratorToUpdate.getPrivilege(), administratorToUpdate.getCreatedAt(), administratorToUpdate.getUpdatedAt(), administratorToUpdate.getDeletedAt(), administratorToUpdate.isStatus());
				response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonAdministrators);	
				}
		}
	    
	    
	    if (privilege != null && !privilege.equals("") && valid){
	    	administratorToUpdate.setPrivilege(privilege);
	    	administratorToUpdate.setUpdatedAt(new Date());
	    	administratorDao.save(administratorToUpdate);
	    	Administrators jsonAdministrators =	new Administrators(administratorToUpdate.getAdministratorId(),  administratorToUpdate.getPhoneNumber(), administratorToUpdate.getUserName(), administratorToUpdate.getFullName(), administratorToUpdate.getEmail(), 
                    administratorToUpdate.getPrivilege(), administratorToUpdate.getCreatedAt(), administratorToUpdate.getUpdatedAt(), administratorToUpdate.getDeletedAt(), administratorToUpdate.isStatus());
	    	response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonAdministrators);
	    	}
		return response;
	}

	@Override
	public ResponseObject deleteAdministrator(long administratorId) {
		ResponseObject response = null;
		Administrator admin = administratorDao.findByAdministratorId(administratorId);
		if(admin != null && admin.isStatus()){
			admin.setStatus(false);
			admin.setDeletedAt(new Date());
			administratorDao.save(admin);
			response = new ResponseObjectCrud(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_DELETTING_MESSAGE, administratorId);
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_DELETTING_MESSAGE);
		}
		return response;
	}
}
