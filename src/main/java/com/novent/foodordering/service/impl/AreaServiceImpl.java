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
import com.novent.foodordering.dao.AreaDao;
import com.novent.foodordering.dao.LoginDao;
import com.novent.foodordering.dao.UserDao;
import com.novent.foodordering.entity.Administrator;
import com.novent.foodordering.entity.Area;
import com.novent.foodordering.entity.Logins;
import com.novent.foodordering.entity.Users;
import com.novent.foodordering.service.AreaService;
import com.novent.foodordering.util.CustomAreas;
import com.novent.foodordering.util.CommonsValidator;
import com.novent.foodordering.util.ResponseObject;
import com.novent.foodordering.util.ResponseObjectAll;
import com.novent.foodordering.util.ResponseObjectCrud;
import com.novent.foodordering.util.ResponseObjectData;
import com.novent.foodordering.util.ResponseObjectPage;

@Service
@Component
public class AreaServiceImpl implements AreaService{
	
	@Autowired
	private AreaDao areaDao;
	@Autowired 
	private AdministratorDao administratorDao;
	@Autowired 
	private UserDao userDao;
	@Autowired 
	private LoginDao loginDao;


	@Override
	public ResponseObject getAllAreas(HttpServletRequest request, String sort) {
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
					if((Type.ADMINISTRATOR == user.getRole())||(Type.ADMIN == user.getRole())){
						 List<Area> allAreas;
							
							if (sort.equals("Asc")) {
								allAreas = areaDao.findAllByOrderByAreaIdAsc();
							} else {
								sort = "Desc";
								allAreas = areaDao.findAllByOrderByAreaIdDesc();
							}
							
							if(!allAreas.isEmpty()){
								List<CustomAreas> jsonAreas = new ArrayList<CustomAreas>(); 
								for (Iterator<Area> iterator = allAreas.iterator(); iterator.hasNext();){
									Area area = iterator.next();
									jsonAreas.add(new CustomAreas(area.getAreaId(), area.getAreaName(), area.getAreaNameAR(), area.getAdministratorId(), area.getAddress(), area.getLongittude(), area.getLattiude(),area.getCreatedAt(), area.getUpdatedAt(), area.getDeletedAt(), area.isStatus()));
								}
								response = new ResponseObjectAll<CustomAreas>(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, jsonAreas);
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
	public ResponseObject getAreaByStatus(boolean status, HttpServletRequest request, String sort, Pageable pageable) {
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
					if((Type.ADMINISTRATOR == user.getRole())||(Type.ADMIN == user.getRole())){
						Page<Area> allAreas;
						if (sort.equals("Asc")) {
							allAreas = areaDao.findByStatusOrderByAreaIdAsc(status, pageable);
						} else {
							sort = "Desc";
							allAreas = areaDao.findByStatusOrderByAreaIdDesc(status, pageable);
						}
						
						if(!allAreas.getContent().isEmpty()){
							List<CustomAreas> jsonAreas = new ArrayList<CustomAreas>(); 
							for (Iterator<Area> iterator = allAreas.iterator(); iterator.hasNext();){
								Area area = iterator.next();
								jsonAreas.add(new CustomAreas(area.getAreaId(), area.getAreaName(), area.getAreaNameAR(), area.getAdministratorId(), area.getAddress(), area.getLongittude(), area.getLattiude(),area.getCreatedAt(), area.getUpdatedAt(), area.getDeletedAt(), area.isStatus()));
							}
							
							response = new ResponseObjectPage<CustomAreas>(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE,
									allAreas.getNumberOfElements(), allAreas.isLast(), allAreas.getTotalPages(),
									allAreas.getTotalElements(), allAreas.getNumber(), sort,
									allAreas.isFirst(), jsonAreas);
							
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
	public ResponseObject getAreaById(long areaId, HttpServletRequest request) {
		ResponseObject response = null;
		// Get the token
		String token = request.getHeader("token");
		boolean valid = token != null; 
		boolean isExpire = true;
		HttpSession session = request.getSession();
				
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
					if((Type.ADMINISTRATOR == user.getRole())||(Type.ADMIN == user.getRole())){
						Area area = areaDao.findByAreaId(areaId);
						if (area != null){
							CustomAreas jsonArea = new CustomAreas(area.getAreaId(), area.getAreaName(), area.getAreaNameAR(), area.getAdministratorId(), area.getAddress(), area.getLongittude(), area.getLattiude(),area.getCreatedAt(), area.getUpdatedAt(), area.getDeletedAt(), area.isStatus());
							response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, jsonArea);
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
	public ResponseObject createArea(Area area, HttpServletRequest request) {
		ResponseObject response = null;
		// Get the token
		String token = request.getHeader("token");
		HttpSession session = request.getSession();
		boolean validToken = token != null; 
		boolean isExpire = true;
						
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
						
								long id = 0;
								Area areaName = areaDao.findByAreaName(area.getAreaName());
								Area areaNameAR = areaDao.findByAreaNameAR(area.getAreaNameAR());
								
								boolean valid = (areaName == null ) ;
								String regex = "^[\u0621-\u064A0-9 ]+$";
								if(area.getAreaName()== null || area.getAreaName().equals("") ){
									valid = false ;
									response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_AREANAME_REQUIRED_ERROR);
								} else if(area.getAreaNameAR() == null || area.getAreaNameAR().equals("") ){
									valid = false ;
									response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_AREANAMEAR_REQUIRED_ERROR);
								} else if(areaName != null ){
									response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_AREANAME_ALREADY_EXIST_ERROR);
								} else if(!area.getAreaNameAR().matches(regex)){
									response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_ARABICNAME_ERROR);
								} else if(areaNameAR != null ){
									response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_AREANAMEAR_ALREADY_EXIST_ERROR);
								} else if(valid){
									area.setAdministratorId(user.getId());
									areaDao.save(area);
									id =area.getAreaId();
									List<Area> areas =user.getAreas();
									areas.add(area);
									user.setAreas(areas);
									administratorDao.save(user);
									response = new ResponseObjectCrud(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_CREATE_CODE, ResponseMessage.SUCCESS_CREATING_MESSAGE, id);
								} else{
									response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_CREATING_MESSAGE);
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
	public ResponseObject updateArea(long areaId, Area area, HttpServletRequest request) {
		ResponseObject response = null;
		// Get the token
		String token = request.getHeader("token");
		HttpSession session = request.getSession();
		boolean validToken = token != null; 
		boolean isExpire = true;
				
		// if the token exist
		if(validToken){
			Logins login = loginDao.findByToken(token);
			Users user = null;
			Area areaToUpdate = null;
			if(login != null){
				 user = userDao.findByUserName(login.getUserName());
				 areaToUpdate = areaDao.findByAreaId(areaId);
				 isExpire = CommonsValidator.isExpire(login.getUserName(), request);
			}
			if(user == null){
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_INCORRECT_TOKEN_ERROR);
			} else if(areaToUpdate == null){
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_NO_AREA_ERROR);
			} else if(user.getPassword().equals(login.getPassword())){
				if(user.getIsLoggedIn()){
					if (!isExpire) {
					if(Type.ADMINISTRATOR == user.getRole()){
						boolean valid = true;
						
						String Name = area.getAreaName();
						String areaNameAR = area.getAreaNameAR();
						String address = area.getAddress();
						double longittude = area.getLongittude();
						double lattiude = area.getLattiude();
//						long administratorId = area.getAdministratorId();
						
						
						 if(!areaToUpdate.isStatus()){
							valid = false;
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_UPDATE_AREA_ERROR);
						}
						
						if (Name != null && !Name.equals("") && valid){
							Area areaName = areaDao.findByAreaName(area.getAreaName());
							if(areaName != null && !areaToUpdate.equals(areaName) ){
								valid = false;
								response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_AREANAME_ALREADY_EXIST_ERROR);
							} else if (valid){
								areaToUpdate.setAreaName(Name);
								areaToUpdate.setUpdatedAt(new Date());
								areaDao.save(areaToUpdate);
								CustomAreas jsonArea = new CustomAreas(areaToUpdate.getAreaId(), areaToUpdate.getAreaName(), areaToUpdate.getAreaNameAR(), areaToUpdate.getAdministratorId(), areaToUpdate.getAddress(), areaToUpdate.getLongittude(),
										                   areaToUpdate.getLattiude(),areaToUpdate.getCreatedAt(), areaToUpdate.getUpdatedAt(), areaToUpdate.getDeletedAt(), areaToUpdate.isStatus());
								response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonArea);	
							}
						}
						
						String regex = "^[\u0621-\u064A0-9 ]+$";

						if (areaNameAR != null && !areaNameAR.equals("") && valid){
							Area areaName = areaDao.findByAreaNameAR(area.getAreaNameAR());
							if(areaName != null && !areaToUpdate.equals(areaName) ){
								valid = false;
								response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_AREANAMEAR_ALREADY_EXIST_ERROR);
							} else if (!area.getAreaNameAR().matches(regex)){
								response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_ARABICNAME_ERROR);
							} else if (valid){
								areaToUpdate.setAreaNameAR(Name);
								areaToUpdate.setUpdatedAt(new Date());
								areaDao.save(areaToUpdate);
								CustomAreas jsonArea = new CustomAreas(areaToUpdate.getAreaId(), areaToUpdate.getAreaName(), areaToUpdate.getAreaNameAR(), areaToUpdate.getAdministratorId(), areaToUpdate.getAddress(), areaToUpdate.getLongittude(),
										                   areaToUpdate.getLattiude(),areaToUpdate.getCreatedAt(), areaToUpdate.getUpdatedAt(), areaToUpdate.getDeletedAt(), areaToUpdate.isStatus());
								response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonArea);	
							}
						}
						
//						if (administratorId != 0 && valid ){
//							Administrator administrator = administratorDao.findById(area.getAdministratorId());
//							if(administrator == null){
//								valid = false;
//								response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_ADMINISTRATOR_NUMBER_ERROR);
//							} else if(!administrator.isStatus()){
//								response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_UPDATE_ADMINISTRATOR_ERROR);
//							} else if (valid){
//							areaToUpdate.setAdministratorId(administratorId);
//							areaToUpdate.setUpdatedAt(new Date());
//							areaDao.save(areaToUpdate);
//							Areas jsonArea = new Areas(areaToUpdate.getAreaId(), areaToUpdate.getAreaName(), areaToUpdate.getAreaNameAR(), areaToUpdate.getAdministratorId(), areaToUpdate.getAddress(), areaToUpdate.getLongittude(),
//					                   areaToUpdate.getLattiude(),areaToUpdate.getCreatedAt(), areaToUpdate.getUpdatedAt(), areaToUpdate.getDeletedAt(), areaToUpdate.isStatus());
//							response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonArea);	
//							}
//						}
//						
						if(address != null && !address.equals("") && valid ){
							areaToUpdate.setAddress(address);
							areaToUpdate.setUpdatedAt(new Date());
							areaDao.save(areaToUpdate);
							CustomAreas jsonArea = new CustomAreas(areaToUpdate.getAreaId(), areaToUpdate.getAreaName(), areaToUpdate.getAreaNameAR(), areaToUpdate.getAdministratorId(), areaToUpdate.getAddress(), areaToUpdate.getLongittude(),
					                   areaToUpdate.getLattiude(),areaToUpdate.getCreatedAt(), areaToUpdate.getUpdatedAt(), areaToUpdate.getDeletedAt(), areaToUpdate.isStatus());
							response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonArea);	
							}
						
						if(longittude != 0 && valid ){
							areaToUpdate.setLongittude(longittude);;
							areaToUpdate.setUpdatedAt(new Date());
							areaDao.save(areaToUpdate);
							CustomAreas jsonArea = new CustomAreas(areaToUpdate.getAreaId(), areaToUpdate.getAreaName(), areaToUpdate.getAreaNameAR(), areaToUpdate.getAdministratorId(), areaToUpdate.getAddress(), areaToUpdate.getLongittude(),
					                   areaToUpdate.getLattiude(),areaToUpdate.getCreatedAt(), areaToUpdate.getUpdatedAt(), areaToUpdate.getDeletedAt(), areaToUpdate.isStatus());
							response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonArea);	
							}
						
						if(lattiude != 0 && valid ){
							areaToUpdate.setLattiude(lattiude);
							areaToUpdate.setUpdatedAt(new Date());
							areaDao.save(areaToUpdate);
							CustomAreas jsonArea = new CustomAreas(areaToUpdate.getAreaId(), areaToUpdate.getAreaName(), areaToUpdate.getAreaNameAR(), areaToUpdate.getAdministratorId(), areaToUpdate.getAddress(), areaToUpdate.getLongittude(),
					                   areaToUpdate.getLattiude(),areaToUpdate.getCreatedAt(), areaToUpdate.getUpdatedAt(), areaToUpdate.getDeletedAt(), areaToUpdate.isStatus());
							response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonArea);	
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
	public ResponseObject deleteArea(long areaId, HttpServletRequest request) {
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
			Area area = null;
			if(login != null){
				 user = userDao.findByUserName(login.getUserName());
				 area = areaDao.findByAreaId(areaId);
				 isExpire = CommonsValidator.isExpire(login.getUserName(), request);
			}
			
			if(user == null){
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_INCORRECT_TOKEN_ERROR);
			} else if(area == null){
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_NO_AREA_ERROR);
			} else if(user.getPassword().equals(login.getPassword())){
				if(user.getIsLoggedIn()){
					if (!isExpire) {
					if(Type.ADMINISTRATOR == user.getRole()){
						if(area != null && area.isStatus()){
							area.setStatus(false);
							area.setDeletedAt(new Date());
							areaDao.save(area);
							response = new ResponseObjectCrud(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_DELETTING_MESSAGE, user.getId());
						} else response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_DELETTING_MESSAGE);
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
