package com.novent.foodordering.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.novent.foodordering.entity.Item;
import com.novent.foodordering.service.ItemService;
import com.novent.foodordering.util.CustomItems;
import com.novent.foodordering.util.ResponseObject;

@RestController
@RequestMapping("api/v1/item")
@CrossOrigin(origins = "*")
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseObject getItemByStatus(@RequestParam(value = "status", required=false, defaultValue="true") Boolean status, @RequestParam(value = "sort", required=false, defaultValue="Desc") String sort, HttpServletRequest request,
                                          @PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) Pageable pageable) {
		return itemService.getItemByStatus(status, request, sort, pageable);
	}

	@RequestMapping(method = RequestMethod.GET, params = "all")
	public ResponseObject getAllItems(@RequestParam(value = "all", defaultValue ="true") Boolean all, @RequestParam(value = "sort", required=false, defaultValue ="Desc") String sort, HttpServletRequest request) {
		return itemService.getAllItems(request, sort);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{itemId}")
	public ResponseObject getItemById(@PathVariable long itemId, HttpServletRequest request) {
		return itemService.getItemById(itemId, request);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseObject createItems(@RequestBody CustomItems items, HttpServletRequest request) {
		return itemService.createItems(items, request);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{itemId}")
	public ResponseObject updateItem(@RequestBody Item item, @PathVariable long itemId, HttpServletRequest request) {
		return itemService.updateItem(itemId, item, request);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{itemId}")
	public ResponseObject deleteItem(@PathVariable long itemId, HttpServletRequest request) {
		return itemService.deleteItem(itemId, request);
	}


}
