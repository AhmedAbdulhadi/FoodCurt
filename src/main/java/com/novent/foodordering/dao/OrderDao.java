package com.novent.foodordering.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.novent.foodordering.entity.Cart;
import com.novent.foodordering.entity.Orders;


@Repository
//public interface OrderDao extends CrudRepository<Orders, Long>{
public interface OrderDao extends PagingAndSortingRepository<Orders, Long>{
	
	public List<Orders> findAllByOrderByOrderIdAsc();
	
	public List<Orders> findAllByOrderByOrderIdDesc();
	 
	 public Page<Orders> findByStatusOrderByOrderIdAsc(Pageable pageable, int status);

	 public Page<Orders> findByStatusOrderByOrderIdDesc(Pageable pageable, int status);
	 
	 public Orders findByOrderId(long orderId);

   	 public List<Orders> findByStatusName (String statusName);
   	 
   	 public Orders findByCart(Cart cart);
   	 
   	 
}
