package com.novent.foodordering.service;

import org.springframework.stereotype.Service;

import com.novent.foodordering.util.Carts;
import com.novent.foodordering.util.ResponseObject;

@Service
public interface CartService {
	
	 public ResponseObject getCartById(long cartId);
	 
	 public ResponseObject updateCart(long cartId, Carts carts);

}
