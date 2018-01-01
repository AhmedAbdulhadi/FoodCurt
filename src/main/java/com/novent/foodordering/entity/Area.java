package com.novent.foodordering.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Area implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@NotNull
	private long areaId;
	@NotNull
	@Column(unique=true)
	private String areaName;
	@NotNull
	@Column(unique=true)
	private String areaNameAR;
	@NotNull
	private long administratorId;
	private String address;
	private double longittude;
	private double lattiude;
	
	//
	private Date createdAt;
	private Date updatedAt;
	private Date deletedAt;
	private boolean status;
	
	//
	@OneToMany
	private List<Branch> branches;
	
	public Area(){
		setCreatedAt(new Date());
		setStatus(true);
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
	public long getAdministratorId() {
		return administratorId;
	}
	public void setAdministratorId(long administratorId) {
		this.administratorId = administratorId;
	}
	@JsonIgnore
	public List<Branch> getBranches() {
		return branches;
	}
	@JsonProperty
	public void setBranches(List<Branch> branches) {
		this.branches = branches;
	}

	public String getAreaNameAR() {
		return areaNameAR;
	}

	public void setAreaNameAR(String areaNameAR) {
		this.areaNameAR = areaNameAR;
	}
}