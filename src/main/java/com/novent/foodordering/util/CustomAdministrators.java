package com.novent.foodordering.util;

import java.util.Date;

import com.novent.foodordering.entity.Administrator.Privilege;


public class CustomAdministrators {
	
	private long administratorId;
	private String phoneNumber;
	private String userName;
	private String fullName;
	private String email;
	private Privilege privilege;
	
	//
	private Date createdAt;
	private Date updatedAt;
	private Date deletedAt;
	private boolean status;
	
	public CustomAdministrators(long administratorId, String phoneNumber, String userName, String fullName, String email, Privilege privilege, Date createdAt, Date updatedAt,
			             Date deletedAt,boolean status){
		this.administratorId = administratorId;
		this.phoneNumber = phoneNumber;
		this.userName = userName;
		this.fullName = fullName;
		this.email = email;
		this.privilege = privilege;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.deletedAt = deletedAt;
		this.status = status;
	}
	
	
	public long getAdministratorId() {
		return administratorId;
	}
	public void setAdministratorId(long administratorId) {
		this.administratorId = administratorId;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Privilege getPrivilege() {
		return privilege;
	}
	public void setPrivilege(Privilege privilege) {
		this.privilege = privilege;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public Date getDeletedAt() {
		return deletedAt;
	}
	public void setDeletedAt(Date deletedAt) {
		this.deletedAt = deletedAt;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}

}
