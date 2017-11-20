package com.novent.foodordering.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.novent.foodordering.service.CartService;
import com.novent.foodordering.util.Carts;
import com.novent.foodordering.util.ResponseObject;

@RestController
@RequestMapping("api/v1/cart")
@CrossOrigin(origins = "*")
public class CartController {

	@Autowired
	private CartService cartService;

	@RequestMapping(method = RequestMethod.GET, value = "/{cartId}")
	public ResponseObject getCartById(@PathVariable long cartId) {
		return cartService.getCartById(cartId);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{cartId}")
	public ResponseObject updateCart(@RequestBody Carts carts, @PathVariable long cartId) {
		return cartService.updateCart(cartId, carts);
	}
}
