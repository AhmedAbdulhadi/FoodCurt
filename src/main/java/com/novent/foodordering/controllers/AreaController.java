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

import com.novent.foodordering.entity.Area;
import com.novent.foodordering.service.AreaService;
import com.novent.foodordering.util.ResponseObject;

@RestController
@RequestMapping("api/v1/area")
@CrossOrigin(origins = "*")

public class AreaController {
	
	@Autowired
	private AreaService areaService;
	
	private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseObject getAreaByStatus(@RequestParam(value = "status", required=false, defaultValue="true") Boolean status, @RequestParam(value = "sort", required=false, defaultValue="Desc") String sort, HttpServletRequest request,
                                          @PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) Pageable pageable) {
		return areaService.getAreaByStatus(status, request, sort, pageable);
	}
	
	@RequestMapping(method = RequestMethod.GET, params = "all")
	public ResponseObject getAllAreas(@RequestParam(value = "all", defaultValue ="true") Boolean all, @RequestParam(value = "sort", required=false, defaultValue ="Desc") String sort, HttpServletRequest request) {
		return areaService.getAllAreas(request, sort);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{areaId}")
	public ResponseObject getAreaById(@PathVariable long areaId, HttpServletRequest request) {
		return areaService.getAreaById(areaId, request);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseObject createArea(@RequestBody Area area, HttpServletRequest request) {
		return areaService.createArea(area, request);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{areaId}")
	public ResponseObject updateArea(@PathVariable long areaId, @RequestBody Area area, HttpServletRequest request) {
		return areaService.updateArea(areaId, area, request);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{areaId}")
	public ResponseObject deleteArea(@PathVariable long areaId, HttpServletRequest request) {
		return areaService.deleteArea(areaId, request);
	}


}
