package com.novent.foodordering.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.novent.foodordering.constatnt.ResponseCode;
import com.novent.foodordering.constatnt.ResponseMessage;
import com.novent.foodordering.constatnt.ResponseStatus;
import com.novent.foodordering.dao.AdministratorDao;
import com.novent.foodordering.dao.AreaDao;
import com.novent.foodordering.entity.Administrator;
import com.novent.foodordering.entity.Area;
import com.novent.foodordering.service.AreaService;
import com.novent.foodordering.util.ResponseObject;
import com.novent.foodordering.util.ResponseObjectAll;
import com.novent.foodordering.util.ResponseObjectCrud;
import com.novent.foodordering.util.ResponseObjectData;

@Service
@Component
public class AreaServiceImpl implements AreaService{
	
	@Autowired
	private AreaDao areaDao;
	@Autowired 
	private AdministratorDao administratorDao;

	@Override
	public ResponseObject getAllAreas() {
		ResponseObject response = null;
		List<Area> allAreas = areaDao.findAll();
		if(!allAreas.isEmpty()){
			response = new ResponseObjectAll<>(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, allAreas);
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_GETTING_MESSAGE);
		}
		return response;
	}

	@Override
	public ResponseObject getAreaById(long areaId) {
		ResponseObject response = null;
		Area area = areaDao.findByAreaId(areaId);
		if (area != null){
			response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, area);
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_GETTING_MESSAGE);
		}
		return response;
	}

	@Override
	public ResponseObject createArea(Area area) {
		ResponseObject response = null;
		long id = 0;

		Area areaName = areaDao.findByAreaName(area.getAreaName());
		Administrator administrator = administratorDao.findByAdministratorId(area.getAdministratorId());
		
		boolean valid = (areaName == null && administrator != null) ;
		
		if(administrator == null){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_ADMINISTRATOR_NUMBER_ERROR);
		} else if(areaName != null ){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_AREANAME_ALREADY_EXIST_ERROR);
		} else if(valid){
			areaDao.save(area);
			id =area.getAreaId();
			List<Area> areas =administrator.getAreas();
			areas.add(area);
			administrator.setAreas(areas);
			administratorDao.save(administrator);
			response = new ResponseObjectCrud(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_CREATE_CODE, ResponseMessage.SUCCESS_CREATING_MESSAGE, id);
		} else{
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_CREATING_MESSAGE);
		} 
		return response;
	}

	@Override
	public ResponseObject updateArea(long areaId, Area area) {
		ResponseObject response = null;
		
		Area areaToUpdate = areaDao.findByAreaId(areaId);
		Area areaName = areaDao.findByAreaName(area.getAreaName());
		
		String Name = area.getAreaName();
		String address = area.getAddress();
		double longittude = area.getLongittude();
		double lattiude = area.getLattiude();
		
		boolean valid = (areaName == null && (areaToUpdate != null &&areaToUpdate.isStatus()) ) ||( areaToUpdate.equals(areaName));
		
		if(areaToUpdate == null || !areaToUpdate.isStatus()){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_DELETTING_MESSAGE);
		} else if(areaName != null && !areaToUpdate.equals(areaName) ){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_AREANAME_ALREADY_EXIST_ERROR);
		} else if(valid){
			areaToUpdate.setAreaName(Name);
			areaToUpdate.setAddress(address);
			areaToUpdate.setLongittude(longittude);
			areaToUpdate.setLattiude(lattiude);
			areaToUpdate.setUpdatedAt(new Date());
			areaDao.save(areaToUpdate);
			response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, areaToUpdate);
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_UPDATING_MESSAGE);
		}
		return response;
	}

	@Override
	public ResponseObject deleteArea(long areaId) {
		ResponseObject response = null;
		Area area = areaDao.findByAreaId(areaId);
		if(area != null && area.isStatus()){
			area.setStatus(false);
			area.setDeletedAt(new Date());
			areaDao.save(area);
			response = new ResponseObjectCrud(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_DELETTING_MESSAGE, areaId);
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_DELETTING_MESSAGE);
		}
		return response;
	}

}
