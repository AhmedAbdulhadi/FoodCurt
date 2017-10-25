package com.novent.foodordering.service;

import org.springframework.stereotype.Service;

import com.novent.foodordering.entity.Orders;
import com.novent.foodordering.util.Order;
import com.novent.foodordering.util.ResponseObject;

@Service
public interface OrderService {
	
    public ResponseObject getAllOrders();
	
	public ResponseObject getOrderById(long orderId);
	
	public ResponseObject createOrder(Order order);
	
	public ResponseObject updateOrder(long orderId, Order order);
	
	public ResponseObject deleteOredr(long orderId);

}
