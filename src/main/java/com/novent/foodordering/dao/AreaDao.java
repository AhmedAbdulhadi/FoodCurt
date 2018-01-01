package com.novent.foodordering.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.novent.foodordering.entity.Area;

@Repository
//public interface AreaDao extends CrudRepository<Area, Long>{
public interface AreaDao extends PagingAndSortingRepository<Area, Long>{

	public List<Area> findAllByOrderByAreaIdAsc();
	
	public List<Area> findAllByOrderByAreaIdDesc();
	
	public Page<Area> findByStatusOrderByAreaIdAsc(boolean status, Pageable pageable);

	public Page<Area> findByStatusOrderByAreaIdDesc(boolean status, Pageable pageable);

	
	public Area findByAreaId(long areaId);
	
	public Area findByAreaName(String areaName);
	
	public Area findByAreaNameAR(String areaNameAR);
	
}
