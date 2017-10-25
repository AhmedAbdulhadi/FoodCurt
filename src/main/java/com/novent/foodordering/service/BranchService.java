package com.novent.foodordering.service;

import org.springframework.stereotype.Service;

import com.novent.foodordering.entity.Branch;
import com.novent.foodordering.util.ResponseObject;

@Service
public interface BranchService {
	
    public ResponseObject getAllBranch();
	
	public ResponseObject getBranchById(long branchId);
	
	public ResponseObject createBranch(Branch branch);
	
	public ResponseObject updateBranch(long branchId, Branch branch);
	
	public ResponseObject deleteBranch(long branchId);

}
