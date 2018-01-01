package com.novent.foodordering.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.novent.foodordering.entity.Administrator;
import com.novent.foodordering.util.ResponseObject;

@Service
public interface AdministratorService {

	public ResponseObject getAdministratorsByStatus(boolean status, HttpServletRequest request, String sort, Pageable pageable);
	
	public ResponseObject getAllAdministrators(HttpServletRequest request, String sort);
	
	public ResponseObject getAdministratorById(long AdministratorId, HttpServletRequest request);
	
	public ResponseObject createAdministrator(Administrator administrator, HttpServletRequest request);
	
	public ResponseObject updateAdministrator(long administratorId, Administrator administrator, HttpServletRequest request);
	
	public ResponseObject deleteAdministrator(long administratorId, HttpServletRequest request);
}
