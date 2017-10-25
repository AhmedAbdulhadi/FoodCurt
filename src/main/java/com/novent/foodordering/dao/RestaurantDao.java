package com.novent.foodordering.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.novent.foodordering.entity.Restaurant;

@Repository
public interface RestaurantDao extends CrudRepository<Restaurant, Long>{
	
	public List<Restaurant> findAll();
	
	public Restaurant findByRestaurantId(long restaurantId);
	
	public Restaurant findByRestaurantName(String restaurantName);
	
	public Restaurant findByUserName(String userName);
	
	public Restaurant findByPhoneNumber (String phoneNumber);
	
	public Restaurant findByEmail(String email);

}
