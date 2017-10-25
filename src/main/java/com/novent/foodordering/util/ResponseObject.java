package com.novent.foodordering.util;
/*
 * created by novent Group backend team (shakalns) 
 */
public class ResponseObject {

	private String status;
	private String code;
	private String message;
	

	public ResponseObject(String status, String code, String message ) {
		this.setStatus(status);
		this.setCode(code);
		this.setMessage(message);
		
	}
	

	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}
	



}
