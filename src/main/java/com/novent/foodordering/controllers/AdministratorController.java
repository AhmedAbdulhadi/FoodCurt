package com.novent.foodordering.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.novent.foodordering.entity.Administrator;
import com.novent.foodordering.service.AdministratorService;
import com.novent.foodordering.util.ResponseObject;

@RestController
@RequestMapping("api/v1/myrestaurant/administrator")
@CrossOrigin(origins = "*")
public class AdministratorController {
	
	@Autowired
	private AdministratorService administratorService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseObject getAllAdministrators() {
		return administratorService.getAllAdministrators();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{AdministratorId}")
	public ResponseObject getAdministratorById(@PathVariable long AdministratorId) {
		return administratorService.getAdministratorById(AdministratorId);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseObject createAdministrator(@RequestBody Administrator administrator) {
		return administratorService.createAdministrator(administrator);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{administratorId}")
	public ResponseObject updateAdministrator(@RequestBody Administrator administrator, @PathVariable long administratorId) {
		return administratorService.updateAdministrator(administratorId, administrator);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{administratorId}")
	public ResponseObject deleteAdministrator(@PathVariable long administratorId) {
		return administratorService.deleteAdministrator(administratorId);
	}

}
