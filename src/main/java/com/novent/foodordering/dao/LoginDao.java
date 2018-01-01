package com.novent.foodordering.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.novent.foodordering.entity.Logins;

@Repository
public interface LoginDao extends CrudRepository<Logins,Long> {

	public List<Logins> findAll();
	
	public Logins findByUserId(long userId);
	
	public Logins findByToken(String token);
	
}
