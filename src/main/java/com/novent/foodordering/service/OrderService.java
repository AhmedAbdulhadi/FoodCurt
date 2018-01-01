package com.novent.foodordering.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.novent.foodordering.util.CustomOrder;
import com.novent.foodordering.util.ResponseObject;

@Service
public interface OrderService {
	
    public ResponseObject getAllOrders(HttpServletRequest request, String sort, Pageable pageable);
	
	public ResponseObject getOrderById(long orderId, HttpServletRequest request);
	
	public ResponseObject createOrder(CustomOrder order, HttpServletRequest request);
	
	public ResponseObject updateOrder(long orderId, CustomOrder order, HttpServletRequest request);
	
	public ResponseObject deleteOredr(long orderId, HttpServletRequest request);

}
