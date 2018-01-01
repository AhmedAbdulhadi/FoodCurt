package com.novent.foodordering.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.novent.foodordering.service.OrderService;
import com.novent.foodordering.util.CustomOrder;
import com.novent.foodordering.util.ResponseObject;

@RestController
@RequestMapping("api/v1/order")
@CrossOrigin(origins = "*")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseObject getAllOrders(@RequestParam(value = "sort", required=false) String sort, HttpServletRequest request, Pageable pageable) {
		if(sort == null){
	    	sort = "Desc";
	    }
		return orderService.getAllOrders(request, sort, pageable);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{orderId}")
	public ResponseObject getOrderById(@PathVariable long orderId, HttpServletRequest request) {
		return orderService.getOrderById(orderId, request);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseObject createOrder(@RequestBody CustomOrder order, HttpServletRequest request) {
		return orderService.createOrder(order, request);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{orderId}")
	public ResponseObject updateOrder(@RequestBody CustomOrder order, @PathVariable long orderId, HttpServletRequest request) {
		return orderService.updateOrder(orderId, order, request);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{orderId}")
	public ResponseObject deleteOredr(@PathVariable long orderId, HttpServletRequest request) {
		return orderService.deleteOredr(orderId, request);
	}

}
