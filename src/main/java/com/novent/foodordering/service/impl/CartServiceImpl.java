package com.novent.foodordering.service.impl;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.novent.foodordering.constatnt.ResponseCode;
import com.novent.foodordering.constatnt.ResponseMessage;
import com.novent.foodordering.constatnt.ResponseStatus;
import com.novent.foodordering.constatnt.Type;
import com.novent.foodordering.dao.CartDao;
import com.novent.foodordering.dao.ItemDao;
import com.novent.foodordering.dao.LoginDao;
import com.novent.foodordering.dao.OrderDao;
import com.novent.foodordering.dao.OrderItemDao;
import com.novent.foodordering.dao.UserDao;
import com.novent.foodordering.entity.Cart;
import com.novent.foodordering.entity.Item;
import com.novent.foodordering.entity.Logins;
import com.novent.foodordering.entity.OrderItem;
import com.novent.foodordering.entity.Orders;
import com.novent.foodordering.entity.Users;
import com.novent.foodordering.service.CartService;
import com.novent.foodordering.util.CustomCarts;
import com.novent.foodordering.util.CommonsValidator;
import com.novent.foodordering.util.ResponseObject;
import com.novent.foodordering.util.ResponseObjectData;

@Service
@Component
public class CartServiceImpl implements CartService {
	
	@Autowired
	public CartDao cartDao;
	@Autowired
	public OrderItemDao orderItemDao;
	@Autowired
	public ItemDao itemDao;
	@Autowired
	public OrderDao orderDao;
	@Autowired
	public UserDao userDao;
	@Autowired
	public LoginDao loginDao;

	@Override
	public ResponseObject getCartById(long cartId, HttpServletRequest request) {
		ResponseObject response = null;
		// Get the token
		String token = request.getHeader("token");
		HttpSession session = request.getSession();
		boolean valid = token != null;
		boolean isExpire = true;

		// if the token exist
		if (valid) {
			Logins login = loginDao.findByToken(token);
			Users user = null;
			if (login != null) {
				user = userDao.findByUserName(login.getUserName());
				isExpire = CommonsValidator.isExpire(login.getUserName(), request);
			}
			if(user == null){
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_INCORRECT_TOKEN_ERROR);
			} else if (user.getPassword().equals(login.getPassword())) {
				if (user.getIsLoggedIn()) {
					if (!isExpire) {
					if ((Type.ADMINISTRATOR == user.getRole()) || (Type.ADMIN == user.getRole())) {
						Cart cart = cartDao.findByCartId(cartId);
						if (cart == null) {
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_GETTING_MESSAGE);
						} else response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, cart);
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
	public ResponseObject updateCart(long cartId, CustomCarts carts, HttpServletRequest request) {
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
			if(login != null){
				 user = userDao.findByUserName(login.getUserName());
				 isExpire = CommonsValidator.isExpire(login.getUserName(), request);
			}
			if(user == null){
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_INCORRECT_TOKEN_ERROR);
			} else if(user.getPassword().equals(login.getPassword())){
				if(user.getIsLoggedIn()){
					if (!isExpire) {
					if((Type.ADMINISTRATOR == user.getRole())||(Type.ADMIN == user.getRole())||(Type.USER == user.getRole())){
						Cart cartToUpdate = cartDao.findByCartId(cartId);
						List<OrderItem> items = carts.getItems();
				        
				        boolean valid = true;
				        boolean isItem = true;
				        double totalPrice = 0;
				        int totalQuantity = 0;
				        
				        
						if(cartToUpdate != null && !items.isEmpty()){
							for (Iterator<OrderItem> iterator = items.iterator(); iterator.hasNext();){
								OrderItem value = iterator.next();
								if (value.getQuantity() == 0){
									valid = false;
								}
								Item item = itemDao.findByItemId(value.getItemId());
								if (item != null){
									value.setPrice(item.getPrice());
									value.setItemName(item.getItemName());
									value.setItemId(value.getItemId());
									orderItemDao.save(value);
								int quantity =value.getQuantity();
								double price = item.getPrice();
								totalPrice +=(quantity*price);
								totalQuantity += quantity;
								} else {
								valid = false;
								isItem = false;
								}
							}
						}
						if (!isItem) {
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_NO_ITEM_ERROR);
						} else if (valid) {
							cartToUpdate.setTotalQuantity(totalQuantity);
							cartToUpdate.setOrderItem(items);
							cartDao.save(cartToUpdate);
//							long cartId = cartToUpdate.getCartId();
							Orders order = orderDao.findByCart(cartToUpdate);
							order.setAmount(totalPrice);
							double afterTax = totalPrice + (totalPrice * order.getTax());
							order.setTotalamount(Double.valueOf(String.format("%,.2f", afterTax)));
							orderDao.save(order);
							response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, cartToUpdate );
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
}