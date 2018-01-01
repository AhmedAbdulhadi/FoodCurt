package com.novent.foodordering.util;

public class CustomUser {
	
	private long userId;
	private String phoneNumber;
	private String fullName;
	
	public CustomUser (long userId, String phoneNumber, String fullName){
		this.userId = userId;
		this.phoneNumber = phoneNumber;
		this.fullName = fullName;
		
	}
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

}
