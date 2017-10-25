package com.novent.foodordering.service;

import org.springframework.stereotype.Service;

import com.novent.foodordering.entity.Restaurant;
import com.novent.foodordering.util.ResponseObject;

@Service
public interface RestaurantService {

public ResponseObject getAllRestaurant();
	
	public ResponseObject getRestaurantById(long restaurantId);
	
	public ResponseObject createRestaurant(Restaurant restaurant);
	
	public ResponseObject updateRestaurant(long restaurantId,Restaurant restaurant);
	
	public ResponseObject deleteRestaurant(long restaurantId);
}
