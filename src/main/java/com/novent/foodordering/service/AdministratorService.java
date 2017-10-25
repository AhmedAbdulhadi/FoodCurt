package com.novent.foodordering.service;

import org.springframework.stereotype.Service;

import com.novent.foodordering.entity.Administrator;
import com.novent.foodordering.util.ResponseObject;

@Service
public interface AdministratorService {

    public ResponseObject getAllAdministrators();
	
	public ResponseObject getAdministratorById(long AdministratorId);
	
	public ResponseObject createAdministrator(Administrator administrator);
	
	public ResponseObject updateAdministrator(long administratorId, Administrator administrator);
	
	public ResponseObject deleteAdministrator(long administratorId);
}
