package com.novent.foodordering.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.novent.foodordering.entity.Admin;
import com.novent.foodordering.service.AdminService;
import com.novent.foodordering.util.ResponseObject;

@RestController
@RequestMapping("api/v1/myrestaurant/admin")
@CrossOrigin(origins = "*")
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseObject getAllAdmins() {
		return adminService.getAllAdmins();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{adminId}")
	public ResponseObject getAdminById(@PathVariable long adminId) {
		return adminService.getAdminById(adminId);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseObject createAdmin(@RequestBody Admin admin) {
		return adminService.createAdmin(admin);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{adminId}")
	public ResponseObject updateAdmin(@RequestBody Admin admin, @PathVariable long adminId) {
		return adminService.updateAdmin(adminId, admin);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{adminId}")
	public ResponseObject deleteAdmin(@PathVariable long adminId) {
		return adminService.deleteAdmin(adminId);
	}

}
