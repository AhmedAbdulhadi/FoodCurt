package com.novent.foodordering.service.impl;

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
import com.novent.foodordering.entity.Branch;
import com.novent.foodordering.entity.Cart;
import com.novent.foodordering.entity.Item;
import com.novent.foodordering.entity.OrderItem;
import com.novent.foodordering.entity.Orders;
import com.novent.foodordering.entity.Users;
import com.novent.foodordering.service.OrderService;
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
			response = new ResponseObjectAll<>(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, allOrders);
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
//		if(order != null){
			response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, order );
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
				
		boolean valid = order != null && user != null && branch != null ;
		
		long userId = order.getUserId();
		long branchId = order.getBranchId();
		int numberOfChair = order.getNumberOfChair();
		boolean takeAway = order.getTakeAway();
		List<OrderItem> items = order.getItems();

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
					totalPrice +=(quantity*price);
					} else {
					valid = false;
					isItem = false;
					}
				}	
		}
		 if(takeAway){
				numberOfChair = 0;
			}
		if (user == null) {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_NO_USER_ERROR);
		} else if (branch == null) {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_NO_BRANCH_ERROR);
		} else if (!isItem) {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_NO_ITEM_ERROR);
		} else if (valid) {
			Cart cart = new Cart();
			Orders newOrder = new Orders();
			cart.setOrderItem(items);
			cartDao.save(cart);
			
			newOrder.setBranchId(branchId);
			newOrder.setCart(cart);
			newOrder.setNumberOfChair(numberOfChair);
			newOrder.setTakeAway(takeAway);
			newOrder.setTotalamount(totalPrice);
			newOrder.setUser(user);
			newOrder.setUserId(userId);
			orderDao.save(newOrder);
			id = newOrder.getOrderId();
			response = new ResponseObjectCrud(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_CREATE_CODE,	ResponseMessage.SUCCESS_CREATING_MESSAGE, id);
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_CREATING_MESSAGE);
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
		
		boolean valid = order != null /*&& branch != null*/ && orderToUpdate != null;

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
			
			orderToUpdate.setTotalamount(totalPrice);
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
		if(order != null && order.getStatus() == 0){
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