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

import com.novent.foodordering.entity.Administrator;
import com.novent.foodordering.service.AdministratorService;
import com.novent.foodordering.util.ResponseObject;

@RestController
@RequestMapping("api/v1/administrator")
@CrossOrigin(origins = "*")
public class AdministratorController {
	
	@Autowired
	private AdministratorService administratorService;
	
	private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseObject getAdministratorsByStatus(@RequestParam(value = "status", required=false, defaultValue="true") Boolean status, @RequestParam(value = "sort", required=false, defaultValue="Desc") String sort, HttpServletRequest request,
                                                    @PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) Pageable pageable) { 
		return administratorService.getAdministratorsByStatus(status, request, sort, pageable);
	}
	
	@RequestMapping(method = RequestMethod.GET, params = "all")
	public ResponseObject getAllAdministrators(@RequestParam(value = "all", defaultValue ="true") Boolean all, @RequestParam(value = "sort", required=false, defaultValue ="Desc") String sort, HttpServletRequest request) {
		return administratorService.getAllAdministrators(request, sort);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{administratorId}")
	public ResponseObject getAdministratorById(@PathVariable long administratorId, HttpServletRequest request) {
		return administratorService.getAdministratorById(administratorId, request);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseObject createAdministrator(@RequestBody Administrator administrator, HttpServletRequest request) {
		return administratorService.createAdministrator(administrator, request);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{administratorId}")
	public ResponseObject updateAdministrator(@PathVariable long administratorId, @RequestBody Administrator administrator, HttpServletRequest request) {
		return administratorService.updateAdministrator(administratorId, administrator, request);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{administratorId}")
	public ResponseObject deleteAdministrator(@PathVariable long administratorId, HttpServletRequest request) {
		return administratorService.deleteAdministrator(administratorId, request);
	}

}
