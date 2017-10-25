package com.novent.foodordering.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.novent.foodordering.entity.Admin;

@Repository
public interface AdminDao extends CrudRepository<Admin, Long>{
 
	public List<Admin> findAll();
	
	public Admin findByAdminId(long adminId);
	
	public Admin findByUserName(String userName);
	
	public Admin findByPhoneNumber(String phoneNumber);
	
	public Admin findByEmail(String email);
	
}
