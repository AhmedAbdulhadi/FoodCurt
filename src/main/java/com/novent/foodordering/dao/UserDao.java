package com.novent.foodordering.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.novent.foodordering.entity.Users;


@Repository
public interface UserDao extends CrudRepository<Users, Long>{
	
	public List<Users> findAll();
	
	public Users findByUserId(long userId);
	
	public Users findByUserName(String userName);
	
	public Users findByPhoneNumber(String phoneNumber);
	
	public Users findByEmail(String email);
}
