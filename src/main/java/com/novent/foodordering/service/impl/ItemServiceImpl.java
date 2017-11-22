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
import com.novent.foodordering.dao.ItemDao;
import com.novent.foodordering.dao.RestaurantDao;
import com.novent.foodordering.entity.Item;
import com.novent.foodordering.entity.Restaurant;
import com.novent.foodordering.service.ItemService;
import com.novent.foodordering.util.Items;
import com.novent.foodordering.util.ResponseObject;
import com.novent.foodordering.util.ResponseObjectAll;
import com.novent.foodordering.util.ResponseObjectCrud;
import com.novent.foodordering.util.ResponseObjectData;

@Service
@Component
public class ItemServiceImpl  implements ItemService{
	
	@Autowired
	private ItemDao itemDao;
	@Autowired
	private RestaurantDao restaurantDao;

	
	@Override
	public ResponseObject getItemByStatus(boolean status) {
		ResponseObject response = null;
		List<Item> allItems = itemDao.findByStatus(status);
		if(!allItems.isEmpty()){
			response = new ResponseObjectAll<Item>(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, allItems);
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_GETTING_MESSAGE);
		}
		return response;
	}

	@Override
	public ResponseObject getItemById(long itemId) {
		ResponseObject response = null;
		Item item = itemDao.findByItemId(itemId);
		if (item != null){
			response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, item);
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_GETTING_MESSAGE);
		}
		return response;
	}

	@Override
	public ResponseObject createItems(Items items) {
		ResponseObject response = null;
		
		Restaurant restaurant = restaurantDao.findByRestaurantId(items.getRestaurantId());
		
 		boolean valid = (restaurant != null && restaurant.isStatus())  && !items.getItems().isEmpty();
 		boolean validName = true;
 		boolean validPrice = true;
		List<Item> item = items.getItems();
		if (valid){
			for (Iterator<Item> iterator = item.iterator(); iterator.hasNext();){
				Item value = iterator.next();
				if(value.getItemName() == null || value.getItemName().equals("")){
					valid = false;
					validName = false;
				} else if (value.getPrice() == 0){
					valid = false;
					validPrice = false;
				} else {
					itemDao.save(value);
				}
			}
		}
		if(items.getRestaurantId() == 0){
			valid = false;
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_RESTAURANTID_REQUIRED_ERROR);
		} else if(restaurant == null ){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_NO_RESTAURANT_ERROR);
		} else if (!restaurant.isStatus()){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_UPDATE_RESTAURANT_ERROR);
		} else if (!validName){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_ITEMNAME_REQUIRED_ERROR);
		} else if (!validPrice){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PRICE_REQUIRED_ERROR);
		} else if(valid){
			restaurant.setItems(item);
			restaurantDao.save(restaurant);
			response = new ResponseObject(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_CREATE_CODE, ResponseMessage.SUCCESS_CREATING_MESSAGE);
		} else{
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_CREATING_MESSAGE);
		} 
		return response;
	}

	@Override
	public ResponseObject updateItem(long itemId, Item item) {
		ResponseObject response = null;
		
		Item itemToUpdate = itemDao.findByItemId(itemId);
		String itemName = item.getItemName();
		double Price = item.getPrice();
		String description = item.getDescription();
		
		boolean valid = ((itemToUpdate != null && itemToUpdate.isStatus()) && item != null);
		
		if(itemToUpdate == null){
			valid = false ;
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_NO_ITEM_ERROR);
		} else if (!itemToUpdate.isStatus()){
			valid = false ;
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_UPDATE_ITEM_ERROR);
		}
		
		if (itemName != null && !itemName.equals("") && valid){
			itemToUpdate.setItemName(itemName);
			itemToUpdate.setUpdatedAt(new Date());
			itemDao.save(itemToUpdate);
			response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_CREATE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, itemToUpdate);
			}
		
		if(Price != 0.0 && valid){
			itemToUpdate.setPrice(item.getPrice());
			itemToUpdate.setUpdatedAt(new Date());
			itemDao.save(itemToUpdate);
			response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_CREATE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, itemToUpdate);
			}
		
		
		if (description != null && !description.equals("") && valid){
			itemToUpdate.setDescription(item.getDescription());
			itemToUpdate.setUpdatedAt(new Date());
			itemDao.save(itemToUpdate);
			response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_CREATE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, itemToUpdate);
			}
		
		return response;
	}

	@Override
	public ResponseObject deleteItem(long itemId) {
		ResponseObject response = null;
		Item item = itemDao.findByItemId(itemId);
		if(item != null && item.isStatus()){
			item.setStatus(false);
			item.setDeletedAt(new Date());
			itemDao.save(item);
			response = new ResponseObjectCrud(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_DELETTING_MESSAGE, itemId);
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_DELETTING_MESSAGE);
		}
		return response;
	}

}
