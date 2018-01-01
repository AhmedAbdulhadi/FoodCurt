package com.novent.foodordering.util;

import java.util.List;

import com.novent.foodordering.entity.OrderItem;

public class CustomOrder {
	private long userId;
	private long branchId;
	private int numberOfChair;
	private boolean takeAway;
	private List<OrderItem> items ;
	
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getBranchId() {
		return branchId;
	}
	public void setBranchId(long branchId) {
		this.branchId = branchId;
	}
	public int getNumberOfChair() {
		return numberOfChair;
	}
	public void setNumberOfChair(int numberOfChair) {
		this.numberOfChair = numberOfChair;
	}
	public boolean getTakeAway() {
		return takeAway;
	}
	public void setTakeAway(boolean takeAway) {
		this.takeAway = takeAway;
	}
	public List<OrderItem> getItems() {
		return items;
	}
	public void setItems(List<OrderItem> items) {
		this.items = items;
	}
	

}
