package com.novent.foodordering.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.novent.foodordering.entity.Area;
import com.novent.foodordering.util.ResponseObject;

@Service
public interface AreaService {
		
	public ResponseObject getAllAreas(HttpServletRequest request, String sort);

	public ResponseObject getAreaByStatus(boolean status, HttpServletRequest request, String sort, Pageable pageable);
	
	public ResponseObject getAreaById(long areaId, HttpServletRequest request);
	
	public ResponseObject createArea(Area area, HttpServletRequest request);
	
	public ResponseObject updateArea(long areaId, Area area, HttpServletRequest request);
	
	public ResponseObject deleteArea(long areaId, HttpServletRequest request);

}
