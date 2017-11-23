package com.novent.foodordering.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.novent.foodordering.constatnt.ResponseCode;
import com.novent.foodordering.constatnt.ResponseMessage;
import com.novent.foodordering.constatnt.ResponseStatus;
import com.novent.foodordering.dao.BranchDao;
import com.novent.foodordering.dao.CartDao;
import com.novent.foodordering.dao.ItemDao;
import com.novent.foodordering.dao.OrderDao;
import com.novent.foodordering.dao.OrderItemDao;
import com.novent.foodordering.dao.UserDao;
import com.novent.foodordering.entity.Administrator;
import com.novent.foodordering.entity.Branch;
import com.novent.foodordering.entity.Cart;
import com.novent.foodordering.entity.Item;
import com.novent.foodordering.entity.OrderItem;
import com.novent.foodordering.entity.Orders;
import com.novent.foodordering.entity.Users;
import com.novent.foodordering.service.OrderService;
import com.novent.foodordering.util.Administrators;
import com.novent.foodordering.util.JsonOrder;
import com.novent.foodordering.util.JsonUser;
import com.novent.foodordering.util.Order;
import com.novent.foodordering.util.ResponseObject;
import com.novent.foodordering.util.ResponseObjectAll;
import com.novent.foodordering.util.ResponseObjectCrud;
import com.novent.foodordering.util.ResponseObjectData;

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
	

	@Override
	public ResponseObject getAllOrders() {
		ResponseObject response = null;
		List<Orders> allOrders = orderDao.findAll();
		if(!allOrders.isEmpty()){
			List<JsonOrder> jsonOrder = new ArrayList<JsonOrder>(); 
			for (Iterator<Orders> iterator = allOrders.iterator(); iterator.hasNext();){
				Orders order = iterator.next();
				Users user = userDao.findByUserId(order.getUserId());
				JsonUser jsonUser = new JsonUser(user.getUserId(), user.getPhoneNumber(), user.getPhoneNumber());
				jsonOrder.add(new JsonOrder(order.getOrderId(), order.getTakeAway(), order.getNumberOfChair(), order.getTotalamount(), order.getAmount(),
                        order.getTax(), order.getBranchId(), order.getCreatedAt(), order.getUpdatedAt(), order.getDeletedAt(), order.getStatus(),
                        order.getStatusName(), order.getCart(), jsonUser));
			}
			response = new ResponseObjectAll<>(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, jsonOrder);
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_GETTING_MESSAGE);
		}
		return response;
	}

	@Override
	public ResponseObject getOrderById(long orderId) {
		ResponseObject response = null;
		Orders order = orderDao.findByOrderId(orderId);
		Users user = userDao.findByUserId(order.getUserId());
		
		if (order != null && user != null){
			JsonUser jsonUser = new JsonUser(user.getUserId(), user.getPhoneNumber(), user.getPhoneNumber());
			JsonOrder jsonOder = new JsonOrder(order.getOrderId(), order.getTakeAway(), order.getNumberOfChair(), order.getTotalamount(), order.getAmount(),
					                           order.getTax(), order.getBranchId(), order.getCreatedAt(), order.getUpdatedAt(), order.getDeletedAt(), order.getStatus(),
					                           order.getStatusName(), order.getCart(), jsonUser);
			response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, jsonOder );
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_GETTING_MESSAGE);
		}
		return response;
	}

	@Override
	public ResponseObject createOrder(Order order) {
		ResponseObject response = null;
		long id = 0;
		boolean isItem = true;
		double totalPrice = 0;
		
		Users user = userDao.findByUserId(order.getUserId());
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
			newOrder.setUserId(userId);
			orderDao.save(newOrder);
			id = newOrder.getOrderId();
			response = new ResponseObjectCrud(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_CREATE_CODE,	ResponseMessage.SUCCESS_CREATING_MESSAGE, id);
		} 
		return response;
	}

	@Override
	public ResponseObject updateOrder(long orderId, Order order) {
		ResponseObject response = null;
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
		return response;
	}

	@Override
	public ResponseObject deleteOredr(long orderId) {
		ResponseObject response = null;
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
		return response;
	}
}