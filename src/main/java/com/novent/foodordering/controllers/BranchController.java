package com.novent.foodordering.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.novent.foodordering.entity.Branch;
import com.novent.foodordering.service.BranchService;
import com.novent.foodordering.util.ResponseObject;

@RestController
@RequestMapping("api/v1/myrestaurant/branch")
@CrossOrigin(origins = "*")
public class BranchController {
	
	@Autowired
	private BranchService branchService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseObject getAllBranch() {
		return branchService.getAllBranch();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{branchId}")
	public ResponseObject getBranchById(@PathVariable long branchId) {
		return branchService.getBranchById(branchId);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseObject createBranch(@RequestBody Branch branch) {
		return branchService.createBranch(branch);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{branchId}")
	public ResponseObject updateBranch(@RequestBody Branch branch, @PathVariable long branchId) {
		return branchService.updateBranch(branchId, branch);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{branchId}")
	public ResponseObject deleteBranch(@PathVariable long branchId) {
		return branchService.deleteBranch(branchId);
	}

}
