package com.novent.foodordering.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.novent.foodordering.entity.Admin;

@Repository
//public interface AdminDao extends CrudRepository<Admin, Long>{
public interface AdminDao extends PagingAndSortingRepository<Admin, Long>{
 
	public List<Admin> findAllByOrderByIdAsc();
	
	public List<Admin> findAllByOrderByIdDesc();
	
	public Page<Admin> findByStatusOrderByIdAsc(boolean status, Pageable pageable);

	public Page<Admin> findByStatusOrderByIdDesc(boolean status, Pageable pageable);
	
	public Admin findById(long Id);
	
	public Admin findByUserName(String userName);
	
	public Admin findByPhoneNumber(String phoneNumber);
	
	public Admin findByEmail(String email);
	
}
