package com.novent.foodordering.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.novent.foodordering.entity.Branch;

@Repository
public interface BranchDao extends CrudRepository<Branch, Long>{
	
	public List<Branch> findAll();
	
	public Branch findByBranchId(long branchId);
	
	public Branch findByAreaId(long areaId);
	
	public Branch findByPhoneNumber(String phoneNumber);

}
