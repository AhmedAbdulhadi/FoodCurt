package com.novent.foodordering.dao;

import java.util.List;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.novent.foodordering.entity.ModelEntity;



@Repository
public interface ModelEntityDao extends CrudRepository<ModelEntity, Long> {
	
	
	public List<ModelEntity> findAll();
	
	public ModelEntity findByModelEntityId(long id);
	
	public ModelEntity findByPhoneNumber(String phoneNumber);
	
	public ModelEntity findByEmail(String email);
	
	public ModelEntity findByUserName (String userName);



}
