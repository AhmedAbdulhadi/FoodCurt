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
import com.novent.foodordering.dao.BranchDao;
import com.novent.foodordering.dao.CartDao;
import com.novent.foodordering.dao.ItemDao;
import com.novent.foodordering.dao.LoginDao;
import com.novent.foodordering.dao.OrderDao;
import com.novent.foodordering.dao.OrderItemDao;
import com.novent.foodordering.dao.UserDao;
import com.novent.foodordering.entity.Branch;
import com.novent.foodordering.entity.Cart;
import com.novent.foodordering.entity.Item;
import com.novent.foodordering.entity.Logins;
import com.novent.foodordering.entity.OrderItem;
import com.novent.foodordering.entity.Orders;
import com.novent.foodordering.entity.Users;
import com.novent.foodordering.service.OrderService;
import com.novent.foodordering.util.CommonsValidator;
import com.novent.foodordering.util.JsonOrder;
import com.novent.foodordering.util.CustomUser;
import com.novent.foodordering.util.CustomOrder;
import com.novent.foodordering.util.ResponseObject;
import com.novent.foodordering.util.ResponseObjectCrud;
import com.novent.foodordering.util.ResponseObjectData;
import com.novent.foodordering.util.ResponseObjectPage;

@Service
@Component
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private OrderItemDao orderItemDao;
	@Autowired
	private CartDao cartDao;
	@Autowired
	private UserDao userDao;
	@Autowired 
	private BranchDao branchDao;
	@Autowired
	private ItemDao itemDao;
	@Autowired
	private LoginDao loginDao;
	

	@Override
	public ResponseObject getAllOrders(HttpServletRequest request, String sort, Pageable pageable) {
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
					if((Type.ADMINISTRATOR == user.getRole())||(Type.ADMIN == user.getRole())){
						Page<Orders> allOrders = null;
						if (sort.equals("Asc")) {
							allOrders = orderDao.findByStatusOrderByOrderIdAsc(pageable, 0);
						} else {
							sort = "Desc";
							allOrders = orderDao.findByStatusOrderByOrderIdDesc(pageable, 0);
						}
						
						if(!allOrders.getContent().isEmpty()){
							List<JsonOrder> jsonOrder = new ArrayList<JsonOrder>(); 
							
							for (Iterator<Orders> iterator = allOrders.iterator(); iterator.hasNext();){
								Orders order = iterator.next();
								CustomUser jsonUser = new CustomUser(user.getId(), user.getPhoneNumber(), user.getPhoneNumber());
								jsonOrder.add(new JsonOrder(order.getOrderId(), order.getTakeAway(), order.getNumberOfChair(), order.getTotalamount(), order.getAmount(),
				                        order.getTax(), order.getBranchId(), order.getCreatedAt(), order.getUpdatedAt(), order.getDeletedAt(), order.getStatus(),
				                        order.getStatusName(), order.getCart(), jsonUser));
							}
							response = new ResponseObjectPage<JsonOrder>(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE,
									allOrders.getNumberOfElements(), allOrders.isLast(), allOrders.getTotalPages(),
									allOrders.getTotalElements(), allOrders.getNumber(), sort,
									allOrders.isFirst(), jsonOrder);

						} else {
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_GETTING_MESSAGE);
						}
					}else {
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
	public ResponseObject getOrderById(long orderId, HttpServletRequest request) {
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
							if((Type.ADMINISTRATOR == user.getRole())||(Type.ADMIN == user.getRole())){
								Orders order = orderDao.findByOrderId(orderId);

								if (order != null && user != null){
									CustomUser jsonUser = new CustomUser(user.getId(), user.getPhoneNumber(), user.getPhoneNumber());
									JsonOrder jsonOder = new JsonOrder(order.getOrderId(), order.getTakeAway(), order.getNumberOfChair(), order.getTotalamount(), order.getAmount(),
											                           order.getTax(), order.getBranchId(), order.getCreatedAt(), order.getUpdatedAt(), order.getDeletedAt(), order.getStatus(),
											                           order.getStatusName(), order.getCart(), jsonUser);
									response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, jsonOder );
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
	public ResponseObject createOrder(CustomOrder order, HttpServletRequest request) {
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
			if(login != null){
				 loginUser = userDao.findByUserName(login.getUserName());
				 isExpire = CommonsValidator.isExpire(login.getUserName(), request);
			}
			if(loginUser == null){
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_INCORRECT_TOKEN_ERROR);
			} else if(loginUser.getPassword().equals(login.getPassword())){
				if(loginUser.getIsLoggedIn()){
					if (!isExpire) {
					if((Type.ADMINISTRATOR == loginUser.getRole())||(Type.USER == loginUser.getRole())){

		long id = 0;
		boolean isItem = true;
		double totalPrice = 0;
		
		Users user = userDao.findById(order.getUserId());
		Branch branch = branchDao.findByBranchId(order.getBranchId());
				
		
		long userId = order.getUserId();
		long branchId = order.getBranchId();
		int numberOfChair = order.getNumberOfChair();
		boolean takeAway = order.getTakeAway();
		List<OrderItem> items = order.getItems();
 		boolean validQuen = true;
 		boolean validItem = true;
 		int totalQuantity = 0;
 		
 		
		boolean valid = order != null && user != null && branch != null && userId != 0 && branchId != 0 ;
		if(items != null){
			if (!items.isEmpty() && valid){
				for (Iterator<OrderItem> iterator = items.iterator(); iterator.hasNext();){
					OrderItem value = iterator.next();
					if (value.getQuantity() == 0){
						valid = false;
						validQuen = false;
					}
					Item item = itemDao.findByItemId(value.getItemId());
					if (item != null){
						if(item.isStatus()){
						value.setPrice(item.getPrice());
						value.setItemName(item.getItemName());
						value.setItemId(value.getItemId());
						orderItemDao.save(value);
					int quantity =value.getQuantity();
					double price = item.getPrice();
					totalPrice +=(quantity*price);
					totalQuantity += quantity;
						} else {
							validItem=false;
							valid = false;
						}
					} else {
					valid = false;
					isItem = false;
					}
				}	
			}
		}
		 if(takeAway){
				numberOfChair = 0;
			}
		 if(!validQuen ){
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_QUANTITY_REQUIRED_ERROR);				
		 } else if(userId == 0 ){
			valid = false;
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_USERID_REQUIRED_ERROR);				
		} else if (branchId ==0){
			valid = false;
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_BRANCHID_REQUIRED_ERROR);
		} else if(items == null){
			valid = false;
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_NOORDER_ERROR);
		} else if (user == null) {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_NO_USER_ERROR);
		} else if (!user.isStatus()){ 
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_UPDATE_USER_ERROR);
		} else if (branch == null) {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_NO_BRANCH_ERROR);
		} else if (!branch.isStatus()){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_UPDATE_BRANCH_ERROR);
		} else if (!isItem) {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_NO_ITEM_ERROR);
		} else if (!validItem){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_UPDATE_ITEM_ERROR);
		} else if (valid) {
			Cart cart = new Cart();
			Orders newOrder = new Orders();
			cart.setTotalQuantity(totalQuantity);
			cart.setOrderItem(items);
			cartDao.save(cart);
			
			newOrder.setBranchId(branchId);
			newOrder.setCart(cart);
			newOrder.setNumberOfChair(numberOfChair);
			newOrder.setTakeAway(takeAway);
			newOrder.setAmount(totalPrice);
			double afterTax = totalPrice + (totalPrice * newOrder.getTax());
			newOrder.setTotalamount(Double.valueOf(String.format("%,.2f", afterTax)));
			newOrder.setUser(user);
//			newOrder.setUserId(userId);
			orderDao.save(newOrder);
			id = newOrder.getOrderId();
			response = new ResponseObjectCrud(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_CREATE_CODE,	ResponseMessage.SUCCESS_CREATING_MESSAGE, id);
		} 
					} else {
						response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHORIZATION_MESSAGE);
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
	public ResponseObject updateOrder(long orderId, CustomOrder order, HttpServletRequest request) {
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
							if((Type.ADMINISTRATOR == user.getRole())||(Type.ADMIN == user.getRole())||(Type.USER == user.getRole())){
								boolean isItem = true;
								double totalPrice = 0;
								
								Orders orderToUpdate = orderDao.findByOrderId(orderId);
								List<OrderItem> items = order.getItems();
								
								boolean valid = order != null && orderToUpdate != null;
						
							     int numberOfChair = order.getNumberOfChair();
							     boolean takeAway = order.getTakeAway();
							     
								 if (!items.isEmpty() && valid){
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
											totalPrice += (quantity*price);
											} else {
											valid = false;
											isItem = false;
											}
										}	
								}
								 if(takeAway){
										numberOfChair = 0;
									} 
								 
								if (!isItem) {
									response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_NO_ITEM_ERROR);
								} else if (valid) {
									Cart cart = orderToUpdate.getCart();
									cart.setOrderItem(items);
									cartDao.save(cart);
									
									orderToUpdate.setNumberOfChair(numberOfChair);
									orderToUpdate.setTakeAway(takeAway);
									orderToUpdate.setUpdatedAt(new Date());
									orderDao.save(orderToUpdate);
									response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, orderToUpdate);
								} else {
									response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_UPDATING_MESSAGE);
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
	public ResponseObject deleteOredr(long orderId, HttpServletRequest request) {
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
							if((Type.ADMINISTRATOR == user.getRole())||(Type.ADMIN == user.getRole())||(Type.USER == user.getRole())){
								Orders order = orderDao.findByOrderId(orderId);
								if(order != null && order.getStatus() == 1){
									order.setStatus(3);
									order.setStatusName(3);
									order.setDeletedAt(new Date());
									orderDao.save(order);
									response = new ResponseObjectCrud(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_DELETTING_MESSAGE, orderId);
								} else {
									response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_DELETTING_MESSAGE);
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