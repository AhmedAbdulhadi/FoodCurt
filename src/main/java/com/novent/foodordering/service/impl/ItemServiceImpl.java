package com.novent.foodordering.service.impl;

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
import com.novent.foodordering.dao.ItemDao;
import com.novent.foodordering.dao.LoginDao;
import com.novent.foodordering.dao.RestaurantDao;
import com.novent.foodordering.dao.UserDao;
import com.novent.foodordering.entity.Item;
import com.novent.foodordering.entity.Logins;
import com.novent.foodordering.entity.Restaurant;
import com.novent.foodordering.entity.Users;
import com.novent.foodordering.service.ItemService;
import com.novent.foodordering.util.CommonsValidator;
import com.novent.foodordering.util.CustomItems;
import com.novent.foodordering.util.ResponseObject;
import com.novent.foodordering.util.ResponseObjectAll;
import com.novent.foodordering.util.ResponseObjectCrud;
import com.novent.foodordering.util.ResponseObjectData;
import com.novent.foodordering.util.ResponseObjectPage;

@Service
@Component
public class ItemServiceImpl  implements ItemService{
	
	@Autowired
	private ItemDao itemDao;
	@Autowired
	private RestaurantDao restaurantDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private LoginDao loginDao;

	
	@Override
	public ResponseObject getAllItems(HttpServletRequest request, String sort) {
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
						
						 List<Item> allItems;
							
							if (sort.equals("Asc")) {
								allItems = itemDao.findAllByOrderByItemIdAsc();
							} else {
								sort = "Desc";
								allItems = itemDao.findAllByOrderByItemIdDesc();
							}
							
							if(!allItems.isEmpty()){
								response = new ResponseObjectAll<Item>(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, allItems);
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
			}
			else {
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
			}
			} else {
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
			}
		
		return response;
	}

	
	
	@Override
	public ResponseObject getItemByStatus(boolean status, HttpServletRequest request, String sort, Pageable pageable) {
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
						Page<Item> allItems;
						if (sort.equals("Asc")) {
							allItems = itemDao.findByStatusOrderByItemIdAsc(status, pageable);
						} else {
							sort = "Desc";
							allItems = itemDao.findByStatusOrderByItemIdDesc(status, pageable);
						}

						if(!allItems.getContent().isEmpty()){
							response = new ResponseObjectPage<Item>(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE,
									allItems.getNumberOfElements(), allItems.isLast(), allItems.getTotalPages(),
									allItems.getTotalElements(), allItems.getNumber(), sort,
									allItems.isFirst(), allItems.getContent());
							
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
			}
			else {
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
			}
			} else {
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
			}
		
		return response;
	}

	@Override
	public ResponseObject getItemById(long itemId, HttpServletRequest request) {
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
			} else {
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
			}
			if(user == null){
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_INCORRECT_TOKEN_ERROR);
			} else if(user.getPassword().equals(login.getPassword())){
				if(user.getIsLoggedIn()){
					if (!isExpire) {
					if((Type.ADMINISTRATOR == user.getRole())||(Type.ADMIN == user.getRole())){
						Item item = itemDao.findByItemId(itemId);
						if (item != null){
							response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, item);
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
			}
			else {
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
			}
			} else {
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
			}
		return response;
	}

	@Override
	public ResponseObject createItems(CustomItems items, HttpServletRequest request) {
		ResponseObject response = null;
		// Get the token
		String token = request.getHeader("token");
		boolean validToken = token != null; 
		HttpSession session = request.getSession();
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
					if((Type.ADMINISTRATOR == user.getRole())||(Type.ADMIN == user.getRole())){	
						Restaurant restaurant = restaurantDao.findById(items.getRestaurantId());
						
				 		boolean valid = (restaurant != null && restaurant.isStatus())  && !items.getItems().isEmpty();
				 		boolean validName = true;
				 		boolean validPrice = true;
						List<Item> item = items.getItems();
						List<Item> restaurantItem = null ;
						if (valid){
							for (Iterator<Item> iterator = item.iterator(); iterator.hasNext();){
								Item value = iterator.next();
								restaurantItem = restaurant.getItems();
								if(value.getItemName() == null || value.getItemName().equals("")){
									valid = false;
									validName = false;
								} else if (value.getPrice() == 0){
									valid = false;
									validPrice = false;
								} else {
									restaurantItem.add(value);
									itemDao.save(value);
								}
							}
						}
						if(items.getRestaurantId() == 0){
							valid = false;
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_RESTAURANTID_REQUIRED_ERROR);
						} else if(restaurant == null ){
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_NO_RESTAURANT_ERROR);
						} else if (!restaurant.isStatus()){
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_UPDATE_RESTAURANT_ERROR);
						} else if (!validName){
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_ITEMNAME_REQUIRED_ERROR);
						} else if (!validPrice){
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PRICE_REQUIRED_ERROR);
						} else if(valid){
							restaurant.setItems(restaurantItem);
							restaurantDao.save(restaurant);
							response = new ResponseObject(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_CREATE_CODE, ResponseMessage.SUCCESS_CREATING_MESSAGE);
						} else response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_CREATING_MESSAGE);
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
			}
			else {
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
			}
			} else {
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
			}
		return response;
	}

	@Override
	public ResponseObject updateItem(long itemId, Item item, HttpServletRequest request) {
		ResponseObject response = null;
		// Get the token
		String token = request.getHeader("token");
		boolean validToken = token != null;
		HttpSession session = request.getSession();
		boolean isExpire = true;
						
		// if the token exist
		if(validToken){
			Logins login = loginDao.findByToken(token);
			Users user = null;
			Item itemToUpdate = null;
			if(login != null){
				 user = userDao.findByUserName(login.getUserName());
				 itemToUpdate = itemDao.findByItemId(itemId);
				 isExpire = CommonsValidator.isExpire(login.getUserName(), request);
			} 
			if(user == null){
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_INCORRECT_TOKEN_ERROR);
			} else if(itemToUpdate == null){
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_NO_ITEM_ERROR);
			} else if(user.getPassword().equals(login.getPassword())){
				if(user.getIsLoggedIn()){
					if (!isExpire) {
					if((Type.ADMINISTRATOR == user.getRole())||(Type.ADMIN == user.getRole())){

						
						String itemName = item.getItemName();
						double Price = item.getPrice();
						String description = item.getDescription();
						
						boolean valid = ((itemToUpdate != null && itemToUpdate.isStatus()) && item != null);
						
						 if (!itemToUpdate.isStatus()){
							valid = false ;
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_UPDATE_ITEM_ERROR);
						}
						
						if (itemName != null && !itemName.equals("") && valid){
							itemToUpdate.setItemName(itemName);
							itemToUpdate.setUpdatedAt(new Date());
							itemDao.save(itemToUpdate);
							response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_CREATE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, itemToUpdate);
							}
						
						if(Price != 0.0 && valid){
							itemToUpdate.setPrice(item.getPrice());
							itemToUpdate.setUpdatedAt(new Date());
							itemDao.save(itemToUpdate);
							response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_CREATE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, itemToUpdate);
							}
						
						
						if (description != null && !description.equals("") && valid){
							itemToUpdate.setDescription(item.getDescription());
							itemToUpdate.setUpdatedAt(new Date());
							itemDao.save(itemToUpdate);
							response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_CREATE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, itemToUpdate);
							}
						

					} else {
						response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
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
		}
		return response;
	
	}

	@Override
	public ResponseObject deleteItem(long itemId, HttpServletRequest request) {
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
						Item item = itemDao.findByItemId(itemId);
						if(item != null && item.isStatus()){
							item.setStatus(false);
							item.setDeletedAt(new Date());
							itemDao.save(item);
							response = new ResponseObjectCrud(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_DELETTING_MESSAGE, itemId);
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
		}
		return response;
	}
}