package com.novent.foodordering.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
import com.novent.foodordering.util.Areas;
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
	public ResponseObject getAreaByStatus(boolean status) {
		ResponseObject response = null;
		List<Area> allAreas = areaDao.findByStatus(status);
		if(!allAreas.isEmpty()){
			List<Areas> jsonAreas = new ArrayList<Areas>(); 
			for (Iterator<Area> iterator = allAreas.iterator(); iterator.hasNext();){
				Area area = iterator.next();
				jsonAreas.add(new Areas(area.getAreaId(), area.getAreaName(), area.getAreaNameAR(), area.getAdministratorId(), area.getAddress(), area.getLongittude(), area.getLattiude(),area.getCreatedAt(), area.getUpdatedAt(), area.getDeletedAt(), area.isStatus()));
			}
			response = new ResponseObjectAll<Areas>(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, jsonAreas);
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
			Areas jsonArea = new Areas(area.getAreaId(), area.getAreaName(), area.getAreaNameAR(), area.getAdministratorId(), area.getAddress(), area.getLongittude(), area.getLattiude(),area.getCreatedAt(), area.getUpdatedAt(), area.getDeletedAt(), area.isStatus());
			response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, jsonArea);
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
		Area areaNameAR = areaDao.findByAreaNameAR(area.getAreaNameAR());
		Administrator administrator = administratorDao.findByAdministratorId(area.getAdministratorId());
		
		boolean valid = (areaName == null && administrator != null) ;
		String regex = "^[\u0621-\u064A0-9 ]+$";
		if(area.getAreaName()== null || area.getAreaName().equals("") ){
			valid = false ;
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_AREANAME_REQUIRED_ERROR);
		} else if(area.getAreaNameAR() == null || area.getAreaNameAR().equals("") ){
			valid = false ;
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_AREANAMEAR_REQUIRED_ERROR);
		} else if(area.getAdministratorId() == 0 ){
			valid = false ;
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_ADMINISTRATORID_REQUIRED_ERROR);
		} else if(administrator == null){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_ADMINISTRATOR_NUMBER_ERROR);
		} else if(!administrator.isStatus()){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_UPDATE_ADMINISTRATOR_ERROR);
		} else if(areaName != null ){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_AREANAME_ALREADY_EXIST_ERROR);
		} else if(!area.getAreaNameAR().matches(regex)){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_ARABICNAME_ERROR);
		} else if(areaNameAR != null ){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_AREANAMEAR_ALREADY_EXIST_ERROR);
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
		boolean valid = true;
		
		Area areaToUpdate = areaDao.findByAreaId(areaId);
		
		String Name = area.getAreaName();
		String areaNameAR = area.getAreaNameAR();
		String address = area.getAddress();
		double longittude = area.getLongittude();
		double lattiude = area.getLattiude();
		long administratorId = area.getAdministratorId();
		
		
		if(areaToUpdate == null){
			valid = false;
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_DELETTING_MESSAGE);
		} else if(!areaToUpdate.isStatus()){
			valid = false;
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_UPDATE_AREA_ERROR);
		}
		
		if (Name != null && !Name.equals("") && valid){
			Area areaName = areaDao.findByAreaName(area.getAreaName());
			if(areaName != null && !areaToUpdate.equals(areaName) ){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_AREANAME_ALREADY_EXIST_ERROR);
			} else if (valid){
				areaToUpdate.setAreaName(Name);
				areaToUpdate.setUpdatedAt(new Date());
				areaDao.save(areaToUpdate);
				Areas jsonArea = new Areas(areaToUpdate.getAreaId(), areaToUpdate.getAreaName(), areaToUpdate.getAreaNameAR(), areaToUpdate.getAdministratorId(), areaToUpdate.getAddress(), areaToUpdate.getLongittude(),
						                   areaToUpdate.getLattiude(),areaToUpdate.getCreatedAt(), areaToUpdate.getUpdatedAt(), areaToUpdate.getDeletedAt(), areaToUpdate.isStatus());
				response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonArea);	
			}
		}
		
		String regex = "^[\u0621-\u064A0-9 ]+$";

		if (areaNameAR != null && !areaNameAR.equals("") && valid){
			Area areaName = areaDao.findByAreaNameAR(area.getAreaNameAR());
			if(areaName != null && !areaToUpdate.equals(areaName) ){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_AREANAMEAR_ALREADY_EXIST_ERROR);
			} else if (!area.getAreaNameAR().matches(regex)){
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_ARABICNAME_ERROR);
			} else if (valid){
				areaToUpdate.setAreaNameAR(Name);
				areaToUpdate.setUpdatedAt(new Date());
				areaDao.save(areaToUpdate);
				Areas jsonArea = new Areas(areaToUpdate.getAreaId(), areaToUpdate.getAreaName(), areaToUpdate.getAreaNameAR(), areaToUpdate.getAdministratorId(), areaToUpdate.getAddress(), areaToUpdate.getLongittude(),
						                   areaToUpdate.getLattiude(),areaToUpdate.getCreatedAt(), areaToUpdate.getUpdatedAt(), areaToUpdate.getDeletedAt(), areaToUpdate.isStatus());
				response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonArea);	
			}
		}
		
		if (administratorId != 0 && valid ){
			Administrator administrator = administratorDao.findByAdministratorId(area.getAdministratorId());
			if(administrator == null){
				valid = false;
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_ADMINISTRATOR_NUMBER_ERROR);
			} else if(!administrator.isStatus()){
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_UPDATE_ADMINISTRATOR_ERROR);
			} else if (valid){
			areaToUpdate.setAdministratorId(administratorId);
			areaToUpdate.setUpdatedAt(new Date());
			areaDao.save(areaToUpdate);
			Areas jsonArea = new Areas(areaToUpdate.getAreaId(), areaToUpdate.getAreaName(), areaToUpdate.getAreaNameAR(), areaToUpdate.getAdministratorId(), areaToUpdate.getAddress(), areaToUpdate.getLongittude(),
	                   areaToUpdate.getLattiude(),areaToUpdate.getCreatedAt(), areaToUpdate.getUpdatedAt(), areaToUpdate.getDeletedAt(), areaToUpdate.isStatus());
			response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonArea);	
			}
		}
		
		if(address != null && !address.equals("") && valid ){
			areaToUpdate.setAddress(address);
			areaToUpdate.setUpdatedAt(new Date());
			areaDao.save(areaToUpdate);
			Areas jsonArea = new Areas(areaToUpdate.getAreaId(), areaToUpdate.getAreaName(), areaToUpdate.getAreaNameAR(), areaToUpdate.getAdministratorId(), areaToUpdate.getAddress(), areaToUpdate.getLongittude(),
	                   areaToUpdate.getLattiude(),areaToUpdate.getCreatedAt(), areaToUpdate.getUpdatedAt(), areaToUpdate.getDeletedAt(), areaToUpdate.isStatus());
			response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonArea);	
			}
		
		if(longittude != 0 && valid ){
			areaToUpdate.setLongittude(longittude);;
			areaToUpdate.setUpdatedAt(new Date());
			areaDao.save(areaToUpdate);
			Areas jsonArea = new Areas(areaToUpdate.getAreaId(), areaToUpdate.getAreaName(), areaToUpdate.getAreaNameAR(), areaToUpdate.getAdministratorId(), areaToUpdate.getAddress(), areaToUpdate.getLongittude(),
	                   areaToUpdate.getLattiude(),areaToUpdate.getCreatedAt(), areaToUpdate.getUpdatedAt(), areaToUpdate.getDeletedAt(), areaToUpdate.isStatus());
			response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonArea);	
			}
		
		if(lattiude != 0 && valid ){
			areaToUpdate.setLattiude(lattiude);
			areaToUpdate.setUpdatedAt(new Date());
			areaDao.save(areaToUpdate);
			Areas jsonArea = new Areas(areaToUpdate.getAreaId(), areaToUpdate.getAreaName(), areaToUpdate.getAreaNameAR(), areaToUpdate.getAdministratorId(), areaToUpdate.getAddress(), areaToUpdate.getLongittude(),
	                   areaToUpdate.getLattiude(),areaToUpdate.getCreatedAt(), areaToUpdate.getUpdatedAt(), areaToUpdate.getDeletedAt(), areaToUpdate.isStatus());
			response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, jsonArea);	
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
