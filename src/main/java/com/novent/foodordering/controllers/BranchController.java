package com.novent.foodordering.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.novent.foodordering.entity.Branch;
import com.novent.foodordering.service.BranchService;
import com.novent.foodordering.util.ResponseObject;

@RestController
@RequestMapping("api/v1/branch")
@CrossOrigin(origins = "*")
public class BranchController {
	
	@Autowired
	private BranchService branchService;
	
	private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;
    
	@RequestMapping(method = RequestMethod.GET)
	public ResponseObject getBranchByStatus(@RequestParam(value = "status", required=false, defaultValue="true") Boolean status, @RequestParam(value = "sort", required=false, defaultValue="Desc") String sort, HttpServletRequest request,
											@PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) Pageable pageable) {
		return branchService.getBranchByStatus(status, request, sort, pageable);
	}

	@RequestMapping(method = RequestMethod.GET, params = "all")
	public ResponseObject getAllBranchs(@RequestParam(value = "all", defaultValue ="true") Boolean all, @RequestParam(value = "sort", required=false, defaultValue ="Desc") String sort, HttpServletRequest request) {
		return branchService.getAllBranchs(request, sort);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{branchId}")
	public ResponseObject getBranchById(@PathVariable long branchId, HttpServletRequest request) {
		return branchService.getBranchById(branchId, request);
	}
	
	@RequestMapping(method = RequestMethod.GET, params = "resturant_id")
	public ResponseObject getBranchByRestaurantId(@RequestParam(value = "resturant_id") long restaurantId, HttpServletRequest request) {
		return branchService.getBranchByRestaurantId(restaurantId, request);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseObject createBranch(@RequestBody Branch branch, HttpServletRequest request) {
		return branchService.createBranch(branch, request);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{branchId}")
	public ResponseObject updateBranch(@RequestBody Branch branch, @PathVariable long branchId, HttpServletRequest request) {
		return branchService.updateBranch(branchId, branch, request);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{branchId}")
	public ResponseObject deleteBranch(@PathVariable long branchId, HttpServletRequest request) {
		return branchService.deleteBranch(branchId, request);
	}

}
