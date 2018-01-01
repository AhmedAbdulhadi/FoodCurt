package com.novent.foodordering.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.novent.foodordering.entity.Restaurant;

@Repository
public interface RestaurantDao extends CrudRepository<Restaurant, Long>{
	
	public List<Restaurant> findAllByOrderByIdAsc();
	
	public List<Restaurant> findAllByOrderByIdDesc();
	
	public Page<Restaurant> findByStatusOrderByIdAsc(boolean status, Pageable pageable);

	public Page<Restaurant> findByStatusOrderByIdDesc(boolean status, Pageable pageable);
	
	public Restaurant findById(long id);
	
	public Restaurant findByRestaurantName(String restaurantName);
	
	public Restaurant findByRestaurantNameAR(String restaurantNameAR);
	
	public Restaurant findByUserName(String userName);
	
	public Restaurant findByPhoneNumber (String phoneNumber);
	
	public Restaurant findByEmail(String email);

}
