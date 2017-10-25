package com.novent.foodordering.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.novent.foodordering.entity.Area;
import com.novent.foodordering.service.AreaService;
import com.novent.foodordering.util.ResponseObject;

@RestController
@RequestMapping("api/v1/myrestaurant/area")
@CrossOrigin(origins = "*")

public class AreaController {
	
	@Autowired
	private AreaService areaService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseObject getAllUser() {
		return areaService.getAllAreas();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{areaId}")
	public ResponseObject getAreaById(@PathVariable long areaId) {
		return areaService.getAreaById(areaId);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseObject createArea(@RequestBody Area area) {
		return areaService.createArea(area);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{areaId}")
	public ResponseObject updateArea(@RequestBody Area area, @PathVariable long areaId) {
		return areaService.updateArea(areaId, area);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{areaId}")
	public ResponseObject deleteArea(@PathVariable long areaId) {
		return areaService.deleteArea(areaId);
	}


}
