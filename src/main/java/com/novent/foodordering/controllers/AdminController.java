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

import com.novent.foodordering.entity.Admin;
import com.novent.foodordering.service.AdminService;
import com.novent.foodordering.util.ResponseObject;

@RestController
@RequestMapping("api/v1/admin")
@CrossOrigin(origins = "*")
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
	private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseObject getAdminByStatus(@RequestParam(value = "status", required=false, defaultValue="true") Boolean status, @RequestParam(value = "sort", required=false, defaultValue="Desc") String sort, HttpServletRequest request,
                                           @PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) Pageable pageable) {
		return adminService.getAdminByStatus(status, request, sort, pageable);
	}
	
	@RequestMapping(method = RequestMethod.GET, params = "all")
	public ResponseObject getAllAdmins(@RequestParam(value = "all", defaultValue ="true") Boolean all, @RequestParam(value = "sort", required=false, defaultValue ="Desc") String sort, HttpServletRequest request) {
		return adminService.getAllAdmins(request, sort);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{adminId}")
	public ResponseObject getAdminById(@PathVariable long adminId, HttpServletRequest request) {
		return adminService.getAdminById(adminId, request);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseObject createAdmin(@RequestBody Admin admin, HttpServletRequest request) {
		return adminService.createAdmin(admin, request);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{adminId}")
	public ResponseObject updateAdmin(@PathVariable long adminId, @RequestBody Admin admin, HttpServletRequest request) {
	    	return adminService.updateAdmin(adminId, admin, request);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{adminId}")
	public ResponseObject deleteAdmin(@PathVariable long adminId, HttpServletRequest request) {
	    	return adminService.deleteAdmin(adminId, request);
	}
}
