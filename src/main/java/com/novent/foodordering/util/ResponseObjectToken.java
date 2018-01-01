package com.novent.foodordering.util;

public class ResponseObjectToken extends ResponseObject {
	
	private long id;
	private String token;
	
	public ResponseObjectToken(String status, String code, String message , long id , String token) {
		super(status, code, message);
		setId(id);
		setToken(token);
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
