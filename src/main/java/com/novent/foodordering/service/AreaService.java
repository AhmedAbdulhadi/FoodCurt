package com.novent.foodordering.service;

import org.springframework.stereotype.Service;

import com.novent.foodordering.entity.Area;
import com.novent.foodordering.util.ResponseObject;

@Service
public interface AreaService {
	
	public ResponseObject getAllAreas();
	
	public ResponseObject getAreaById(long areaId);
	
	public ResponseObject createArea(Area area);
	
	public ResponseObject updateArea(long areaId, Area area);
	
	public ResponseObject deleteArea(long areaId);

}
