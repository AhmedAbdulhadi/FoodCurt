package com.novent.foodordering.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.novent.foodordering.entity.Cart;

@Repository
public interface CartDao extends CrudRepository<Cart, Long> {
	
	public List<Cart> findAllByOrderByCartIdAsc();
	
	public List<Cart> findAllByOrderByCartIdDesc();
	 
	 public Cart findByCartId(long cartId);

}
