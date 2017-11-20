package com.novent.foodordering.service.impl;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.novent.foodordering.constatnt.ResponseCode;
import com.novent.foodordering.constatnt.ResponseMessage;
import com.novent.foodordering.constatnt.ResponseStatus;
import com.novent.foodordering.dao.CartDao;
import com.novent.foodordering.dao.ItemDao;
import com.novent.foodordering.dao.OrderItemDao;
import com.novent.foodordering.entity.Cart;
import com.novent.foodordering.entity.Item;
import com.novent.foodordering.entity.OrderItem;
import com.novent.foodordering.service.CartService;
import com.novent.foodordering.util.Carts;
import com.novent.foodordering.util.ResponseObject;
import com.novent.foodordering.util.ResponseObjectData;

@Service
@Component
public class CartServiceImpl implements CartService {
	
	@Autowired
	public CartDao cartDao;
	@Autowired
	public OrderItemDao orderItemDao;
	@Autowired
	public ItemDao itemDao;


	@Override
	public ResponseObject getCartById(long cartId) {
		ResponseObject response = null;
		Cart cart = cartDao.findByCartId(cartId);
		if(cart == null ){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_GETTING_MESSAGE);
		} else {
			response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, cart );
		}
		return response;
	}

	@Override
	public ResponseObject updateCart(long cartId, Carts carts) {
		ResponseObject response = null;
		System.out.println(carts);
		Cart cartToUpdate = cartDao.findByCartId(cartId);
		System.out.println(cartToUpdate);
		List<OrderItem> items = carts.getItems();
        System.out.println(items);
        
        boolean valid = true;
        boolean isItem = true;
        double totalPrice = 0;
        
		if(cartToUpdate != null && !items.isEmpty()){
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
		if (!isItem) {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_NO_ITEM_ERROR);
		} else if (valid) {
			cartToUpdate.setOrderItem(items);
			cartDao.save(cartToUpdate);
			response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, cartToUpdate );
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_GETTING_MESSAGE);
		}
		return response;
	}
}