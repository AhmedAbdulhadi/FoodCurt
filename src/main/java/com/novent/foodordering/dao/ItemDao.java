package com.novent.foodordering.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.novent.foodordering.entity.Item;

@Repository
//public interface ItemDao extends CrudRepository<Item, Long>{
public interface ItemDao extends PagingAndSortingRepository<Item, Long>{

	public List<Item> findAllByOrderByItemIdAsc();
	
	public List<Item> findAllByOrderByItemIdDesc();
	
	public Page<Item> findByStatusOrderByItemIdAsc(boolean status, Pageable pageable);

	public Page<Item> findByStatusOrderByItemIdDesc(boolean status, Pageable pageable);
	
	public Item findByItemId(long itemId);
}
