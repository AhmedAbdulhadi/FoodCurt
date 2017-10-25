package com.novent.foodordering.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.novent.foodordering.entity.Item;

@Repository
public interface ItemDao extends CrudRepository<Item, Long>{

	public List<Item> findAll();
	
	public Item findByItemId(long itemId);
}
