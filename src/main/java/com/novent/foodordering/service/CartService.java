package com.novent.foodordering.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.novent.foodordering.util.CustomCarts;
import com.novent.foodordering.util.ResponseObject;

@Service
public interface CartService {
	
	 public ResponseObject getCartById(long cartId, HttpServletRequest request);
	 
	 public ResponseObject updateCart(long cartId, CustomCarts carts, HttpServletRequest request);

}
