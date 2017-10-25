package com.novent.foodordering.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.novent.foodordering.entity.Administrator;

@Repository
public interface AdministratorDao extends CrudRepository<Administrator, Long>{
 
	public List<Administrator> findAll();
	
	public Administrator findByAdministratorId(long adminId);
	
	public Administrator findByUserName(String userName);
	
	public Administrator findByPhoneNumber(String phoneNumber);
	
	public Administrator findByEmail(String email);
	
}
