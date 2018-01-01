package com.novent.foodordering.util;

import java.util.List;

/*
 * created by novent Group backend team (shakalns) 
 */
public class ResponseObjectAll<T> extends ResponseObject {
	
	private int size;
	private List<T> data;	
	
	public ResponseObjectAll(String status, String code, String message , List<T> data) {
		super(status, code, message);
		this.setData(data);
		this.setSize(data.size());
	}
	
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
}
