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

import com.novent.foodordering.entity.Restaurant;
import com.novent.foodordering.service.RestaurantService;
import com.novent.foodordering.util.ResponseObject;

@RestController
@RequestMapping("api/v1/restaurant")
@CrossOrigin(origins = "*")
public class RestaurantController {

	@Autowired
	private RestaurantService restaurantService;
	
	private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseObject getRestaurantByStatus(@RequestParam(value = "status", required=false, defaultValue ="true") Boolean status, @RequestParam(value = "sort", required=false, defaultValue ="Desc") String sort,
			                                    @PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) Pageable pageable) {
		return restaurantService.getRestaurantByStatus(status, sort, pageable);
	}
	
	@RequestMapping(method = RequestMethod.GET, params = "all")
	public ResponseObject getAllRestaurants(@RequestParam(value = "all", defaultValue ="true") Boolean all, @RequestParam(value = "sort", required=false, defaultValue ="Desc") String sort) {
		return restaurantService.getAllRestaurants(sort);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{restaurantId}")
	public ResponseObject getRestaurantById(@PathVariable long restaurantId, HttpServletRequest request) {
		return restaurantService.getRestaurantById(restaurantId, request);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseObject createRestaurant(@RequestBody Restaurant restaurant, HttpServletRequest request) {
		return restaurantService.createRestaurant(restaurant, request);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{restaurantId}")
	public ResponseObject updateRestaurant(@PathVariable long restaurantId, @RequestBody Restaurant restaurant, HttpServletRequest request) {
		return restaurantService.updateRestaurant(restaurantId, restaurant, request);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{restaurantId}")
	public ResponseObject deleteRestaurant(@PathVariable long restaurantId, HttpServletRequest request) {
		return restaurantService.deleteRestaurant(restaurantId, request);
	}

}