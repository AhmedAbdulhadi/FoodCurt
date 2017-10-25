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
import com.novent.foodordering.dao.AdministratorDao;
import com.novent.foodordering.entity.Administrator;
import com.novent.foodordering.entity.Administrator.Privilege;
import com.novent.foodordering.service.AdministratorService;
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
	public ResponseObject getAllAdministrators() {
		ResponseObject response = null;
		List<Administrator> allAdmins = administratorDao.findAll();
		if(!allAdmins.isEmpty()){
			response = new ResponseObjectAll<Administrator>(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, allAdmins);
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_GETTING_MESSAGE);
		}
		return response;
	}

	@Override
	public ResponseObject getAdministratorById(long AdministratorId) {
		ResponseObject response = null;
		Administrator admin = administratorDao.findByAdministratorId(AdministratorId);
		if (admin != null){
			response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, admin);
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
		
		try{
			phone = pnUtil.parse(phoneNumber,"");
			isValidNumber = pnUtil.isValidNumber(phone);
		    isJONumber = pnUtil.getRegionCodeForNumber(phone).equals("JO");
		} catch (Exception e) {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_ERROR);
		}
		
		if(!isValidNumber && phoneNumber.substring(0, 2).equals("00")){
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
		
		Administrator adminToUpdate = administratorDao.findByAdministratorId(administratorId);
		Administrator phoneNumberAdmin = administratorDao.findByPhoneNumber(administrator.getPhoneNumber());
		Administrator userNameAdmin = administratorDao.findByUserName(administrator.getUserName());
		Administrator emailAdmin = administratorDao.findByEmail(administrator.getEmail());
		
		boolean valid =
		        (userNameAdmin == null && phoneNumberAdmin == null && emailAdmin == null && administrator != null && (adminToUpdate != null && adminToUpdate.isStatus())) ||
				(userNameAdmin == null && phoneNumberAdmin == null && emailAdmin != null && adminToUpdate.equals(emailAdmin))||
				(userNameAdmin == null && phoneNumberAdmin != null && emailAdmin == null && adminToUpdate.equals(phoneNumberAdmin))||
				(userNameAdmin == null && phoneNumberAdmin != null && emailAdmin != null && adminToUpdate.equals(phoneNumberAdmin)&& adminToUpdate.equals(emailAdmin))||
				(userNameAdmin != null && phoneNumberAdmin == null && emailAdmin == null && adminToUpdate.equals(userNameAdmin))||
				(userNameAdmin != null && phoneNumberAdmin == null && emailAdmin != null && adminToUpdate.equals(userNameAdmin)&& adminToUpdate.equals(emailAdmin))||
				(userNameAdmin != null && phoneNumberAdmin != null && emailAdmin == null && adminToUpdate.equals(userNameAdmin) && adminToUpdate.equals(phoneNumberAdmin))||
				(userNameAdmin != null && phoneNumberAdmin != null && emailAdmin != null && adminToUpdate.equals(userNameAdmin)&& adminToUpdate.equals(phoneNumberAdmin)&& adminToUpdate.equals(emailAdmin));
		
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
	    

	    if(adminToUpdate == null || !adminToUpdate.isStatus() ){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_DELETTING_MESSAGE);
		} else if(!isValidNumber && phoneNumber.substring(0, 2).equals("00")){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PREFIX_FORMAT_ERROR);			
		} else if (!isJONumber || !isValidNumber){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PHONENUMBER_FORMAT_ERROR);
		} else if (phoneNumberAdmin != null && !adminToUpdate.equals(phoneNumberAdmin)){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PHONENUMBER_ALREADY_EXIST_ERROR);
		} else if(userName.length() > 20 || userName.length() < 6){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_USERNAME_LENGTH_ERROR);
		} else if(userNameAdmin != null && !adminToUpdate.equals(userNameAdmin)){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_USERNAME_ALREADY_EXIST_ERROR);
		} else if (fullName.length() > 40 || fullName.length() <10){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_FULLNAME_LENGTH_ERROR);
		} else if(password.length() < 6 || password.length() > 10){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PASSWORD_LENGTH_ERROR);
		} else if(emailAdmin != null && !adminToUpdate.equals(emailAdmin)){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_EMAIL_ALREADY_EXIST_ERROR);
		} else if (!email.matches(regex)) {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_EMAIL_FORMAT_ERROR);
		} else if(valid){
			adminToUpdate.setPhoneNumber(phoneNumber);
			adminToUpdate.setUserName(userName);
			adminToUpdate.setFullName(fullName);
			adminToUpdate.setPassword(password);
			adminToUpdate.setEmail(email);
			adminToUpdate.setPrivilege(privilege);
			adminToUpdate.setUpdatedAt(new Date());
			administratorDao.save(adminToUpdate);
			response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, adminToUpdate);
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_UPDATING_MESSAGE);
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
