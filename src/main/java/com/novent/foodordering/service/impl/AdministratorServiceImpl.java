package com.novent.foodordering.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import com.novent.foodordering.entity.Administrator.Privilege;
import com.novent.foodordering.service.AdministratorService;
import com.novent.foodordering.util.CustomAdministrators;
import com.novent.foodordering.util.CommonsValidator;
import com.novent.foodordering.util.ResponseObject;
import com.novent.foodordering.util.ResponseObjectAll;
import com.novent.foodordering.util.ResponseObjectCrud;
import com.novent.foodordering.util.ResponseObjectData;
import com.novent.foodordering.util.ResponseObjectPage;

@Service
@Component
public class AdministratorServiceImpl implements AdministratorService{
	
	@Autowired
	private AdministratorDao administratorDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private LoginDao loginDao;
	

	@Override
	public ResponseObject getAllAdministrators(HttpServletRequest request, String sort) {
		ResponseObject response = null;
		// Get the token
		String token = request.getHeader("token");
		HttpSession session = request.getSession();
		boolean valid = token != null; 
		boolean isExpire = true;
				
		// if the token exist
		if(valid){
			Logins login = loginDao.findByToken(token);
			Users user = null;
			if(login != null){
				 user = userDao.findByUserName(login.getUserName());
				 isExpire = CommonsValidator.isExpire(login.getUserName(), request);
		}
			if(user == null){
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_INCORRECT_TOKEN_ERROR);
			} else if(user.getPassword().equals(login.getPassword())){
			if(user.getIsLoggedIn()){
				if (!isExpire) {
				if(Type.ADMINISTRATOR == user.getRole()){
					 List<Administrator> allAdministrators;
						
						if (sort.equals("Asc")) {
							allAdministrators = administratorDao.findAllByOrderByIdAsc();
						} else {
							sort = "Desc";
							allAdministrators = administratorDao.findAllByOrderByIdDesc();
						}
						
						if(!allAdministrators.isEmpty()){
							List<CustomAdministrators> jsonAdministrator = new ArrayList<CustomAdministrators>();
							for (Iterator<Administrator> iterator = allAdministrators.iterator(); iterator.hasNext();){
								Administrator administrator = iterator.next();
								
								jsonAdministrator.add(new CustomAdministrators(administrator.getId(),  administrator.getPhoneNumber(), administrator.getUserName(), administrator.getFullName(), administrator.getEmail(), 
										administrator.getPrivilege(), administrator.getCreatedAt(), administrator.getUpdatedAt(), administrator.getDeletedAt(), administrator.isStatus()));
							}
							response = new ResponseObjectAll<CustomAdministrators>(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, jsonAdministrator);
						} else {
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_GETTING_MESSAGE);
						}
					
				} else {
					response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTHORIZATION_CODE, ResponseMessage.FAILED_AUTHORIZATION_MESSAGE);
				}
				} else {
					session.removeAttribute(login.getUserName());
					user.setIsLoggedIn(false);
					userDao.save(user);
					login.setToken("123");
					loginDao.save(login);
					response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_SESSION_TIMEOUT_ERROR);
				}
			} else {
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
			}
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
		}
	} else {
		response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
	}
		return response;
	}
	

	@Override
	public ResponseObject getAdministratorsByStatus(boolean status, HttpServletRequest request, String sort, Pageable pageable) {
		ResponseObject response = null;
		// Get the token
		String token = request.getHeader("token");
		HttpSession session = request.getSession();
		boolean valid = token != null; 
		boolean isExpire = true;
				
		// if the token exist
		if(valid){
			Logins login = loginDao.findByToken(token);
			Users user = null;
			if(login != null){
				 user = userDao.findByUserName(login.getUserName());
				 isExpire = CommonsValidator.isExpire(login.getUserName(), request);
		}
			if(user == null){
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_INCORRECT_TOKEN_ERROR);
			} else if(user.getPassword().equals(login.getPassword())){
			if(user.getIsLoggedIn()){
				if (!isExpire) {
				if(Type.ADMINISTRATOR == user.getRole()){
					Page<Administrator> allAdministrator;
					if (sort.equals("Asc")) {
						allAdministrator = administratorDao.findByStatusOrderByIdAsc(status, pageable);
					} else {
						sort = "Desc";
						allAdministrator = administratorDao.findByStatusOrderByIdDesc(status, pageable);
					}
					List<CustomAdministrators> jsonAdministrator = new ArrayList<CustomAdministrators>();					
					if(!allAdministrator.getContent().isEmpty()){
						for (Iterator<Administrator> iterator = allAdministrator.iterator(); iterator.hasNext();){
							Administrator administrator = iterator.next();
							
							jsonAdministrator.add(new CustomAdministrators(administrator.getId(),  administrator.getPhoneNumber(), administrator.getUserName(), administrator.getFullName(), administrator.getEmail(), 
									administrator.getPrivilege(), administrator.getCreatedAt(), administrator.getUpdatedAt(), administrator.getDeletedAt(), administrator.isStatus()));
						}	
						response = new ResponseObjectPage<CustomAdministrators>(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE,
								allAdministrator.getNumberOfElements(), allAdministrator.isLast(), allAdministrator.getTotalPages(),
								allAdministrator.getTotalElements(), allAdministrator.getNumber(), sort,
								allAdministrator.isFirst(), jsonAdministrator);

					} else response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_GETTING_MESSAGE);
				} else {
					response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTHORIZATION_CODE, ResponseMessage.FAILED_AUTHORIZATION_MESSAGE);
				}
				} else {
					session.removeAttribute(login.getUserName());
					user.setIsLoggedIn(false);
					userDao.save(user);
					login.setToken("123");
					loginDao.save(login);
					response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_SESSION_TIMEOUT_ERROR);
				}
			} else {
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
			}
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
		}
	} else {
		response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
	}
		return response;
	}

	@Override
	public ResponseObject getAdministratorById(long administratorId, HttpServletRequest request) {
		ResponseObject response = null;
		// Get the token
		String token = request.getHeader("token");
		HttpSession session = request.getSession();
		boolean valid = token != null; 
		boolean isExpire = true;
						
		// if the token exist
		if(valid){
			Logins login = loginDao.findByToken(token);
			Users user = null;
			if(login != null){
				 user = userDao.findByUserName(login.getUserName());
				 isExpire = CommonsValidator.isExpire(login.getUserName(), request);
		}
			if(user == null){
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_INCORRECT_TOKEN_ERROR);
			} else if(user.getPassword().equals(login.getPassword())){
			if(user.getIsLoggedIn()){
				if (!isExpire) {
				if(Type.ADMINISTRATOR == user.getRole()){
					Administrator administrator = administratorDao.findById(administratorId);
					if (administrator != null){
						CustomAdministrators administrators =	new CustomAdministrators(administrator.getId(),  administrator.getPhoneNumber(), administrator.getUserName(), administrator.getFullName(), administrator.getEmail(), 
								administrator.getPrivilege(), administrator.getCreatedAt(), administrator.getUpdatedAt(), administrator.getDeletedAt(), administrator.isStatus());
						response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, administrators);
					} else response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_GETTING_MESSAGE);
				} else {
					response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTHORIZATION_CODE, ResponseMessage.FAILED_AUTHORIZATION_MESSAGE);
				}
				} else {
					session.removeAttribute(login.getUserName());
					user.setIsLoggedIn(false);
					userDao.save(user);
					login.setToken("123");
					loginDao.save(login);
					response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_SESSION_TIMEOUT_ERROR);
				}
			} else {
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
			}
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
		}
	} else {
		response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
	}
		return response;
	}

	@Override
	public ResponseObject createAdministrator(Administrator administrator, HttpServletRequest request) {
		ResponseObject response = null;
		ResponseObject validationResponse = null;
		// Get the token
		String token = request.getHeader("token");
		HttpSession session = request.getSession();
		boolean validToken = token != null; 
		boolean isExpire = true;
								
		// if the token exist
		if(validToken){
			Logins login = loginDao.findByToken(token);
			Users user = null;
			if(login != null){
				 user = userDao.findByUserName(login.getUserName());
				 isExpire = CommonsValidator.isExpire(login.getUserName(), request);
		}
			if(user == null){
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_INCORRECT_TOKEN_ERROR);
			} else if(user.getPassword().equals(login.getPassword())){
			if(user.getIsLoggedIn()){
				if (!isExpire) {
				if(Type.ADMINISTRATOR == user.getRole()){
					
					String phoneNumber = administrator.getPhoneNumber();
					String userName = administrator.getUserName();
					String fullName = administrator.getFullName();
					String password = administrator.getPassword();
					String email = administrator.getEmail();
					Privilege privilege = administrator.getPrivilege();
					long id = 0;
					boolean validValidation = true;
					
					validationResponse = CommonsValidator.validPhoneNumber(phoneNumber);
					if (validationResponse != null)
						response = validationResponse;

					validationResponse = CommonsValidator.validEmail(email);
					if (validationResponse != null)
						response = validationResponse;

					validationResponse = CommonsValidator.validUserName(userName);
					if (validationResponse != null)
						response = validationResponse;

					validationResponse = CommonsValidator.validFullName(fullName);
					if (validationResponse != null)
						response = validationResponse;

					validationResponse = CommonsValidator.validPassword(password);
					if (validationResponse != null)
						response = validationResponse;

					if (response != null)
						validValidation = false;

					if(phoneNumber != null && phoneNumber != "" && phoneNumber.charAt(0) == '+'){
						phoneNumber = phoneNumber.replace("+","");
					}
				
					if(phoneNumber != null && phoneNumber != "" && phoneNumber.charAt(3) == '0'){
						phoneNumber = phoneNumber.replace("0","");
					}
					
					Administrator phoneNumberAdmin = administratorDao.findByPhoneNumber(administrator.getPhoneNumber());
					Administrator userNameAdmin = administratorDao.findByUserName(administrator.getUserName());
					Administrator emailAdmin = administratorDao.findByEmail(administrator.getEmail());
					
					boolean valid = (phoneNumberAdmin == null && userNameAdmin == null && emailAdmin == null) ;
					
					if (privilege == null || privilege.equals("")){
						response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PRIVILAGE_REQUIRED_ERROR);				
					} else if (phoneNumberAdmin != null){
						response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PHONENUMBER_ALREADY_EXIST_ERROR);
					} else if(userNameAdmin != null){
						response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_USERNAME_ALREADY_EXIST_ERROR);
					} else if(emailAdmin != null){
						response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_EMAIL_ALREADY_EXIST_ERROR);
					}
					
					if(valid && validValidation){
						administrator.setRole(Type.ADMINISTRATOR);
						administrator.setPhoneNumber(phoneNumber);
						administratorDao.save(administrator);
						id =administrator.getId();
						response = new ResponseObjectCrud(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_CREATE_CODE, ResponseMessage.SUCCESS_CREATING_MESSAGE, id);
					} 
				} else {
					response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTHORIZATION_CODE, ResponseMessage.FAILED_AUTHORIZATION_MESSAGE);
				}
				} else {
					session.removeAttribute(login.getUserName());
					user.setIsLoggedIn(false);
					userDao.save(user);
					login.setToken("123");
					loginDao.save(login);
					response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_SESSION_TIMEOUT_ERROR);
				}
			} else {
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
			}
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
		}
	} else {
		response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
	}
		return response;
	}

	@Override
	public ResponseObject updateAdministrator(long administratorId, Administrator administrator, HttpServletRequest request) {
		ResponseObject response = null;
		ResponseObject validationResponse = null;
		// Get the token
		String token = request.getHeader("token");
		HttpSession session = request.getSession();
		boolean validToken = token != null; 
		boolean isExpire = true;
										
		// if the token exist
		if(validToken){
			Logins login = loginDao.findByToken(token);
			Administrator loginUser = null;
			Administrator administratorToUpdate = null;
			if(login != null && administratorId != 0){
				loginUser = administratorDao.findByUserName(login.getUserName());
				administratorToUpdate = administratorDao.findById(administratorId);
				isExpire = CommonsValidator.isExpire(login.getUserName(), request);
		}
			
			if(loginUser == null){
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHORIZATION_MESSAGE);
			} else if (administratorToUpdate == null){
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_PATCHING_MESSAGE);
//			} else if(loginUser.getRole() == Type.ADMINISTRATOR && (!loginUser.equals(administratorToUpdate))){
//				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHORIZATION_MESSAGE);
			} else if(loginUser.getRole() != Type.ADMINISTRATOR){
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHORIZATION_MESSAGE);
			} else if(loginUser.getPassword().equals(login.getPassword())){
			if(loginUser.getIsLoggedIn()){
				if (!isExpire) {
				if(Type.ADMINISTRATOR == loginUser.getRole()){

					String phoneNumber = administrator.getPhoneNumber();
					String userName = administrator.getUserName();
					String fullName = administrator.getFullName();
					String password = administrator.getPassword();
					String email = administrator.getEmail();
				    Privilege privilege = administrator.getPrivilege();
				    boolean valid = true;
				    boolean validValidation = true;
				    
				     if (!administratorToUpdate.isStatus() ){
						valid = false ;
						response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_UPDATE_ADMINISTRATOR_ERROR);
					}
			
				    if (phoneNumber != null && phoneNumber != "" && valid){
				    	
				    	validationResponse = CommonsValidator.validPhoneNumber(phoneNumber);
						if (validationResponse != null)
							response = validationResponse;
						
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
						} 
						
						if (response != null)
							validValidation = false;

						if(valid && validValidation){
							administratorToUpdate.setPhoneNumber(phoneNumber);
							administratorToUpdate.setUpdatedAt(new Date());
							administratorDao.save(administratorToUpdate);
							CustomAdministrators jsonAdministrators =	new CustomAdministrators(administratorToUpdate.getId(),  administratorToUpdate.getPhoneNumber(), administratorToUpdate.getUserName(), administratorToUpdate.getFullName(), administrator.getEmail(), 
									administratorToUpdate.getPrivilege(), administratorToUpdate.getCreatedAt(), administratorToUpdate.getUpdatedAt(), administratorToUpdate.getDeletedAt(), administratorToUpdate.isStatus());
							response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonAdministrators);
						}	
					}
				    
				    if(userName != null && !userName.equals("") && valid){
						Administrator userNameAdministrator = administratorDao.findByUserName(userName);
						
						validationResponse = CommonsValidator.validUserName(userName);
						if (validationResponse != null)
							response = validationResponse;
						
						if(userNameAdministrator != null && !administratorToUpdate.equals(userNameAdministrator)){
							valid = false;
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_USERNAME_ALREADY_EXIST_ERROR);
						} 

						if (response != null)
							validValidation = false;

						 if(valid && validValidation){
							administratorToUpdate.setUserName(userName);
							administratorToUpdate.setUpdatedAt(new Date());
							administratorDao.save(administratorToUpdate);
							CustomAdministrators jsonAdministrators =	new CustomAdministrators(administratorToUpdate.getId(),  administratorToUpdate.getPhoneNumber(), administratorToUpdate.getUserName(), administratorToUpdate.getFullName(), administrator.getEmail(), 
									                                               administratorToUpdate.getPrivilege(), administratorToUpdate.getCreatedAt(), administratorToUpdate.getUpdatedAt(), administratorToUpdate.getDeletedAt(), administratorToUpdate.isStatus());
							response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonAdministrators);	
							}
					}
				    
				    if(fullName != null && !fullName.equals("") && valid){

				    	validationResponse = CommonsValidator.validFullName(fullName);
						if (validationResponse != null)
							response = validationResponse;
						
						if (response != null)
							validValidation = false;

				    	if(valid && validValidation){
							administratorToUpdate.setFullName(fullName);
							administratorToUpdate.setUpdatedAt(new Date());
							administratorDao.save(administratorToUpdate);
							CustomAdministrators jsonAdministrators =	new CustomAdministrators(administratorToUpdate.getId(),  administratorToUpdate.getPhoneNumber(), administratorToUpdate.getUserName(), administratorToUpdate.getFullName(), administratorToUpdate.getEmail(), 
									                                               administratorToUpdate.getPrivilege(), administratorToUpdate.getCreatedAt(), administratorToUpdate.getUpdatedAt(), administratorToUpdate.getDeletedAt(), administratorToUpdate.isStatus());
							response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonAdministrators);
							}
					}
				    
				    
				    if (password != null && !password.equals("") && valid){

						validationResponse = CommonsValidator.validPassword(password);
						if (validationResponse != null)
							response = validationResponse;
						
						if (response != null)
							validValidation = false;

				    	if(valid && validValidation){
							administratorToUpdate.setPassword(password);
							administratorToUpdate.setUpdatedAt(new Date());
							administratorDao.save(administratorToUpdate);
							CustomAdministrators jsonAdministrators =	new CustomAdministrators(administratorToUpdate.getId(),  administratorToUpdate.getPhoneNumber(), administratorToUpdate.getUserName(), administratorToUpdate.getFullName(), administratorToUpdate.getEmail(), 
			                        administratorToUpdate.getPrivilege(), administratorToUpdate.getCreatedAt(), administratorToUpdate.getUpdatedAt(), administratorToUpdate.getDeletedAt(), administratorToUpdate.isStatus());
							response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonAdministrators);	
							} 
					}
				    
				    
				    if (email != null && !email.equals("") && valid){
						Administrator emailAdmin = administratorDao.findByEmail(email);
						
						validationResponse = CommonsValidator.validEmail(email);
						if (validationResponse != null)
							response = validationResponse;

						if(emailAdmin != null && !administratorToUpdate.equals(emailAdmin)){
							valid = false;
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_EMAIL_ALREADY_EXIST_ERROR);
						} 
						if (response != null)
							validValidation = false;

						if(valid && validValidation){
							administratorToUpdate.setEmail(email);
							administratorToUpdate.setUpdatedAt(new Date());
							administratorDao.save(administratorToUpdate);
							CustomAdministrators jsonAdministrators =	new CustomAdministrators(administratorToUpdate.getId(),  administratorToUpdate.getPhoneNumber(), administratorToUpdate.getUserName(), administratorToUpdate.getFullName(), administratorToUpdate.getEmail(), 
			                        administratorToUpdate.getPrivilege(), administratorToUpdate.getCreatedAt(), administratorToUpdate.getUpdatedAt(), administratorToUpdate.getDeletedAt(), administratorToUpdate.isStatus());
							response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonAdministrators);	
							}
					}
				    
				    
				    if (privilege != null && !privilege.equals("") && valid){
				    	administratorToUpdate.setPrivilege(privilege);
				    	administratorToUpdate.setUpdatedAt(new Date());
				    	administratorDao.save(administratorToUpdate);
				    	CustomAdministrators jsonAdministrators =	new CustomAdministrators(administratorToUpdate.getId(),  administratorToUpdate.getPhoneNumber(), administratorToUpdate.getUserName(), administratorToUpdate.getFullName(), administratorToUpdate.getEmail(), 
			                    administratorToUpdate.getPrivilege(), administratorToUpdate.getCreatedAt(), administratorToUpdate.getUpdatedAt(), administratorToUpdate.getDeletedAt(), administratorToUpdate.isStatus());
				    	response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonAdministrators);
				    	}

				} else {
					response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTHORIZATION_CODE, ResponseMessage.FAILED_AUTHORIZATION_MESSAGE);
				}
				} else {
					session.removeAttribute(login.getUserName());
					loginUser.setIsLoggedIn(false);
					userDao.save(loginUser);
					login.setToken("123");
					loginDao.save(login);
					response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_SESSION_TIMEOUT_ERROR);
				}
			} else {
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
			}
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
		}
	} else {
		response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
	}	
		return response;
	}


	@Override
	public ResponseObject deleteAdministrator(long administratorId, HttpServletRequest request) {
		ResponseObject response = null;
		// Get the token
		String token = request.getHeader("token");
		HttpSession session = request.getSession();
		boolean valid = token != null; 
		boolean isExpire = true;
						
		// if the token exist
		if(valid){
			Logins login = loginDao.findByToken(token);
			Administrator user = null;
			Administrator admin = null;
			if(login != null && administratorId != 0){
				 user = administratorDao.findByUserName(login.getUserName());
				 admin = administratorDao.findById(administratorId);
				 isExpire = CommonsValidator.isExpire(login.getUserName(), request);
		}
			if(user == null){
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHORIZATION_MESSAGE);
			} else if (admin == null){
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_PATCHING_MESSAGE);
			} else if(user.getRole() != Type.ADMINISTRATOR){
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHORIZATION_MESSAGE);
			} else if(user.getPassword().equals(login.getPassword())){
			if(user.getIsLoggedIn()){
				if (!isExpire) {
				if(Type.ADMINISTRATOR == user.getRole()){
					if(admin != null && admin.isStatus()){
						admin.setStatus(false);
						admin.setIsLoggedIn(false);
						admin.setDeletedAt(new Date());
						administratorDao.save(admin);
						response = new ResponseObjectCrud(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_DELETTING_MESSAGE, user.getId());
					} else {
						response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_DELETTING_MESSAGE);
					}
				} else {
					response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTHORIZATION_CODE, ResponseMessage.FAILED_AUTHORIZATION_MESSAGE);
				}
			} else {
				session.removeAttribute(login.getUserName());
				user.setIsLoggedIn(false);
				userDao.save(user);
				login.setToken("123");
				loginDao.save(login);
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_SESSION_TIMEOUT_ERROR);
			}
			} else {
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
			}
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
		}
	} else {
		response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
	}
		return response;
	}
}
