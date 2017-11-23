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
import com.novent.foodordering.dao.AdminDao;
import com.novent.foodordering.dao.AdministratorDao;
import com.novent.foodordering.entity.Admin;
import com.novent.foodordering.entity.Admin.Privilege;
import com.novent.foodordering.entity.Administrator;
import com.novent.foodordering.service.AdminService;
import com.novent.foodordering.util.Admins;
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
	public ResponseObject getAdminByStatus(boolean status) {
		ResponseObject response = null;
		List<Admin> allAdmins = adminDao.findByStatus(status);
		
		if(!allAdmins.isEmpty()){
			List<Admins> jsonAdmins = new ArrayList<Admins>(); 
			for (Iterator<Admin> iterator = allAdmins.iterator(); iterator.hasNext();){
				Admin admin = iterator.next();
				jsonAdmins.add(new Admins(admin.getAdminId(), admin.getPhoneNumber(), admin.getUserName(), admin.getFullName(), admin.getEmail(), 
	                      admin.getAdministratorId(), admin.getPrivilege(), admin.getCreatedAt(), admin.getUpdatedAt(), admin.getDeletedAt(), admin.isStatus()));
			}	
			response = new ResponseObjectAll<Admins>(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, jsonAdmins);
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
			Admins jsonAdmin = new Admins(admin.getAdminId(), admin.getPhoneNumber(), admin.getUserName(), admin.getFullName(), admin.getEmail(), 
					                      admin.getAdministratorId(), admin.getPrivilege(), admin.getCreatedAt(), admin.getUpdatedAt(), admin.getDeletedAt(), admin.isStatus());
			response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, jsonAdmin);
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
		Privilege privilege = admin.getPrivilege();
		long administratorId = admin.getAdministratorId();
		
		
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
		
		boolean valid = (phoneNumberAdmin == null  && userNameAdmin == null && emailAdmin == null && administrator != null /*&& isValidNumber && isJONumber */) ;
		
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
		}  else if (administratorId == 0 ){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_ADMINISTRATORID_REQUIRED_ERROR);				
		} else if(phoneNumber != null && phoneNumber != "" &&!isValidNumber && phoneNumber.substring(0, 2).equals("00")){
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
		}  else if(!administrator.isStatus()){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_UPDATE_ADMINISTRATOR_ERROR);
		}  else if(valid){
			admin.setPhoneNumber(phoneNumber);
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
		boolean valid = true ;
		
		Admin adminToUpdate = adminDao.findByAdminId(adminId);
		String userName = admin.getUserName();
		String fullName = admin.getFullName();
		String phoneNumber = admin.getPhoneNumber();
		String password = admin.getPassword();
		String email = admin.getEmail();
		Privilege privilege = admin.getPrivilege();
		long administratorId = admin.getAdministratorId();
		
		 if(adminToUpdate == null){
			 valid = false;
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_DELETTING_MESSAGE);
		} else if (!adminToUpdate.isStatus()){
			valid = false ;
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_UPDATE_ADMIN_ERROR);
		}
		
		
		PhoneNumberUtil pnUtil = PhoneNumberUtil.getInstance();
		if (phoneNumber != null && phoneNumber != "" && valid){
			try{
				phone = pnUtil.parse(phoneNumber,"");
				isValidNumber = pnUtil.isValidNumber(phone);
			    isJONumber = pnUtil.getRegionCodeForNumber(phone).equals("JO");
			} catch (Exception e) {
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_ERROR);
			}
			
			if(phoneNumber != null && phoneNumber != "" && phoneNumber.charAt(0) == '+'){
				phoneNumber = phoneNumber.replace("+","");
			}
		
			if(phoneNumber != null && phoneNumber != "" && phoneNumber.charAt(3) == '0'){
				phoneNumber = phoneNumber.replace("0","");
			}
		 
			Admin phoneNumberAdmin = adminDao.findByPhoneNumber(phoneNumber);
			if(phoneNumberAdmin != null && !adminToUpdate.equals(phoneNumberAdmin)){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PHONENUMBER_ALREADY_EXIST_ERROR);
			} else if (!isValidNumber && phoneNumber.substring(0, 2).equals("00")){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PREFIX_FORMAT_ERROR);
			} else if (!isJONumber || !isValidNumber){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PHONENUMBER_FORMAT_ERROR);
			} else if(valid){
			adminToUpdate.setPhoneNumber(phoneNumber);
			adminToUpdate.setUpdatedAt(new Date());
			adminDao.save(adminToUpdate);
			Admins jsonAdmin = new Admins(adminToUpdate.getAdminId(), adminToUpdate.getPhoneNumber(), adminToUpdate.getUserName(), adminToUpdate.getFullName(), adminToUpdate.getEmail(), 
										  adminToUpdate.getAdministratorId(), adminToUpdate.getPrivilege(), adminToUpdate.getCreatedAt(), adminToUpdate.getUpdatedAt(), adminToUpdate.getDeletedAt(), adminToUpdate.isStatus());
			response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonAdmin);
			}	
		}
		
		
		if(userName != null && !userName.equals("") && valid){
			Admin userNameAdmin = adminDao.findByUserName(userName);
			if(userName.length() > 20 ){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_USERNAME_LENGTH_LESS_ERROR);
			} else if (userName.length() < 6){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_USERNAME_LENGTH_GREATER_ERROR);
			} else if(userNameAdmin != null && !adminToUpdate.equals(userNameAdmin)){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_USERNAME_ALREADY_EXIST_ERROR);
			} else if(valid){
			adminToUpdate.setUserName(userName);
			adminToUpdate.setUpdatedAt(new Date());
			adminDao.save(adminToUpdate);
			Admins jsonAdmin = new Admins(adminToUpdate.getAdminId(), adminToUpdate.getPhoneNumber(), adminToUpdate.getUserName(), adminToUpdate.getFullName(), adminToUpdate.getEmail(), 
					  adminToUpdate.getAdministratorId(), adminToUpdate.getPrivilege(), adminToUpdate.getCreatedAt(), adminToUpdate.getUpdatedAt(), adminToUpdate.getDeletedAt(), adminToUpdate.isStatus());
			response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonAdmin);
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
			adminToUpdate.setFullName(fullName);
			adminToUpdate.setUpdatedAt(new Date());
			adminDao.save(adminToUpdate);
			Admins jsonAdmin = new Admins(adminToUpdate.getAdminId(), adminToUpdate.getPhoneNumber(), adminToUpdate.getUserName(), adminToUpdate.getFullName(), adminToUpdate.getEmail(), 
					  adminToUpdate.getAdministratorId(), adminToUpdate.getPrivilege(), adminToUpdate.getCreatedAt(), adminToUpdate.getUpdatedAt(), adminToUpdate.getDeletedAt(), adminToUpdate.isStatus());
			response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonAdmin);
			}
		}
		
		if (password != null && !password.equals("") && valid ){
			if(password.length() < 6 ){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PASSWORD_LENGTH_GREATER_ERROR);
			} else if (password.length() > 10){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PASSWORD_LENGTH_LESS_ERROR);
			} else if(valid){
				adminToUpdate.setPassword(password);
				adminToUpdate.setUpdatedAt(new Date());
				adminDao.save(adminToUpdate);
				Admins jsonAdmin = new Admins(adminToUpdate.getAdminId(), adminToUpdate.getPhoneNumber(), adminToUpdate.getUserName(), adminToUpdate.getFullName(), adminToUpdate.getEmail(), 
						  adminToUpdate.getAdministratorId(), adminToUpdate.getPrivilege(), adminToUpdate.getCreatedAt(), adminToUpdate.getUpdatedAt(), adminToUpdate.getDeletedAt(), adminToUpdate.isStatus());
				response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonAdmin);	
			} 
		}
		
		
		if (email != null && !email.equals("") && valid){
			Admin emailAdmin = adminDao.findByEmail(admin.getEmail());
			String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
			if (!email.matches(regex)) {
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_EMAIL_FORMAT_ERROR);
			} else if(emailAdmin != null && !adminToUpdate.equals(emailAdmin)){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_EMAIL_ALREADY_EXIST_ERROR);
			} else if(valid){
				adminToUpdate.setEmail(email);
				adminToUpdate.setUpdatedAt(new Date());
				adminDao.save(adminToUpdate);
				Admins jsonAdmin = new Admins(adminToUpdate.getAdminId(), adminToUpdate.getPhoneNumber(), adminToUpdate.getUserName(), adminToUpdate.getFullName(), adminToUpdate.getEmail(), 
						  adminToUpdate.getAdministratorId(), adminToUpdate.getPrivilege(), adminToUpdate.getCreatedAt(), adminToUpdate.getUpdatedAt(), adminToUpdate.getDeletedAt(), adminToUpdate.isStatus());
				response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonAdmin);	
			}
		}
		
		if (privilege != null && !privilege.equals("") && valid){
			adminToUpdate.setPrivilege(privilege);
			adminToUpdate.setUpdatedAt(new Date());
			adminDao.save(adminToUpdate);
			Admins jsonAdmin = new Admins(adminToUpdate.getAdminId(), adminToUpdate.getPhoneNumber(), adminToUpdate.getUserName(), adminToUpdate.getFullName(), adminToUpdate.getEmail(), 
					  adminToUpdate.getAdministratorId(), adminToUpdate.getPrivilege(), adminToUpdate.getCreatedAt(), adminToUpdate.getUpdatedAt(), adminToUpdate.getDeletedAt(), adminToUpdate.isStatus());
			response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonAdmin);	
		}
		
		

		if(administratorId != 0 && valid){
			Administrator administrator = administratorDao.findByAdministratorId(admin.getAdministratorId());
			if(administrator == null){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_ADMINISTRATOR_NUMBER_ERROR);
			} else if(!administrator.isStatus()){
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_UPDATE_ADMINISTRATOR_ERROR);
			} else if(valid){
				adminToUpdate.setAdministratorId(administratorId);
				adminToUpdate.setUpdatedAt(new Date());
				adminDao.save(adminToUpdate);
				Admins jsonAdmin = new Admins(adminToUpdate.getAdminId(), adminToUpdate.getPhoneNumber(), adminToUpdate.getUserName(), adminToUpdate.getFullName(), adminToUpdate.getEmail(), 
						  adminToUpdate.getAdministratorId(), adminToUpdate.getPrivilege(), adminToUpdate.getCreatedAt(), adminToUpdate.getUpdatedAt(), adminToUpdate.getDeletedAt(), adminToUpdate.isStatus());
				response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonAdmin);	
			
			}
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
