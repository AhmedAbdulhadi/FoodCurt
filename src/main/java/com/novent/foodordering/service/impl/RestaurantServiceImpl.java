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
import com.novent.foodordering.dao.RestaurantDao;
import com.novent.foodordering.entity.Admin;
import com.novent.foodordering.entity.Restaurant;
import com.novent.foodordering.service.RestaurantService;
import com.novent.foodordering.util.ResponseObject;
import com.novent.foodordering.util.ResponseObjectAll;
import com.novent.foodordering.util.ResponseObjectCrud;
import com.novent.foodordering.util.ResponseObjectData;

@Service
@Component
public class RestaurantServiceImpl implements RestaurantService{

	@Autowired
	private RestaurantDao restaurantDao;
	@Autowired
	private AdminDao adminDao;
	
	@Override
	public ResponseObject getAllRestaurant() {
		ResponseObject response = null;
		List<Restaurant> allRestaurants = restaurantDao.findAll();
		if(!allRestaurants.isEmpty()){
			response = new ResponseObjectAll<Restaurant>(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, allRestaurants);
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_GETTING_MESSAGE);
		}
		return response;
	}

	@Override
	public ResponseObject getRestaurantById(long restaurantId) {
		ResponseObject response = null;
		Restaurant restaurant = restaurantDao.findByRestaurantId(restaurantId);
		if (restaurant != null){
			response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, restaurant);
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_GETTING_MESSAGE);
		}
		return response;
	}

	@Override
	public ResponseObject createRestaurant(Restaurant restaurant) {
		ResponseObject response = null;
		PhoneNumber  phone = null;
		boolean isValidNumber = false;
		boolean isJONumber = false;
		long id = 0;

		Restaurant phoneNumberRestaurant = restaurantDao.findByPhoneNumber(restaurant.getPhoneNumber());
		Restaurant userNameRestaurant = restaurantDao.findByUserName(restaurant.getUserName());
		Restaurant nameRestaurant  = restaurantDao.findByRestaurantName(restaurant.getRestaurantName());
		Restaurant emailRestaurant = restaurantDao.findByEmail(restaurant.getEmail());
		Admin admin = adminDao.findByAdminId(restaurant.getAdminId());
		
		boolean valid = ((phoneNumberRestaurant == null )&&(userNameRestaurant == null)&& nameRestaurant == null && emailRestaurant == null && (admin != null && admin.isStatus()));
		
		PhoneNumberUtil pnUtil = PhoneNumberUtil.getInstance();
		String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"; 
		
		String restaurantName = restaurant.getRestaurantName();
		String phoneNumber = restaurant.getPhoneNumber();
		String userName = restaurant.getUserName();
		String password = restaurant.getPassword();
		String email = restaurant.getEmail();
		
		try{
			phone = pnUtil.parse(phoneNumber,"");
			isValidNumber = pnUtil.isValidNumber(phone);
		    isJONumber = pnUtil.getRegionCodeForNumber(phone).equals("JO");
		} catch (Exception e) {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_ERROR);
		}
		

		if (restaurantName == null || restaurantName.equals("")){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_RESTAURANTNAME_REQUIRED_ERROR);				
		} else if (phoneNumber == null || phoneNumber.equals("")){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PHONENUMBER_REQUIRED_ERROR);				
		} else if (userName == null || userName.equals("")){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_USERNAME_REQUIRED_ERROR);				
		}  else if (password == null || password.equals("")){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PASSWORD_REQUIRED_ERROR);				
		}  else if (email == null || email.equals("")){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_EMAIL_ADDRESS_REQUIRED_ERROR);				
		} else if(nameRestaurant != null ){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_RESTAURANT_ALREADY_EXIST_ERROR);			
		} else if(restaurantName.length() < 3 || restaurantName.length() > 15){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_RESTAURANT_NAME_ERROR);			
		} else if(phoneNumber != null && phoneNumber != "" && !isValidNumber && phoneNumber.substring(0, 2).equals("00")){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PREFIX_FORMAT_ERROR);			
		} else if (!isJONumber || !isValidNumber){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PHONENUMBER_FORMAT_ERROR);
		} else if(phoneNumberRestaurant != null ){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PHONENUMBER_ALREADY_EXIST_ERROR);
		} else if(userName.length() > 20 || userName.length() < 6){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_USERNAME_LENGTH_ERROR);
		} else if(userNameRestaurant != null ){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_USERNAME_ALREADY_EXIST_ERROR);
		} else if(password.length() < 6 || password.length() > 10){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PASSWORD_LENGTH_ERROR);
		} else if (!email.matches(regex)) {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_EMAIL_FORMAT_ERROR);
		} else if(emailRestaurant != null){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_EMAIL_ALREADY_EXIST_ERROR);
		} else if(admin == null || !admin.isStatus()){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_NO_ADMIN_ERROR);
		} else if(valid){
			restaurantDao.save(restaurant);
			id =restaurant.getRestaurantId();
			List<Restaurant> restaurants = admin.getRestaurant();
			restaurants.add(restaurant);
			admin.setRestaurant(restaurants);
			adminDao.save(admin);
			response = new ResponseObjectCrud(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_CREATE_CODE, ResponseMessage.SUCCESS_CREATING_MESSAGE, id);
		} else{
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_CREATING_MESSAGE);
		} 
		return response;
	}

	@Override
	public ResponseObject updateRestaurant(long restaurantId, Restaurant restaurant) {
		ResponseObject response = null;
		PhoneNumber  phone = null;
		boolean isValidNumber = false;
		boolean isJONumber = false;
		boolean valid = true ;
		
		Restaurant restaurantToUpdate = restaurantDao.findByRestaurantId(restaurantId);
		String restaurantName = restaurant.getRestaurantName();
		String phoneNumber = restaurant.getPhoneNumber();
		String userName = restaurant.getUserName();
		String password = restaurant.getPassword();
		String email = restaurant.getEmail();
		String rate = restaurant.getRate();
		String workingHours = restaurant.getWorkingHours();
		int numberOfBranches = restaurant.getNumberOfBranches();
		long adminId = restaurant.getAdminId();
		
		if(restaurantToUpdate == null || !restaurantToUpdate.isStatus()){
			valid = false;
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_DELETTING_MESSAGE);
		}
		
		if (restaurantName != null && !restaurantName.equals("") && valid ){
			if(restaurantName.length() < 3 ){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_RESTAURANT_NAME_GREATER_ERROR);			
			} else if (restaurantName.length() > 15){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_RESTAURANT_NAME_LESS_ERROR);			
			} else if (valid){
				restaurantToUpdate.setRestaurantName(restaurantName);;
				restaurantToUpdate.setUpdatedAt(new Date());
				restaurantDao.save(restaurantToUpdate);
				response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, restaurantToUpdate);
				}		
			}
		
		
		PhoneNumberUtil pnUtil = PhoneNumberUtil.getInstance();
		if (phoneNumber != null && !phoneNumber.equals("") && valid ){
			Restaurant phoneNumberRestaurant = restaurantDao.findByPhoneNumber(restaurant.getPhoneNumber());
			try{
				phone = pnUtil.parse(phoneNumber,"");
				isValidNumber = pnUtil.isValidNumber(phone);
			    isJONumber = pnUtil.getRegionCodeForNumber(phone).equals("JO");
			} catch (Exception e) {
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_ERROR);
			}
			if(phoneNumberRestaurant != null && !restaurantToUpdate.equals(phoneNumberRestaurant)){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PHONENUMBER_ALREADY_EXIST_ERROR);
			} else if (!isValidNumber && phoneNumber.substring(0, 2).equals("00")){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PREFIX_FORMAT_ERROR);			
			} else if (!isJONumber || !isValidNumber){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PHONENUMBER_FORMAT_ERROR);
			} else if(valid){
				restaurantToUpdate.setPhoneNumber(phoneNumber);
				restaurantToUpdate.setUpdatedAt(new Date());
				restaurantDao.save(restaurantToUpdate);
				response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, restaurantToUpdate);
			}	
		}
		
		if(userName != null && ! userName.equals("") && valid ){
			Restaurant userNameRestaurant = restaurantDao.findByUserName(restaurant.getUserName());
			if(userName.length() > 20 ){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_USERNAME_LENGTH_LESS_ERROR);
			} else if ( userName.length() < 6){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_USERNAME_LENGTH_GREATER_ERROR);
			} else if(userNameRestaurant != null && !restaurantToUpdate.equals(userNameRestaurant) ){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_USERNAME_ALREADY_EXIST_ERROR);
			} else if (valid){
				restaurantToUpdate.setUserName(userName);
				restaurantToUpdate.setUpdatedAt(new Date());
				restaurantDao.save(restaurantToUpdate);
				response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, restaurantToUpdate);
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
				restaurantToUpdate.setPassword(password);
				restaurantToUpdate.setUpdatedAt(new Date());
				restaurantDao.save(restaurantToUpdate);
				response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, restaurantToUpdate);	
			} 
			
		}
		
		if (email != null && !email.equals("") && valid ){
			Restaurant emailRestaurant = restaurantDao.findByEmail(restaurant.getEmail());
			String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
			if (!email.matches(regex)) {
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_EMAIL_FORMAT_ERROR);
			} else if(emailRestaurant != null && !restaurantToUpdate.equals(emailRestaurant)){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_EMAIL_ALREADY_EXIST_ERROR);
			} else if(valid){
				restaurantToUpdate.setEmail(email);
				restaurantToUpdate.setUpdatedAt(new Date());
				restaurantDao.save(restaurantToUpdate);
				response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, restaurantToUpdate);	
			}
		}
		
		if(numberOfBranches != 0 && valid ){
			restaurantToUpdate.setNumberOfBranches(numberOfBranches);
			restaurantToUpdate.setUpdatedAt(new Date());
			restaurantDao.save(restaurantToUpdate);
			response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, restaurantToUpdate);	
		}
		
		if (rate != null && !rate.equals("") && valid ){
			restaurantToUpdate.setRate(rate);
			restaurantToUpdate.setUpdatedAt(new Date());
			restaurantDao.save(restaurantToUpdate);
			response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, restaurantToUpdate);	
		}
		
		if(workingHours != null && !workingHours.equals("") && valid){
			restaurantToUpdate.setWorkingHours(workingHours);
			restaurantToUpdate.setUpdatedAt(new Date());
			restaurantDao.save(restaurantToUpdate);
			response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, restaurantToUpdate);	
		}
		
		return response;
	}

	@Override
	public ResponseObject deleteRestaurant(long restaurantId) {
		ResponseObject response = null;
		Restaurant restaurant = restaurantDao.findByRestaurantId(restaurantId);
		if(restaurant != null && restaurant.isStatus()){
			restaurant.setStatus(false);
			restaurant.setDeletedAt(new Date());
			restaurantDao.save(restaurant);
			response = new ResponseObjectCrud(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_DELETTING_MESSAGE, restaurantId);
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_DELETTING_MESSAGE);
		}
		return response;
	}

}
