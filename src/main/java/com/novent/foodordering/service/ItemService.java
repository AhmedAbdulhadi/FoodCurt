package com.novent.foodordering.service;

import org.springframework.stereotype.Service;

import com.novent.foodordering.entity.Item;
import com.novent.foodordering.util.Items;
import com.novent.foodordering.util.ResponseObject;

@Service
public interface ItemService {
	
	public ResponseObject getAllItem();
	
	public ResponseObject getItemById(long itemId);
	
	public ResponseObject createItems(Items items);
	
	public ResponseObject updateItem(long itemId, Item item);
	
	public ResponseObject deleteItem(long itemId);
	

	
}
