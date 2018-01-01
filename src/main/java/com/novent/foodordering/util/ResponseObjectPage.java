package com.novent.foodordering.util;

import java.util.List;

import org.springframework.data.domain.Sort;

public class ResponseObjectPage<T> extends ResponseObject {


	private boolean last;
	private int totalPages;
	private long totalElements;
	private int number;
	private String sort;
	private int size;
	private boolean first;
//	private int numberOfElements;
	private List<T> data;
	
	public ResponseObjectPage(String status, String code, String message, int size, boolean last, int totalPages, long totalElements,
			                  int number, String sort, boolean first, List<T> data) {
		super(status, code, message);
		setSize(size);
		setLast(last);
		setTotalPages(totalPages);
		setTotalElements(totalElements);
		setNumber(number);
		setSort(sort);
		setFirst(first);
//		setNumberOfElements(numberOfElements);
		setData(data);
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public long getTotalElements() {
		return totalElements;
	}
	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}
	public boolean isLast() {
		return last;
	}
	public void setLast(boolean last) {
		this.last = last;
	}
	public boolean isFirst() {
		return first;
	}
	public void setFirst(boolean first) {
		this.first = first;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
}