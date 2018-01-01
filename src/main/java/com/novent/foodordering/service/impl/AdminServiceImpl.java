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
import com.novent.foodordering.dao.AdminDao;
import com.novent.foodordering.dao.AdministratorDao;
import com.novent.foodordering.dao.LoginDao;
import com.novent.foodordering.dao.UserDao;
import com.novent.foodordering.entity.Admin;
import com.novent.foodordering.entity.Admin.Privilege;
import com.novent.foodordering.entity.Administrator;
import com.novent.foodordering.entity.Logins;
import com.novent.foodordering.entity.Users;
import com.novent.foodordering.service.AdminService;
import com.novent.foodordering.util.CustomAdmins;
import com.novent.foodordering.util.CommonsValidator;
import com.novent.foodordering.util.ResponseObject;
import com.novent.foodordering.util.ResponseObjectAll;
import com.novent.foodordering.util.ResponseObjectCrud;
import com.novent.foodordering.util.ResponseObjectData;
import com.novent.foodordering.util.ResponseObjectPage;

@Service
@Component
public class AdminServiceImpl implements AdminService{
	
	@Autowired
	private AdminDao adminDao;
	@Autowired
	private AdministratorDao administratorDao;
	@Autowired
	private LoginDao loginDao;
	@Autowired
	private UserDao userDao;
	

	@Override
	public ResponseObject getAllAdmins(HttpServletRequest request, String sort) {
		ResponseObject response = null;
		// Get the token
		String token = request.getHeader("token");
		boolean valid = token != null; 
		HttpSession session = request.getSession();
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
							 List<Admin> allAdmins;
							
							if (sort.equals("Asc")) {
								allAdmins = adminDao.findAllByOrderByIdAsc();
							} else {
								sort = "Desc";
								allAdmins = adminDao.findAllByOrderByIdDesc();
							}
							
							if(!allAdmins.isEmpty()){
								List<CustomAdmins> jsonAdmins = new ArrayList<CustomAdmins>(); 
								for (Iterator<Admin> iterator = allAdmins.iterator(); iterator.hasNext();){
									Admin admin = iterator.next();
									jsonAdmins.add(new CustomAdmins(admin.getId(), admin.getPhoneNumber(), admin.getUserName(), admin.getFullName(), admin.getEmail(),
											admin.getAdministratorId(), admin.getPrivilege(), admin.getCreatedAt(), admin.getUpdatedAt(), admin.getDeletedAt(), admin.isStatus()));
								}
								response = new ResponseObjectAll<CustomAdmins>(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, jsonAdmins);
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
	public ResponseObject getAdminByStatus(boolean status, HttpServletRequest request, String sort, Pageable pageable) {
		ResponseObject response = null;
		// Get the token
		String token = request.getHeader("token");
		boolean valid = token != null; 
		HttpSession session = request.getSession();
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
						Page<Admin> allAdmins;
						if (sort.equals("Asc")) {
							allAdmins = adminDao.findByStatusOrderByIdAsc(status, pageable);
						} else {
							sort = "Desc";
							allAdmins = adminDao.findByStatusOrderByIdDesc(status, pageable);
						}
						
						if (!allAdmins.getContent().isEmpty()) {
							List<CustomAdmins> jsonAdmins = new ArrayList<CustomAdmins>();
							for (Iterator<Admin> iterator = allAdmins.iterator(); iterator.hasNext();) {
								Admin admin = iterator.next();
								jsonAdmins.add(new CustomAdmins(admin.getId(), admin.getPhoneNumber(), admin.getUserName(), admin.getFullName(), admin.getEmail(),
										admin.getAdministratorId(), admin.getPrivilege(), admin.getCreatedAt(), admin.getUpdatedAt(), admin.getDeletedAt(), admin.isStatus()));
							}

							response = new ResponseObjectPage<CustomAdmins>(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE,
									allAdmins.getNumberOfElements(), allAdmins.isLast(), allAdmins.getTotalPages(),
									allAdmins.getTotalElements(), allAdmins.getNumber(), sort,
									allAdmins.isFirst(), jsonAdmins);
							
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
	public ResponseObject getAdminById(long adminId, HttpServletRequest request) {
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
				
						Admin admin = adminDao.findById(adminId);
						if (admin != null){
							CustomAdmins jsonAdmin = new CustomAdmins(admin.getId(), admin.getPhoneNumber(), admin.getUserName(), admin.getFullName(), admin.getEmail(), 
					                      admin.getAdministratorId(), admin.getPrivilege(), admin.getCreatedAt(), admin.getUpdatedAt(), admin.getDeletedAt(), admin.isStatus());
							response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, jsonAdmin);
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
	public ResponseObject createAdmin(Admin admin, HttpServletRequest request) {
		ResponseObject response = null;
		ResponseObject validationResponse = null;
		// Get the token
		String token = request.getHeader("token");
		HttpSession session = request.getSession();
		boolean validToken = token != null; 
		boolean isExpire = true;
		boolean validValidation = true;
								
		// if the token exist
		if(validToken){
			Logins login = loginDao.findByToken(token);
			Administrator user = null;
			if(login != null){
				user = (Administrator) userDao.findByUserName(login.getUserName());
				isExpire = CommonsValidator.isExpire(login.getUserName(), request);
			}
			if(user == null){
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_INCORRECT_TOKEN_ERROR);
			} else if(user.getPassword().equals(login.getPassword())){
				if(user.getIsLoggedIn()){
					if (!isExpire) {
					if(Type.ADMINISTRATOR == user.getRole()){
						
						String phoneNumber = admin.getPhoneNumber();
						String userName = admin.getUserName();
						String fullName = admin.getFullName();
						String password = admin.getPassword();
						String email = admin.getEmail();
						Privilege privilege = admin.getPrivilege();
//						long administratorId = admin.getAdministratorId();
						boolean valid = true;
						long id =0;
						
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
						
							Users phoneNumberAdmin = userDao.findByPhoneNumber(phoneNumber);
							Users userNameAdmin = userDao.findByUserName(admin.getUserName());
							Users emailAdmin = userDao.findByEmail(admin.getEmail());
							
							 valid = (phoneNumberAdmin == null  && userNameAdmin == null && emailAdmin == null ) ;
						
						
						 if (privilege == null || privilege.equals("")){
							 valid = false;
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PRIVILAGE_REQUIRED_ERROR);				
						} else if (phoneNumberAdmin != null){
							valid = false;
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PHONENUMBER_ALREADY_EXIST_ERROR);
						} else if(userNameAdmin != null){
							valid = false;
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_USERNAME_ALREADY_EXIST_ERROR);
						} else if(emailAdmin != null){
							valid = false;
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_EMAIL_ALREADY_EXIST_ERROR);
						} 
						 
						 if(valid && validValidation){
							admin.setRole(Type.ADMIN);
							admin.setAdministratorId(user.getId());
							admin.setPhoneNumber(phoneNumber);
							adminDao.save(admin);
							List<Admin> admins = user.getAdmins();
							admins.add(admin);
							user.setAdmins(admins);
							administratorDao.save(user);
							id =admin.getId();
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
	public ResponseObject updateAdmin(long adminId, Admin admin, HttpServletRequest request) {
		ResponseObject response = null;
		ResponseObject validationResponse = null;
		// Get the token
		String token = request.getHeader("token");
		HttpSession session = request.getSession();
		boolean validToken = token != null; 
		boolean valid = true ;
		boolean isExpire = true;
		boolean validValidation = true;
										
		// if the token exist
		if(validToken){
			Logins login = loginDao.findByToken(token);
			Admin adminToUpdate = null;
			Users loginUser = null;
			if(login != null && adminId != 0){
				loginUser = userDao.findByUserName(login.getUserName());
				adminToUpdate = adminDao.findById(adminId);
				isExpire = CommonsValidator.isExpire(login.getUserName(), request);
			} else {
				valid = false;
			}
			
			if(loginUser == null){
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_INCORRECT_TOKEN_ERROR);
			} else if(adminToUpdate == null){
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_PATCHING_MESSAGE);
			} else if(loginUser.getRole() == Type.ADMIN && (!loginUser.equals(adminToUpdate))){
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHORIZATION_MESSAGE);
			} else if(!(loginUser.getRole() == Type.ADMINISTRATOR || loginUser.getRole() == Type.ADMIN)){
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHORIZATION_MESSAGE);
			} else if(loginUser != null && adminToUpdate != null && valid && loginUser.getPassword().equals(login.getPassword())){
						if(loginUser.getIsLoggedIn()){
							if (!isExpire) {
							if(Type.ADMIN == adminToUpdate.getRole()){

								String userName = admin.getUserName();
								String fullName = admin.getFullName();
								String phoneNumber = admin.getPhoneNumber();
								String password = admin.getPassword();
								String email = admin.getEmail();
								Privilege privilege = admin.getPrivilege();
								long administratorId = admin.getAdministratorId();
								
								 if (!adminToUpdate.isStatus()){
									valid = false ;
									response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_UPDATE_ADMIN_ERROR);
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
									
									Users phoneNumberAdmin = userDao.findByPhoneNumber(phoneNumber);
									if (phoneNumberAdmin != null && !adminToUpdate.equals(phoneNumberAdmin)) {
										valid = false;
										response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PHONENUMBER_ALREADY_EXIST_ERROR);
									}
									
									if (response != null)
										validValidation = false;
									
									if(valid && validValidation){
									adminToUpdate.setPhoneNumber(phoneNumber);
									adminToUpdate.setUpdatedAt(new Date());
									adminDao.save(adminToUpdate);
									CustomAdmins jsonAdmin = new CustomAdmins(adminToUpdate.getId(), adminToUpdate.getPhoneNumber(), adminToUpdate.getUserName(), adminToUpdate.getFullName(), adminToUpdate.getEmail(), 
																  adminToUpdate.getAdministratorId(), adminToUpdate.getPrivilege(), adminToUpdate.getCreatedAt(), adminToUpdate.getUpdatedAt(), adminToUpdate.getDeletedAt(), adminToUpdate.isStatus());
									response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonAdmin);
									}	
								}
								
								
								if(userName != null && !userName.equals("") && valid){
									Users userNameAdmin = userDao.findByUserName(userName);
									 if(userNameAdmin != null && !adminToUpdate.equals(userNameAdmin)){
										valid = false;
										response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_USERNAME_ALREADY_EXIST_ERROR);
									} 

									 validationResponse = CommonsValidator.validUserName(userName);
										if (validationResponse != null)
											response = validationResponse;

										if (response != null)
											validValidation = false;

									if(valid && validValidation){
									adminToUpdate.setUserName(userName);
									adminToUpdate.setUpdatedAt(new Date());
									adminDao.save(adminToUpdate);
									CustomAdmins jsonAdmin = new CustomAdmins(adminToUpdate.getId(), adminToUpdate.getPhoneNumber(), adminToUpdate.getUserName(), adminToUpdate.getFullName(), adminToUpdate.getEmail(), 
											  adminToUpdate.getAdministratorId(), adminToUpdate.getPrivilege(), adminToUpdate.getCreatedAt(), adminToUpdate.getUpdatedAt(), adminToUpdate.getDeletedAt(), adminToUpdate.isStatus());
									response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonAdmin);
									}
								}
								
								
								if(fullName != null && !fullName.equals("") && valid){

									validationResponse = CommonsValidator.validFullName(fullName);
									if (validationResponse != null)
										response = validationResponse;

									if (response != null)
										validValidation = false;

									if(valid && validValidation){
									adminToUpdate.setFullName(fullName);
									adminToUpdate.setUpdatedAt(new Date());
									adminDao.save(adminToUpdate);
									CustomAdmins jsonAdmin = new CustomAdmins(adminToUpdate.getId(), adminToUpdate.getPhoneNumber(), adminToUpdate.getUserName(), adminToUpdate.getFullName(), adminToUpdate.getEmail(), 
											  adminToUpdate.getAdministratorId(), adminToUpdate.getPrivilege(), adminToUpdate.getCreatedAt(), adminToUpdate.getUpdatedAt(), adminToUpdate.getDeletedAt(), adminToUpdate.isStatus());
									response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonAdmin);
									}
								}
								
								if (password != null && !password.equals("") && valid ){
									validationResponse = CommonsValidator.validPassword(password);
									if (validationResponse != null)
										response = validationResponse;

									if (response != null)
										validValidation = false;

									if(valid && validValidation){
										adminToUpdate.setPassword(password);
										adminToUpdate.setUpdatedAt(new Date());
										adminDao.save(adminToUpdate);
										CustomAdmins jsonAdmin = new CustomAdmins(adminToUpdate.getId(), adminToUpdate.getPhoneNumber(), adminToUpdate.getUserName(), adminToUpdate.getFullName(), adminToUpdate.getEmail(), 
												  adminToUpdate.getAdministratorId(), adminToUpdate.getPrivilege(), adminToUpdate.getCreatedAt(), adminToUpdate.getUpdatedAt(), adminToUpdate.getDeletedAt(), adminToUpdate.isStatus());
										response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonAdmin);	
									} 
								}
								
								
								if (email != null && !email.equals("") && valid){
									Users emailAdmin = userDao.findByEmail(admin.getEmail());
									
									validationResponse = CommonsValidator.validEmail(email);
									if (validationResponse != null)
										response = validationResponse;

									if(emailAdmin != null && !adminToUpdate.equals(emailAdmin)){
										valid = false;
										response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_EMAIL_ALREADY_EXIST_ERROR);
									}

									if (response != null)
										validValidation = false;
									
									if(valid && validValidation){
										adminToUpdate.setEmail(email);
										adminToUpdate.setUpdatedAt(new Date());
										adminDao.save(adminToUpdate);
										CustomAdmins jsonAdmin = new CustomAdmins(adminToUpdate.getId(), adminToUpdate.getPhoneNumber(), adminToUpdate.getUserName(), adminToUpdate.getFullName(), adminToUpdate.getEmail(), 
												  adminToUpdate.getAdministratorId(), adminToUpdate.getPrivilege(), adminToUpdate.getCreatedAt(), adminToUpdate.getUpdatedAt(), adminToUpdate.getDeletedAt(), adminToUpdate.isStatus());
										response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonAdmin);	
									}
								}
								
								if (privilege != null && !privilege.equals("") && valid){
									adminToUpdate.setPrivilege(privilege);
									adminToUpdate.setUpdatedAt(new Date());
									adminDao.save(adminToUpdate);
									CustomAdmins jsonAdmin = new CustomAdmins(adminToUpdate.getId(), adminToUpdate.getPhoneNumber(), adminToUpdate.getUserName(), adminToUpdate.getFullName(), adminToUpdate.getEmail(), 
											  adminToUpdate.getAdministratorId(), adminToUpdate.getPrivilege(), adminToUpdate.getCreatedAt(), adminToUpdate.getUpdatedAt(), adminToUpdate.getDeletedAt(), adminToUpdate.isStatus());
									response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonAdmin);	
								}
								
								
						
								if(administratorId != 0 && valid){
									Administrator administrator = administratorDao.findById(admin.getAdministratorId());
									if(administrator == null){
										valid = false;
										response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_ADMINISTRATOR_NUMBER_ERROR);
									} else if(!administrator.isStatus()){
										response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_UPDATE_ADMINISTRATOR_ERROR);
									} else if(valid){
										adminToUpdate.setAdministratorId(administratorId);
										adminToUpdate.setUpdatedAt(new Date());
										adminDao.save(adminToUpdate);
										CustomAdmins jsonAdmin = new CustomAdmins(adminToUpdate.getId(), adminToUpdate.getPhoneNumber(), adminToUpdate.getUserName(), adminToUpdate.getFullName(), adminToUpdate.getEmail(), 
												  adminToUpdate.getAdministratorId(), adminToUpdate.getPrivilege(), adminToUpdate.getCreatedAt(), adminToUpdate.getUpdatedAt(), adminToUpdate.getDeletedAt(), adminToUpdate.isStatus());
										response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonAdmin);	
									}
								}
						} else response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTHORIZATION_CODE, ResponseMessage.FAILED_AUTHORIZATION_MESSAGE);

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
	public ResponseObject deleteAdmin(long adminId, HttpServletRequest request) {
		ResponseObject response = null;
		// Get the token
		String token = request.getHeader("token");
		HttpSession session = request.getSession();
		boolean validToken = token != null; 
		boolean isExpire = true;
										
		// if the token exist
		if(validToken){
			Logins login = loginDao.findByToken(token);
			Users loginUser = null;
			Admin admin = null;
			boolean valid = true;
			if(login != null && adminId != 0){
				loginUser = userDao.findByUserName(login.getUserName());
				admin = adminDao.findById(adminId);
				isExpire = CommonsValidator.isExpire(login.getUserName(), request);
			} else {
				valid = false;
			} 
			
			if(loginUser == null){
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_INCORRECT_TOKEN_ERROR);
			} else if(admin == null){
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_PATCHING_MESSAGE);
			} else if(loginUser.getRole() == Type.ADMIN && (!loginUser.equals(admin))){
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHORIZATION_MESSAGE);
			} else if(!(loginUser.getRole() == Type.ADMINISTRATOR || loginUser.getRole() == Type.ADMIN)){
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHORIZATION_MESSAGE);
			} else if(valid && loginUser.getPassword().equals(login.getPassword())){
				if(loginUser.getIsLoggedIn()){
					if (!isExpire) {
					if(Type.ADMIN == admin.getRole()){
						if(admin != null && admin.isStatus()){
							admin.setStatus(false);
							admin.setIsLoggedIn(false);
							admin.setDeletedAt(new Date());
							adminDao.save(admin);
							response = new ResponseObjectCrud(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_DELETTING_MESSAGE, admin.getId());
						} else response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_DELETTING_MESSAGE);
					}  else {
					response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
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
}
