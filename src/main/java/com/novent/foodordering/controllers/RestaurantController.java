package com.novent.foodordering.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.novent.foodordering.entity.Restaurant;
import com.novent.foodordering.service.RestaurantService;
import com.novent.foodordering.util.ResponseObject;

@RestController
@RequestMapping("api/v1/myrestaurant/restaurant")
@CrossOrigin(origins = "*")
public class RestaurantController {

	@Autowired
	private RestaurantService restaurantService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseObject getAllRestaurant() {
		return restaurantService.getAllRestaurant();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{restaurantId}")
	public ResponseObject getRestaurantById(@PathVariable long restaurantId) {
		return restaurantService.getRestaurantById(restaurantId);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseObject createRestaurant(@RequestBody Restaurant restaurant) {
		return restaurantService.createRestaurant(restaurant);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{restaurantId}")
	public ResponseObject updateRestaurant(@RequestBody Restaurant restaurant, @PathVariable long restaurantId) {
		return restaurantService.updateRestaurant(restaurantId, restaurant);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{restaurantId}")
	public ResponseObject deleteRestaurant(@PathVariable long restaurantId) {
		return restaurantService.deleteRestaurant(restaurantId);
	}

}
