package com.novent.foodordering.util;

public class Login {
	
	private String userName;
	private String password;
	private boolean remmberMe;
	
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public boolean isRemmberMe() {
		return remmberMe;
	}
	public void setRemmberMe(boolean remmberMe) {
		this.remmberMe = remmberMe;
	}
}
