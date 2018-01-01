package com.novent.foodordering.util;

import java.util.Date;


public class CustomAreas {
	
	private long areaId;
	private String areaName;
	private String areaNameAR;
	private long administratorId;
	private String address;
	private double longittude;
	private double lattiude;
	
	//
	private Date createdAt;
	private Date updatedAt;
	private Date deletedAt;
	private boolean status;
	
	public CustomAreas(long areaId, String areaName, String areaNameAR, long administratorId, String address, double longittude, double lattiude, Date createdAt, Date updatedAt, Date deletedAt, boolean status){
		this.areaId = areaId;
		this.areaName = areaName;
		this.areaNameAR = areaNameAR;
		this.administratorId = administratorId;
		this.address = address;
		this.longittude = longittude;
		this.lattiude = lattiude;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.deletedAt = deletedAt;
		this.status = status;
	}
	
	public long getAreaId() {
		return areaId;
	}
	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public long getAdministratorId() {
		return administratorId;
	}
	public void setAdministratorId(long administratorId) {
		this.administratorId = administratorId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public double getLongittude() {
		return longittude;
	}
	public void setLongittude(double longittude) {
		this.longittude = longittude;
	}
	public double getLattiude() {
		return lattiude;
	}
	public void setLattiude(double lattiude) {
		this.lattiude = lattiude;
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

	public String getAreaNameAR() {
		return areaNameAR;
	}

	public void setAreaNameAR(String areaNameAR) {
		this.areaNameAR = areaNameAR;
	}

}
