package com.novent.foodordering.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.novent.foodordering.entity.Area;

@Repository
public interface AreaDao extends CrudRepository<Area, Long>{

	public List<Area> findAll();
	
	public Area findByAreaId(long areaId);
	
	public Area findByAreaName(String areaName);
	
}
