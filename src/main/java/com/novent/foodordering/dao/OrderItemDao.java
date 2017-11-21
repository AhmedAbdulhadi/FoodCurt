package com.novent.foodordering.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.novent.foodordering.entity.OrderItem;

@Repository
public interface OrderItemDao extends CrudRepository<OrderItem, Long> {
	
	 public List<OrderItem> findAll();
	 
	 public OrderItem findByOrdreItemId(long ordreItemId);

}
