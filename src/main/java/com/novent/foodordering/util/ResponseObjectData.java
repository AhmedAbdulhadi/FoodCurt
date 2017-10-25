package com.novent.foodordering.util;
/*
 * created by novent Group backend team (shakalns) 
 */
public class ResponseObjectData extends ResponseObject {

	private Object data ;
	
	
	public ResponseObjectData(String status, String code, String message ,Object data ) {
		super(status, code, message);
		this.setData(data);
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	} 
}
