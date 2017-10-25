package com.novent.foodordering.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.novent.foodordering.entity.Item;
import com.novent.foodordering.service.ItemService;
import com.novent.foodordering.util.Items;
import com.novent.foodordering.util.ResponseObject;

@RestController
@RequestMapping("api/v1/myrestaurant/item")
@CrossOrigin(origins = "*")
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseObject getAllItem() {
		return itemService.getAllItem();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{itemId}")
	public ResponseObject getItemById(@PathVariable long itemId) {
		return itemService.getItemById(itemId);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseObject createItems(@RequestBody Items items) {
		return itemService.createItems(items);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{itemId}")
	public ResponseObject updateItem(@RequestBody Item item, @PathVariable long itemId) {
		return itemService.updateItem(itemId, item);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{itemId}")
	public ResponseObject deleteItem(@PathVariable long itemId) {
		return itemService.deleteItem(itemId);
	}


}
