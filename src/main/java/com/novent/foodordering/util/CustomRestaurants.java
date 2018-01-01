package com.novent.foodordering.util;

import java.util.Date;


public class CustomRestaurants {

	private long restaurantId;
	private String restaurantName;
	private String restaurantNameAR;
	private String phoneNumber;	
	private String userName;
	private String email;
	private long adminId;
	private int numberOfBranches;
	private String rate;
	private String workingHours;
	
	//
	private Date createdAt;
	private Date updatedAt;
	private Date deletedAt;
	private boolean status;
	
	public CustomRestaurants(long restaurantId, String restaurantName, String restaurantNameAR, String phoneNumber, String userName, String email, long adminId, int numberOfBranches,
			           String rate,  String workingHours, Date createdAt, Date updatedAt, Date deletedAt, boolean status){
		this.restaurantId = restaurantId;
		this.restaurantName = restaurantName;
		this.restaurantNameAR = restaurantNameAR;
		this.phoneNumber = phoneNumber;
		this.userName = userName;
		this.email = email;
		this.adminId = adminId;
		this.numberOfBranches = numberOfBranches;
		this.rate = rate;
		this.workingHours = workingHours;
		this.createdAt = createdAt; 
		this.updatedAt = updatedAt;
		this.deletedAt = deletedAt;
		this.status = status;
	}
	
	
	public long getRestaurantId() {
		return restaurantId;
	}
	public void setRestaurantId(long restaurantId) {
		this.restaurantId = restaurantId;
	}
	public String getRestaurantName() {
		return restaurantName;
	}
	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public long getAdminId() {
		return adminId;
	}
	public void setAdminId(long adminId) {
		this.adminId = adminId;
	}
	public int getNumberOfBranches() {
		return numberOfBranches;
	}
	public void setNumberOfBranches(int numberOfBranches) {
		this.numberOfBranches = numberOfBranches;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getWorkingHours() {
		return workingHours;
	}
	public void setWorkingHours(String workingHours) {
		this.workingHours = workingHours;
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


	public String getRestaurantNameAR() {
		return restaurantNameAR;
	}


	public void setRestaurantNameAR(String restaurantNameAR) {
		this.restaurantNameAR = restaurantNameAR;
	}

}
