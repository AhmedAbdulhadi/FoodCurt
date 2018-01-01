package com.novent.foodordering.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.novent.foodordering.entity.Item;
import com.novent.foodordering.util.CustomItems;
import com.novent.foodordering.util.ResponseObject;

@Service
public interface ItemService {
	
	public ResponseObject getAllItems(HttpServletRequest request, String sort);
	
	public ResponseObject getItemByStatus(boolean status, HttpServletRequest request, String sort, Pageable pageable);
	
	public ResponseObject getItemById(long itemId, HttpServletRequest request);
	
	public ResponseObject createItems(CustomItems items, HttpServletRequest request);
	
	public ResponseObject updateItem(long itemId, Item item, HttpServletRequest request);
	
	public ResponseObject deleteItem(long itemId, HttpServletRequest request);
	

	
}
