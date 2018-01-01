package com.novent.foodordering.service.impl;

import java.util.Date;
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
import com.novent.foodordering.dao.AreaDao;
import com.novent.foodordering.dao.BranchDao;
import com.novent.foodordering.dao.LoginDao;
import com.novent.foodordering.dao.RestaurantDao;
import com.novent.foodordering.dao.UserDao;
import com.novent.foodordering.entity.Area;
import com.novent.foodordering.entity.Branch;
import com.novent.foodordering.entity.Logins;
import com.novent.foodordering.entity.Restaurant;
import com.novent.foodordering.entity.Users;
import com.novent.foodordering.service.BranchService;
import com.novent.foodordering.util.CommonsValidator;
import com.novent.foodordering.util.ResponseObject;
import com.novent.foodordering.util.ResponseObjectAll;
import com.novent.foodordering.util.ResponseObjectCrud;
import com.novent.foodordering.util.ResponseObjectData;
import com.novent.foodordering.util.ResponseObjectPage;

@Service
@Component
public class BranchServiceImpl implements BranchService{
	
	@Autowired
	private BranchDao branchDao;
	@Autowired
	private RestaurantDao restaurantDao;
	@Autowired
	private AreaDao areaDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private LoginDao loginDao;
	

	@Override
	public ResponseObject getAllBranchs(HttpServletRequest request, String sort) {
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
						List<Branch> allBranchs;
						
						if (sort.equals("Asc")) {
							allBranchs = branchDao.findAllByOrderByBranchIdAsc();
						} else {
							sort = "Desc";
							allBranchs = branchDao.findAllByOrderByBranchIdDesc();
						}
						
						if(!allBranchs.isEmpty()){
							response = new ResponseObjectAll<Branch>(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, allBranchs);
						} else {
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_GETTING_MESSAGE);
						}
						
		
					} else {
						response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHORIZATION_MESSAGE);
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
	public ResponseObject getBranchByStatus(boolean status, HttpServletRequest request, String sort, Pageable pageable) {
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
						Page<Branch> allBranchs;
						if (sort.equals("Asc")) {
							allBranchs = branchDao.findByStatusOrderByBranchIdAsc(status, pageable);
						} else {
							sort = "Desc";
							allBranchs = branchDao.findByStatusOrderByBranchIdDesc(status, pageable);
						}
						
						if(!allBranchs.getContent().isEmpty()){
							response = new ResponseObjectPage<Branch>(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE,
									allBranchs.getNumberOfElements(), allBranchs.isLast(), allBranchs.getTotalPages(),
									allBranchs.getTotalElements(), allBranchs.getNumber(), sort,
									allBranchs.isFirst(), allBranchs.getContent());
							
						} else response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_GETTING_MESSAGE);
					} else {
						response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHORIZATION_MESSAGE);
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
	public ResponseObject getBranchById(long branchId, HttpServletRequest request) {
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
						Branch branch = branchDao.findByBranchId(branchId);
						if (branch != null){
							response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, branch);
						} else response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_GETTING_MESSAGE);
					} else {
						response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHORIZATION_MESSAGE);
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
	public ResponseObject createBranch(Branch branch, HttpServletRequest request) {
		ResponseObject response = null;
		ResponseObject validationResponse = null;
		// Get the token
		String token = request.getHeader("token");
		boolean validToken = token != null; 
		HttpSession session = request.getSession();
		boolean isExpire = true;
		boolean validValidation = true;
										
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
					if((Type.ADMINISTRATOR == user.getRole())||(Type.ADMIN == user.getRole())){
						String branchName = branch.getBranchName();
						String branchNameAR = branch.getBranchNameAR();
						String phoneNumber = branch.getPhoneNumber();
						long restaurantId = branch.getRestaurantId();
						long areaId = branch.getAreaId();
						long id = 0;
						
						validationResponse = CommonsValidator.validPhoneNumber(phoneNumber);
						if (validationResponse != null)
							response = validationResponse;
						
						validationResponse = CommonsValidator.validARName(branchNameAR);
						if (validationResponse != null)
							response = validationResponse;
						
						validationResponse = CommonsValidator.validFullName(branchName);
						if (validationResponse != null)
							response = validationResponse;
						
						if(phoneNumber != null && phoneNumber != "" && phoneNumber.charAt(0) == '+'){
							phoneNumber = phoneNumber.replace("+","");
						}
					
						if(phoneNumber != null && phoneNumber != "" && phoneNumber.charAt(3) == '0'){
							phoneNumber = phoneNumber.replace("0","");
						}
						
						Area area = areaDao.findByAreaId(branch.getAreaId());
						Restaurant restaurant = restaurantDao.findById(branch.getRestaurantId());
						Branch phoneNumberBranch = branchDao.findByPhoneNumber(branch.getPhoneNumber());
						
						boolean valid = (restaurant != null && area != null && branch != null && phoneNumberBranch == null);
						
						
						 if (areaId == 0){
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_AREAID_REQUIRED_ERROR);				
						} else if (restaurantId == 0){
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_RESTAURANTID_REQUIRED_ERROR);				
						}  else if (restaurant == null){
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_NO_RESTAURANT_ERROR);
						} else if (!restaurant.isStatus()){
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_UPDATE_RESTAURANT_ERROR);
						} else if(area == null){
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_NO_AREA_ERROR);
						} else if (!area.isStatus()){
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_UPDATE_AREA_ERROR);
						} else if (phoneNumberBranch != null){
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_PHONENUMBER_ALREADY_EXIST_ERROR);
						} 
						
						if (response != null)
							validValidation = false;
						
						if(valid && validValidation){
							int numOfBranches = restaurant.getNumberOfBranches();
							branch.setPhoneNumber(phoneNumber);
							restaurant.setNumberOfBranches(++numOfBranches);
							List<Branch> branches = restaurant.getBranches();
							branches.add(branch);
							branchDao.save(branch);
							restaurant.setBranches(branches);
							restaurantDao.save(restaurant);
							id = branch.getBranchId();
							List<Branch> branchList = area.getBranches();
							branchList.add(branch);
							area.setBranches(branchList);
							areaDao.save(area);
							response = new ResponseObjectCrud(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_CREATE_CODE, ResponseMessage.SUCCESS_CREATING_MESSAGE, id);
						} else response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_CREATING_MESSAGE);
					} else {
						response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHORIZATION_MESSAGE);
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
	public ResponseObject updateBranch(long branchId, Branch branch, HttpServletRequest request) {
		ResponseObject response = null;
		ResponseObject validationResponse = null;
		// Get the token
		String token = request.getHeader("token");
		boolean validToken = token != null;
		HttpSession session = request.getSession();
		boolean isExpire = true;
		boolean validValidation = true;
								
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
					if((Type.ADMINISTRATOR == user.getRole())||(Type.ADMIN == user.getRole())){
						String branchName = branch.getBranchName();
						String branchNameAR = branch.getBranchNameAR();
						String phoneNumber = branch.getPhoneNumber();
						String rate = branch.getRate();
						String workingHours = branch.getWorkingHours();
						long areaId = branch.getAreaId();
						boolean valid = true;
						
						
						Branch branchToUpdate = branchDao.findByBranchId(branchId);		
						
						if( branchToUpdate == null){
							valid = false;
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_NO_BRANCH_ERROR);
						} else if (!branchToUpdate.isStatus()){
							valid = false;
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_UPDATE_BRANCH_ERROR);
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
							
							Branch branchPhoneNumber = branchDao.findByPhoneNumber(branch.getPhoneNumber());
							if(branchPhoneNumber != null && !branchToUpdate.equals(branchPhoneNumber)){
								valid = false;
								response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PHONENUMBER_ALREADY_EXIST_ERROR);
							}
							
							if (response != null)
								validValidation = false;

							if(valid && validValidation){
								branchToUpdate.setPhoneNumber(phoneNumber);
								branchToUpdate.setUpdatedAt(new Date());
								branchDao.save(branchToUpdate);
								response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, branchToUpdate);
							}	
						}
					    
					    if(areaId != 0 &&  valid ){
						Area area = areaDao.findByAreaId(branch.getAreaId());
						if(area == null || !area.isStatus()){
							valid = false;
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_NO_AREA_ERROR);
							} if (valid){
								branchToUpdate.setAreaId(areaId);
								branchToUpdate.setUpdatedAt(new Date());
								branchDao.save(branchToUpdate);
								response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, branchToUpdate);
							
							}
					    }
					    
					    if(branchName != null && !branchName.equals("") && valid ){
					    	validationResponse = CommonsValidator.validFullName(branchName);
							if (validationResponse != null)
								response = validationResponse;
							
							if (response != null)
								validValidation = false;
					    	
					    	if (valid && validValidation){
								branchToUpdate.setBranchName(branchName);
								branchToUpdate.setUpdatedAt(new Date());
								branchDao.save(branchToUpdate);
								response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, branchToUpdate);
							}	
					    }
					    
					    
					    if(branchNameAR != null && !branchNameAR.equals("") && valid ){
					    	validationResponse = CommonsValidator.validARName(branchNameAR);
							if (validationResponse != null)
								response = validationResponse;
							
							if (response != null)
								validValidation = false;
					    	
					    	if (valid && validValidation){
								branchToUpdate.setBranchNameAR(branchName);
								branchToUpdate.setUpdatedAt(new Date());
								branchDao.save(branchToUpdate);
								response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, branchToUpdate);
							}	
					    }
					    
					    
					    if (rate != null && !rate.equals("") && valid){
					    	branchToUpdate.setRate(rate);
					    	branchToUpdate.setUpdatedAt(new Date());
					    	branchDao.save(branchToUpdate);
					    	response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, branchToUpdate);
					    }	
					    
					    if (workingHours != null && !workingHours.equals("") && valid){
					    	branchToUpdate.setWorkingHours(workingHours);
					    	branchToUpdate.setUpdatedAt(new Date());
					    	branchDao.save(branchToUpdate);
					    	response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, branchToUpdate);
					    }

					} else {
						response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHORIZATION_MESSAGE);
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
	public ResponseObject deleteBranch(long branchId, HttpServletRequest request) {
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
						Branch branch = branchDao.findByBranchId(branchId);
						if(branch != null && branch.isStatus()){
							branch.setStatus(false);
							branch.setDeletedAt(new Date());
							branchDao.save(branch);
							response = new ResponseObjectCrud(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_DELETTING_MESSAGE, branchId);
						} else response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_DELETTING_MESSAGE);
					} else {
						response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHORIZATION_MESSAGE);
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
	public ResponseObject getBranchByRestaurantId(long restaurantId, HttpServletRequest request) {
		ResponseObject response = null;
		// Get the token
		String token = request.getHeader("token");
		boolean valid = token != null; 
										
		// if the token exist
		if(valid){
			Logins login = loginDao.findByToken(token);
			Users user = null;
			if(login != null){
				 user = userDao.findByUserName(login.getUserName());
			}
			if(user == null){
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_INCORRECT_TOKEN_ERROR);
			} else if(user.getPassword().equals(login.getPassword())){
				if(user.getIsLoggedIn()){
					if((Type.ADMINISTRATOR == user.getRole())||(Type.ADMIN == user.getRole())){
						List<Branch> Branchs = branchDao.findByRestaurantId(restaurantId);
						if(!Branchs.isEmpty()){
							response = new ResponseObjectAll<Branch>(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, Branchs);
						} else response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_GETTING_MESSAGE);
					} else {
						response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHORIZATION_MESSAGE);
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
