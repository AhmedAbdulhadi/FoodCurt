package com.novent.foodordering.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.novent.foodordering.entity.Branch;
import com.novent.foodordering.util.ResponseObject;

@Service
public interface BranchService {
	    
	public ResponseObject getAllBranchs(HttpServletRequest request, String sort);

	public ResponseObject getBranchByStatus(boolean status, HttpServletRequest request, String sort, Pageable pageable);
	
	public ResponseObject getBranchById(long branchId, HttpServletRequest request);
	
	public ResponseObject getBranchByRestaurantId(long restaurantId, HttpServletRequest request);
	
	public ResponseObject createBranch(Branch branch, HttpServletRequest request);
	
	public ResponseObject updateBranch(long branchId, Branch branch, HttpServletRequest request);
	
	public ResponseObject deleteBranch(long branchId, HttpServletRequest request);

}
