package com.novent.foodordering.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.novent.foodordering.service.CartService;
import com.novent.foodordering.util.CustomCarts;
import com.novent.foodordering.util.ResponseObject;

@RestController
@RequestMapping("api/v1/cart")
@CrossOrigin(origins = "*")
public class CartController {

	@Autowired
	private CartService cartService;

	@RequestMapping(method = RequestMethod.GET, value = "/{cartId}")
	public ResponseObject getCartById(@PathVariable long cartId, HttpServletRequest request) {
		return cartService.getCartById(cartId, request);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{cartId}")
	public ResponseObject updateCart(@RequestBody CustomCarts carts, @PathVariable long cartId, HttpServletRequest request) {
		return cartService.updateCart(cartId, carts, request);
	}
}
