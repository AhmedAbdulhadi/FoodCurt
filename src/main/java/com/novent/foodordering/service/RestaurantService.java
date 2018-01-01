package com.novent.foodordering.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.novent.foodordering.entity.Restaurant;
import com.novent.foodordering.util.ResponseObject;

@Service
public interface RestaurantService {
	
	public ResponseObject getRestaurantByStatus(boolean status, String sort, Pageable pageable);
	
	public ResponseObject getAllRestaurants(String sort);

	public ResponseObject getRestaurantById(long restaurantId, HttpServletRequest request);
	
	public ResponseObject createRestaurant(Restaurant restaurant, HttpServletRequest request);
	
	public ResponseObject updateRestaurant(long restaurantId, Restaurant restaurant, HttpServletRequest request);
	
	public ResponseObject deleteRestaurant(long restaurantId, HttpServletRequest request);
}
