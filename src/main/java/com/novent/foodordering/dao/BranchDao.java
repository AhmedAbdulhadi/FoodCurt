package com.novent.foodordering.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.novent.foodordering.entity.Branch;

@Repository
//public interface BranchDao extends CrudRepository<Branch, Long>{
public interface BranchDao extends PagingAndSortingRepository<Branch, Long>{
	
	public List<Branch> findAllByOrderByBranchIdAsc();
	
	public List<Branch> findAllByOrderByBranchIdDesc();
	
	public Page<Branch> findByStatusOrderByBranchIdAsc(boolean status, Pageable pageable);

	public Page<Branch> findByStatusOrderByBranchIdDesc(boolean status, Pageable pageable);
	
	public List<Branch> findByRestaurantId(long restaurantId);
	
	public Branch findByBranchId(long branchId);
	
	public Branch findByAreaId(long areaId);
	
	public Branch findByPhoneNumber(String phoneNumber);
	

}
