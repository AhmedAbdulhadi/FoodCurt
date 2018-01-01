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
import com.novent.foodordering.dao.LoginDao;
import com.novent.foodordering.dao.RestaurantDao;
import com.novent.foodordering.dao.UserDao;
import com.novent.foodordering.entity.Admin;
import com.novent.foodordering.entity.Logins;
import com.novent.foodordering.entity.Restaurant;
import com.novent.foodordering.entity.Users;
import com.novent.foodordering.service.RestaurantService;
import com.novent.foodordering.util.CommonsValidator;
import com.novent.foodordering.util.ResponseObject;
import com.novent.foodordering.util.ResponseObjectAll;
import com.novent.foodordering.util.ResponseObjectCrud;
import com.novent.foodordering.util.ResponseObjectData;
import com.novent.foodordering.util.ResponseObjectPage;
import com.novent.foodordering.util.CustomRestaurants;

@Service
@Component
public class RestaurantServiceImpl implements RestaurantService{

	@Autowired
	private RestaurantDao restaurantDao;
	@Autowired
	private AdminDao adminDao;
	@Autowired
	private LoginDao loginDao;
	@Autowired
	private UserDao userDao;
	
	@Override
	public ResponseObject getAllRestaurants(String sort) {
		ResponseObject response = null;
		 List<Restaurant> allRestaurants;
		 
		System.out.println( System.getenv("ENV"));
		System.out.println( System.getProperty("ENV"));
		 
		if (sort.equals("Asc")) {
			allRestaurants = restaurantDao.findAllByOrderByIdAsc();
		} else {
			sort = "Desc";
			allRestaurants = restaurantDao.findAllByOrderByIdDesc();
		}
		
		if(!allRestaurants.isEmpty()){
			List<CustomRestaurants> jsonRestaurants = new ArrayList<CustomRestaurants>(); 
			for (Iterator<Restaurant> iterator = allRestaurants.iterator(); iterator.hasNext();){
				Restaurant restaurant = iterator.next();
				jsonRestaurants.add(new CustomRestaurants(restaurant.getId(), restaurant.getRestaurantName(), restaurant.getRestaurantNameAR(), restaurant.getPhoneNumber(), restaurant.getUserName(), restaurant.getEmail(),
						                            restaurant.getAdminId(), restaurant.getNumberOfBranches(), restaurant.getRate(), restaurant.getWorkingHours(), restaurant.getCreatedAt(),
						                            restaurant.getUpdatedAt(), restaurant.getDeletedAt(),restaurant.isStatus()));
			}
			response = new ResponseObjectAll<CustomRestaurants>(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, jsonRestaurants);
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_GETTING_MESSAGE);
		}
		return response;
	}
	
	
	@Override
	public ResponseObject getRestaurantByStatus(boolean status, String sort, Pageable pageable) {
		ResponseObject response = null;

		Page<Restaurant> allRestaurants;
		if (sort.equals("Asc")) {
			allRestaurants = restaurantDao.findByStatusOrderByIdAsc(status, pageable);
		} else {
			sort = "Desc";
			allRestaurants = restaurantDao.findByStatusOrderByIdDesc(status, pageable);
		}
		
		if(!allRestaurants.getContent().isEmpty()){
			List<CustomRestaurants> jsonRestaurants = new ArrayList<CustomRestaurants>(); 
			for (Iterator<Restaurant> iterator = allRestaurants.iterator(); iterator.hasNext();){
				Restaurant restaurant = iterator.next();
				jsonRestaurants.add(new CustomRestaurants(restaurant.getId(), restaurant.getRestaurantName(), restaurant.getRestaurantNameAR(), restaurant.getPhoneNumber(), restaurant.getUserName(), restaurant.getEmail(),
						                            restaurant.getAdminId(), restaurant.getNumberOfBranches(), restaurant.getRate(), restaurant.getWorkingHours(), restaurant.getCreatedAt(),
						                            restaurant.getUpdatedAt(), restaurant.getDeletedAt(),restaurant.isStatus()));
			}
			response = new ResponseObjectPage<CustomRestaurants>(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE,
					allRestaurants.getNumberOfElements(), allRestaurants.isLast(), allRestaurants.getTotalPages(),
					allRestaurants.getTotalElements(), allRestaurants.getNumber(), sort,
					allRestaurants.isFirst(), jsonRestaurants);
			
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_GETTING_MESSAGE);
		}
		return response;
	}

	@Override
	public ResponseObject getRestaurantById(long restaurantId, HttpServletRequest request) {
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
						Restaurant restaurant = restaurantDao.findById(restaurantId);
						if (restaurant != null){
							CustomRestaurants restaurants = new CustomRestaurants(restaurant.getId(), restaurant.getRestaurantName(), restaurant.getRestaurantNameAR(), restaurant.getPhoneNumber(), restaurant.getUserName(), restaurant.getEmail(),
				                    restaurant.getAdminId(), restaurant.getNumberOfBranches(), restaurant.getRate(), restaurant.getWorkingHours(), restaurant.getCreatedAt(),
				                    restaurant.getUpdatedAt(), restaurant.getDeletedAt(),restaurant.isStatus());
							response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, restaurants);
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
	public ResponseObject createRestaurant(Restaurant restaurant, HttpServletRequest request) {
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
					if(Type.ADMINISTRATOR == user.getRole()){
						String restaurantName = restaurant.getRestaurantName();
						String restaurantNameAR = restaurant.getRestaurantNameAR();
						String phoneNumber = restaurant.getPhoneNumber();
						String userName = restaurant.getUserName();
						String password = restaurant.getPassword();
						String email = restaurant.getEmail();
						long adminId = restaurant.getAdminId();
						long id = 0;
				
						validationResponse = CommonsValidator.validPhoneNumber(phoneNumber);
						if (validationResponse != null)
							response = validationResponse;

						validationResponse = CommonsValidator.validEmail(email);
						if (validationResponse != null)
							response = validationResponse;

						validationResponse = CommonsValidator.validUserName(userName);
						if (validationResponse != null)
							response = validationResponse;

						validationResponse = CommonsValidator.validARName(restaurantNameAR);
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

						Restaurant phoneNumberRestaurant = restaurantDao.findByPhoneNumber(phoneNumber);
						Restaurant userNameRestaurant = restaurantDao.findByUserName(userName);
						Restaurant nameRestaurant  = restaurantDao.findByRestaurantName(restaurantName);
						Restaurant nameRestaurantAR  = restaurantDao.findByRestaurantNameAR(restaurantNameAR);
						Restaurant emailRestaurant = restaurantDao.findByEmail(email);
						Admin admin = adminDao.findById(adminId);
						
						boolean valid = ((phoneNumberRestaurant == null )&&(userNameRestaurant == null)&& nameRestaurant == null && emailRestaurant == null && (admin != null && admin.isStatus()));

						if (restaurantName == null || restaurantName.equals("")){
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_RESTAURANTNAME_REQUIRED_ERROR);				
						} else if (restaurantNameAR == null || restaurantNameAR.equals("")){
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_RESTAURANTNAMEAR_REQUIRED_ERROR);				
						} else if (adminId == 0){
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_ADMIN_REQUIRED_ERROR);				
						} else if(nameRestaurant != null ){
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_RESTAURANT_ALREADY_EXIST_ERROR);			
						} else if(nameRestaurantAR != null ){
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_RESTAURANTAR_ALREADY_EXIST_ERROR);			
						} else if(restaurantName.length() < 3 || restaurantName.length() > 15){
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_RESTAURANT_NAME_ERROR);			
						} else if(phoneNumberRestaurant != null ){
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PHONENUMBER_ALREADY_EXIST_ERROR);
						} else if(userNameRestaurant != null ){
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_USERNAME_ALREADY_EXIST_ERROR);
						} else if(emailRestaurant != null){
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_EMAIL_ALREADY_EXIST_ERROR);
						} else if(admin == null){
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_NO_ADMIN_ERROR);
						} else if (!admin.isStatus()){
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_UPDATE_ADMIN_ERROR);
						} 
						if(valid && validValidation){
							restaurant.setPhoneNumber(phoneNumber);
							restaurantDao.save(restaurant);
							id =restaurant.getId();
							List<Restaurant> restaurants = admin.getRestaurant();
							restaurants.add(restaurant);
							admin.setRestaurant(restaurants);
							adminDao.save(admin);
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
	public ResponseObject updateRestaurant(long restaurantId, Restaurant restaurant, HttpServletRequest request) {
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
			Restaurant restaurantToUpdate = null;
			if(login != null){
				user = userDao.findByUserName(login.getUserName());
				restaurantToUpdate = restaurantDao.findById(restaurantId);
				isExpire = CommonsValidator.isExpire(login.getUserName(), request);
			}
			if(user == null){
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_INCORRECT_TOKEN_ERROR);
			} else if(user.getPassword().equals(login.getPassword())){
				if(user.getIsLoggedIn()){
					if (!isExpire) {
					if(Type.ADMIN == user.getRole() || Type.ADMINISTRATOR == user.getRole() ){
						String restaurantName = restaurant.getRestaurantName();
						String restaurantNameAR = restaurant.getRestaurantNameAR();
						String phoneNumber = restaurant.getPhoneNumber();
						String userName = restaurant.getUserName();
						String password = restaurant.getPassword();
						String email = restaurant.getEmail();
						String rate = restaurant.getRate();
						String workingHours = restaurant.getWorkingHours();
						int numberOfBranches = restaurant.getNumberOfBranches();
						long adminId = restaurant.getAdminId();
						boolean valid = true ;
						
						if (!restaurantToUpdate.isStatus()){
							valid = false;
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_UPDATE_RESTAURANT_ERROR);
						}
						
						if (restaurantName != null && !restaurantName.equals("") && valid ){
							if(restaurantName.length() < 3 ){
								valid = false;
								response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_RESTAURANT_NAME_GREATER_ERROR);			
							} else if (restaurantName.length() > 15){
								valid = false;
								response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_RESTAURANT_NAME_LESS_ERROR);			
							} else if (valid){
								restaurantToUpdate.setRestaurantName(restaurantName);
								restaurantToUpdate.setUpdatedAt(new Date());
								restaurantDao.save(restaurantToUpdate);
								CustomRestaurants jsonRestaurant = new CustomRestaurants(restaurantToUpdate.getId(), restaurantToUpdate.getRestaurantName(), restaurantToUpdate.getRestaurantNameAR(), restaurantToUpdate.getPhoneNumber(),
										                                  restaurantToUpdate.getUserName(), restaurantToUpdate.getEmail(), restaurantToUpdate.getAdminId(),
										                                  restaurantToUpdate.getNumberOfBranches(), restaurantToUpdate.getRate(), restaurantToUpdate.getWorkingHours(),
										                                  restaurantToUpdate.getCreatedAt(), restaurantToUpdate.getUpdatedAt(), restaurantToUpdate.getDeletedAt(),restaurantToUpdate.isStatus());
								response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonRestaurant);
								}		
							}
						
				
						if (restaurantNameAR != null && !restaurantNameAR.equals("") && valid ){
							validationResponse = CommonsValidator.validARName(restaurantNameAR);
							if (validationResponse != null)
								response = validationResponse;
							
							if (response != null)
								validValidation = false;
							
							if (valid && validValidation){
								restaurantToUpdate.setRestaurantNameAR(restaurantNameAR);
								restaurantToUpdate.setUpdatedAt(new Date());
								restaurantDao.save(restaurantToUpdate);
								CustomRestaurants jsonRestaurant = new CustomRestaurants(restaurantToUpdate.getId(), restaurantToUpdate.getRestaurantName(), restaurantToUpdate.getRestaurantNameAR(), restaurantToUpdate.getPhoneNumber(),
										                                  restaurantToUpdate.getUserName(), restaurantToUpdate.getEmail(), restaurantToUpdate.getAdminId(),
										                                  restaurantToUpdate.getNumberOfBranches(), restaurantToUpdate.getRate(), restaurantToUpdate.getWorkingHours(),
										                                  restaurantToUpdate.getCreatedAt(), restaurantToUpdate.getUpdatedAt(), restaurantToUpdate.getDeletedAt(),restaurantToUpdate.isStatus());
								response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonRestaurant);
								}		
							}
						
						
						if (phoneNumber != null && !phoneNumber.equals("") && valid ){
							
							validationResponse = CommonsValidator.validPhoneNumber(phoneNumber);
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
							
							Restaurant phoneNumberRestaurant = restaurantDao.findByPhoneNumber(phoneNumber);

							
							if(phoneNumberRestaurant != null && !restaurantToUpdate.equals(phoneNumberRestaurant)){
								valid = false;
								response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PHONENUMBER_ALREADY_EXIST_ERROR);
							} 
							
							if(valid && validValidation){
								restaurantToUpdate.setPhoneNumber(phoneNumber);
								restaurantToUpdate.setUpdatedAt(new Date());
								restaurantDao.save(restaurantToUpdate);
								CustomRestaurants jsonRestaurant = new CustomRestaurants(restaurantToUpdate.getId(), restaurantToUpdate.getRestaurantName(), restaurant.getRestaurantNameAR(), restaurantToUpdate.getPhoneNumber(),
				                        restaurantToUpdate.getUserName(), restaurantToUpdate.getEmail(), restaurantToUpdate.getAdminId(),
				                        restaurantToUpdate.getNumberOfBranches(), restaurantToUpdate.getRate(), restaurantToUpdate.getWorkingHours(),
				                        restaurantToUpdate.getCreatedAt(), restaurantToUpdate.getUpdatedAt(), restaurantToUpdate.getDeletedAt(),restaurantToUpdate.isStatus());
								response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonRestaurant);
							}	
						}
						
						if(userName != null && !userName.equals("") && valid ){
							Restaurant userNameRestaurant = restaurantDao.findByUserName(userName);
							
							if(userNameRestaurant != null && !restaurantToUpdate.equals(userNameRestaurant) ){
								valid = false;
								response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_USERNAME_ALREADY_EXIST_ERROR);
							} 

							validationResponse = CommonsValidator.validUserName(userName);
							if (validationResponse != null)
								response = validationResponse;
							
							if (response != null)
								validValidation = false;
							
							if (valid && validValidation){
								restaurantToUpdate.setUserName(userName);
								restaurantToUpdate.setUpdatedAt(new Date());
								restaurantDao.save(restaurantToUpdate);
								CustomRestaurants jsonRestaurant = new CustomRestaurants(restaurantToUpdate.getId(), restaurantToUpdate.getRestaurantName(), restaurant.getRestaurantNameAR(), restaurantToUpdate.getPhoneNumber(),
				                        restaurantToUpdate.getUserName(), restaurantToUpdate.getEmail(), restaurantToUpdate.getAdminId(),
				                        restaurantToUpdate.getNumberOfBranches(), restaurantToUpdate.getRate(), restaurantToUpdate.getWorkingHours(),
				                        restaurantToUpdate.getCreatedAt(), restaurantToUpdate.getUpdatedAt(), restaurantToUpdate.getDeletedAt(),restaurantToUpdate.isStatus());
								response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonRestaurant);
							}	
							
						}
						
						if (password != null && !password.equals("") && valid){
							validationResponse = CommonsValidator.validPassword(password);
							if (validationResponse != null)
								response = validationResponse;
							
							if (response != null)
								validValidation = false;
							
							if(valid && validValidation){
								restaurantToUpdate.setPassword(password);
								restaurantToUpdate.setUpdatedAt(new Date());
								restaurantDao.save(restaurantToUpdate);
								CustomRestaurants jsonRestaurant = new CustomRestaurants(restaurantToUpdate.getId(), restaurantToUpdate.getRestaurantName(), restaurant.getRestaurantNameAR(), restaurantToUpdate.getPhoneNumber(),
				                        restaurantToUpdate.getUserName(), restaurantToUpdate.getEmail(), restaurantToUpdate.getAdminId(),
				                        restaurantToUpdate.getNumberOfBranches(), restaurantToUpdate.getRate(), restaurantToUpdate.getWorkingHours(),
				                        restaurantToUpdate.getCreatedAt(), restaurantToUpdate.getUpdatedAt(), restaurantToUpdate.getDeletedAt(),restaurantToUpdate.isStatus());
								response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonRestaurant);	
							} 
							
						}
						
						if (email != null && !email.equals("") && valid ){
							validationResponse = CommonsValidator.validEmail(email);
							if (validationResponse != null)
								response = validationResponse;
							
							Restaurant emailRestaurant = restaurantDao.findByEmail(restaurant.getEmail());
							 if(emailRestaurant != null && !restaurantToUpdate.equals(emailRestaurant)){
								valid = false;
								response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_EMAIL_ALREADY_EXIST_ERROR);
							}
							 
							 if (response != null)
									validValidation = false;
							 
							 if(valid && validValidation){
								restaurantToUpdate.setEmail(email);
								restaurantToUpdate.setUpdatedAt(new Date());
								restaurantDao.save(restaurantToUpdate);
								CustomRestaurants jsonRestaurant = new CustomRestaurants(restaurantToUpdate.getId(), restaurantToUpdate.getRestaurantName(), restaurant.getRestaurantNameAR(), restaurantToUpdate.getPhoneNumber(),
				                        restaurantToUpdate.getUserName(), restaurantToUpdate.getEmail(), restaurantToUpdate.getAdminId(),
				                        restaurantToUpdate.getNumberOfBranches(), restaurantToUpdate.getRate(), restaurantToUpdate.getWorkingHours(),
				                        restaurantToUpdate.getCreatedAt(), restaurantToUpdate.getUpdatedAt(), restaurantToUpdate.getDeletedAt(),restaurantToUpdate.isStatus());
								response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonRestaurant);	
							}
						}
						
						if(numberOfBranches != 0 && valid ){
							restaurantToUpdate.setNumberOfBranches(numberOfBranches);
							restaurantToUpdate.setUpdatedAt(new Date());
							restaurantDao.save(restaurantToUpdate);
							CustomRestaurants jsonRestaurant = new CustomRestaurants(restaurantToUpdate.getId(), restaurantToUpdate.getRestaurantName(), restaurant.getRestaurantNameAR(), restaurantToUpdate.getPhoneNumber(),
				                    restaurantToUpdate.getUserName(), restaurantToUpdate.getEmail(), restaurantToUpdate.getAdminId(),
				                    restaurantToUpdate.getNumberOfBranches(), restaurantToUpdate.getRate(), restaurantToUpdate.getWorkingHours(),
				                    restaurantToUpdate.getCreatedAt(), restaurantToUpdate.getUpdatedAt(), restaurantToUpdate.getDeletedAt(),restaurantToUpdate.isStatus());
							response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonRestaurant);	
						}
						
						if (rate != null && !rate.equals("") && valid ){
							restaurantToUpdate.setRate(rate);
							restaurantToUpdate.setUpdatedAt(new Date());
							restaurantDao.save(restaurantToUpdate);
							CustomRestaurants jsonRestaurant = new CustomRestaurants(restaurantToUpdate.getId(), restaurantToUpdate.getRestaurantName(), restaurant.getRestaurantNameAR(), restaurantToUpdate.getPhoneNumber(),
				                    restaurantToUpdate.getUserName(), restaurantToUpdate.getEmail(), restaurantToUpdate.getAdminId(),
				                    restaurantToUpdate.getNumberOfBranches(), restaurantToUpdate.getRate(), restaurantToUpdate.getWorkingHours(),
				                    restaurantToUpdate.getCreatedAt(), restaurantToUpdate.getUpdatedAt(), restaurantToUpdate.getDeletedAt(),restaurantToUpdate.isStatus());
							response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonRestaurant);	
						}
						
						if(workingHours != null && !workingHours.equals("") && valid){
							restaurantToUpdate.setWorkingHours(workingHours);
							restaurantToUpdate.setUpdatedAt(new Date());
							restaurantDao.save(restaurantToUpdate);
							CustomRestaurants jsonRestaurant = new CustomRestaurants(restaurantToUpdate.getId(), restaurantToUpdate.getRestaurantName(), restaurant.getRestaurantNameAR(), restaurantToUpdate.getPhoneNumber(),
				                    restaurantToUpdate.getUserName(), restaurantToUpdate.getEmail(), restaurantToUpdate.getAdminId(),
				                    restaurantToUpdate.getNumberOfBranches(), restaurantToUpdate.getRate(), restaurantToUpdate.getWorkingHours(),
				                    restaurantToUpdate.getCreatedAt(), restaurantToUpdate.getUpdatedAt(), restaurantToUpdate.getDeletedAt(),restaurantToUpdate.isStatus());
							response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonRestaurant);	
						}
						
						
						if(adminId != 0 && valid){
						Admin admin = adminDao.findById(adminId);
						if(admin == null){
							valid = false;
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_NO_ADMIN_ERROR);
						} else if (!admin.isStatus()){
							valid = false;
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_UPDATE_ADMIN_ERROR);
						} else if (valid){
							restaurantToUpdate.setAdminId(adminId);
							restaurantToUpdate.setUpdatedAt(new Date());
							restaurantDao.save(restaurantToUpdate);
							CustomRestaurants jsonRestaurant = new CustomRestaurants(restaurantToUpdate.getId(), restaurantToUpdate.getRestaurantName(), restaurant.getRestaurantNameAR(), restaurantToUpdate.getPhoneNumber(),
				                    restaurantToUpdate.getUserName(), restaurantToUpdate.getEmail(), restaurantToUpdate.getAdminId(),
				                    restaurantToUpdate.getNumberOfBranches(), restaurantToUpdate.getRate(), restaurantToUpdate.getWorkingHours(),
				                    restaurantToUpdate.getCreatedAt(), restaurantToUpdate.getUpdatedAt(), restaurantToUpdate.getDeletedAt(),restaurantToUpdate.isStatus());
							response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonRestaurant);
						}
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
	public ResponseObject deleteRestaurant(long restaurantId, HttpServletRequest request) {
		ResponseObject response = null;
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
				if(Type.ADMIN == user.getRole() || Type.ADMINISTRATOR == user.getRole()){
					Restaurant restaurant = restaurantDao.findById(restaurantId);
					if(restaurant != null && restaurant.isStatus()){
						restaurant.setStatus(false);
						restaurant.setDeletedAt(new Date());
						restaurantDao.save(restaurant);
						response = new ResponseObjectCrud(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_DELETTING_MESSAGE, restaurantId);
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