package com.novent.foodordering.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.novent.foodordering.entity.Orders;


@Repository
public interface OrderDao extends CrudRepository<Orders, Long>{
	
	 public List<Orders> findAll();
		
	 public Orders findByOrderId(long orderId);
		
   	 public List<Orders> findByStatusName (String statusName);

}
