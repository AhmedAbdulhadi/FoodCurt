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
import com.novent.foodordering.dao.AdminDao;
import com.novent.foodordering.dao.AdministratorDao;
import com.novent.foodordering.entity.Admin;
import com.novent.foodordering.entity.Admin.Privilege;
import com.novent.foodordering.entity.Administrator;
import com.novent.foodordering.service.AdminService;
import com.novent.foodordering.util.ResponseObject;
import com.novent.foodordering.util.ResponseObjectAll;
import com.novent.foodordering.util.ResponseObjectCrud;
import com.novent.foodordering.util.ResponseObjectData;

@Service
@Component
public class AdminServiceImpl implements AdminService{
	
	@Autowired
	private AdminDao adminDao;
	@Autowired
	private AdministratorDao administratorDao;

	@Override
	public ResponseObject getAllAdmins() {
		ResponseObject response = null;
		List<Admin> allAdmins = adminDao.findAll();
		if(!allAdmins.isEmpty()){
			response = new ResponseObjectAll<Admin>(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, allAdmins);
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_GETTING_MESSAGE);
		}
		return response;
	}

	@Override
	public ResponseObject getAdminById(long adminId) {
		ResponseObject response = null;
		Admin admin = adminDao.findByAdminId(adminId);
		if (admin != null){
			response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, admin);
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_GETTING_MESSAGE);
		}
		return response;
	}

	@Override
	public ResponseObject createAdmin(Admin admin) {
		ResponseObject response = null;
		PhoneNumber  phone = null;
		boolean isValidNumber = false;
		boolean isJONumber = false;
		long id = 0;
		
		Admin phoneNumberAdmin = adminDao.findByPhoneNumber(admin.getPhoneNumber());
		Admin userNameAdmin = adminDao.findByUserName(admin.getUserName());
		Admin emailAdmin = adminDao.findByEmail(admin.getEmail());
		Administrator administrator = administratorDao.findByAdministratorId(admin.getAdministratorId());
		
		PhoneNumberUtil pnUtil = PhoneNumberUtil.getInstance();
		String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"; 
		
		String phoneNumber = admin.getPhoneNumber();
		String userName = admin.getUserName();
		String fullName = admin.getFullName();
		String password = admin.getPassword();
		String email = admin.getEmail();
		
		
		try{
			phone = pnUtil.parse(phoneNumber,"");
			isValidNumber = pnUtil.isValidNumber(phone);
		    isJONumber = pnUtil.getRegionCodeForNumber(phone).equals("JO");
		} catch (Exception e) {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_ERROR);
		}
		
		boolean valid = (phoneNumberAdmin == null  && userNameAdmin == null && emailAdmin == null && administrator != null /*&& isValidNumber && isJONumber */) ;
		
		
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
		} else if (!email.matches(regex)) {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_EMAIL_FORMAT_ERROR);
		} else if(emailAdmin != null){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_EMAIL_ALREADY_EXIST_ERROR);
		}  else if(administrator == null){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_ADMINISTRATOR_NUMBER_ERROR);
		}  else if(valid){
			adminDao.save(admin);
			List<Admin> admins = administrator.getAdmins();
			admins.add(admin);
			administrator.setAdmins(admins);
			administratorDao.save(administrator);
			id =admin.getAdminId();
			response = new ResponseObjectCrud(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_CREATE_CODE, ResponseMessage.SUCCESS_CREATING_MESSAGE, id);
		} else{
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_CREATING_MESSAGE);
		} 
		return response;
	}

	@Override
	public ResponseObject updateAdmin(long adminId, Admin admin) {
		ResponseObject response = null;
		PhoneNumber  phone = null;
		boolean isValidNumber = false;
		boolean isJONumber = false;
		
		Admin adminToUpdate = adminDao.findByAdminId(adminId);
		Admin phoneNumberAdmin = adminDao.findByPhoneNumber(admin.getPhoneNumber());
		Admin userNameAdmin = adminDao.findByUserName(admin.getUserName());
		Admin emailAdmin = adminDao.findByEmail(admin.getEmail());
		Administrator administrator = administratorDao.findByAdministratorId(admin.getAdministratorId());

		boolean valid = (phoneNumberAdmin == null && userNameAdmin == null && emailAdmin == null && administrator != null && (adminToUpdate != null && adminToUpdate.isStatus())) ||
				(userNameAdmin == null && phoneNumberAdmin == null && emailAdmin != null && adminToUpdate.equals(emailAdmin))||
				(userNameAdmin == null && phoneNumberAdmin != null && emailAdmin == null && adminToUpdate.equals(phoneNumberAdmin))||
				(userNameAdmin == null && phoneNumberAdmin != null && emailAdmin != null && adminToUpdate.equals(phoneNumberAdmin)&& adminToUpdate.equals(emailAdmin))||
				(userNameAdmin != null && phoneNumberAdmin == null && emailAdmin == null && adminToUpdate.equals(userNameAdmin))||
				(userNameAdmin != null && phoneNumberAdmin == null && emailAdmin != null && adminToUpdate.equals(userNameAdmin)&& adminToUpdate.equals(emailAdmin))||
				(userNameAdmin != null && phoneNumberAdmin != null && emailAdmin == null && adminToUpdate.equals(userNameAdmin) && adminToUpdate.equals(phoneNumberAdmin))||
				(userNameAdmin != null && phoneNumberAdmin != null && emailAdmin != null && adminToUpdate.equals(userNameAdmin)&& adminToUpdate.equals(phoneNumberAdmin)&& adminToUpdate.equals(emailAdmin));
	
		PhoneNumberUtil pnUtil = PhoneNumberUtil.getInstance();
		String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
		
		String userName = admin.getUserName();
		String fullName = admin.getFullName();
		String phoneNumber = admin.getPhoneNumber();
		String password = admin.getPassword();
		String email = admin.getEmail();
		Privilege privilege = admin.getPrivilege();
		
		
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
		} else if (!email.matches(regex)) {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_EMAIL_FORMAT_ERROR);
		} else if(emailAdmin != null && !adminToUpdate.equals(emailAdmin)){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_EMAIL_ALREADY_EXIST_ERROR);
		} else if(adminToUpdate == null || !adminToUpdate.isStatus() ){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_DELETTING_MESSAGE);
		} else if(administrator == null){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_ADMINISTRATOR_NUMBER_ERROR);
		} else if(valid){
			adminToUpdate.setPhoneNumber(phoneNumber);
			adminToUpdate.setUserName(userName);
			adminToUpdate.setFullName(fullName);
			adminToUpdate.setPassword(password);
			adminToUpdate.setEmail(email);
			adminToUpdate.setPrivilege(privilege);
			adminToUpdate.setUpdatedAt(new Date());
			adminDao.save(adminToUpdate);
			response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, adminToUpdate);
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_UPDATING_MESSAGE);
		}
		return response;
	}

	@Override
	public ResponseObject deleteAdmin(long adminId) {
		ResponseObject response = null;
		Admin admin = adminDao.findByAdminId(adminId);
		if(admin != null && admin.isStatus()){
			admin.setStatus(false);
			admin.setDeletedAt(new Date());
			adminDao.save(admin);
			response = new ResponseObjectCrud(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_DELETTING_MESSAGE, adminId);
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_DELETTING_MESSAGE);
		}
		return response;
	}

}
