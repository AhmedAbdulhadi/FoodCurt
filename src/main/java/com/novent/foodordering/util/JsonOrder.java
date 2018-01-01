package com.novent.foodordering.util;

import java.util.Date;

import com.novent.foodordering.entity.Cart;

public class JsonOrder {
	
	private long orderId;
	private boolean takeAway;
	private int numberOfChair;
	private double totalamount;
	private double amount;
	private double tax = 0.08;
	private long branchId;
	private Date createdAt;
	private Date updatedAt;
	private Date deletedAt;
	private int status;
	private String statusName;
	
	private Cart cart;
	private CustomUser user;
	
	public JsonOrder(long orderId, boolean takeAway, int numberOfChair, double totalamount, double amount, double tax, long branchId, Date createdAt, Date updatedAt, 
			         Date deletedAt, int status, String statusName, Cart cart, CustomUser user){
		this.orderId = orderId;
		this.takeAway = takeAway;
		this.numberOfChair = numberOfChair;
		this.totalamount = totalamount;
		this.amount = amount;
		this.tax = tax;
		this.branchId = branchId;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.deletedAt = deletedAt;
		this.status = status;
		this.statusName = statusName;
		this.cart = cart;
		this.user = user;
	}
	
	
	public long getOrderId() {
		return orderId;
	}
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	public boolean isTakeAway() {
		return takeAway;
	}
	public void setTakeAway(boolean takeAway) {
		this.takeAway = takeAway;
	}
	public int getNumberOfChair() {
		return numberOfChair;
	}
	public void setNumberOfChair(int numberOfChair) {
		this.numberOfChair = numberOfChair;
	}
	public double getTotalamount() {
		return totalamount;
	}
	public void setTotalamount(double totalamount) {
		this.totalamount = totalamount;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getTax() {
		return tax;
	}
	public void setTax(double tax) {
		this.tax = tax;
	}
	public long getBranchId() {
		return branchId;
	}
	public void setBranchId(long branchId) {
		this.branchId = branchId;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public Cart getCart() {
		return cart;
	}
	public void setCart(Cart cart) {
		this.cart = cart;
	}
	public CustomUser getUser() {
		return user;
	}
	public void setUser(CustomUser user) {
		this.user = user;
	}


}
