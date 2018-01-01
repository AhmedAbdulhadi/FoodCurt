package com.novent.foodordering.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.novent.foodordering.entity.Admin;
import com.novent.foodordering.util.ResponseObject;

@Service
public interface AdminService {

	public ResponseObject getAdminByStatus(boolean status, HttpServletRequest request, String sort, Pageable pageable);
	
	public ResponseObject getAllAdmins(HttpServletRequest request, String sort);
	
	public ResponseObject getAdminById(long adminId, HttpServletRequest request);
	
	public ResponseObject createAdmin(Admin admin, HttpServletRequest request);
	
	public ResponseObject updateAdmin(long userId, Admin admin, HttpServletRequest request);
	
	public ResponseObject deleteAdmin(long userId, HttpServletRequest request);
	
}
