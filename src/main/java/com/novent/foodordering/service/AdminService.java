package com.novent.foodordering.service;

import org.springframework.stereotype.Service;

import com.novent.foodordering.entity.Admin;
import com.novent.foodordering.util.ResponseObject;

@Service
public interface AdminService {

    public ResponseObject getAllAdmins();
	
	public ResponseObject getAdminById(long adminId);
	
	public ResponseObject createAdmin(Admin admin);
	
	public ResponseObject updateAdmin(long adminId, Admin admin);
	
	public ResponseObject deleteAdmin(long adminId);
}
