package com.novent.foodordering.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Orders implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@NotNull
	private long orderId;
	@NotNull
	private boolean takeAway;
	private int numberOfChair;
	private double totalamount;
	private long branchId;
	private long userId;
	//
	private Date createdAt;
	private Date updatedAt;
	private Date deletedAt;
	private int status;
	private String statusName;
	
	//Relations
	@OneToOne
	private Cart cart;
	@OneToOne
	private Users user;
	
	public Orders(){
		setCreatedAt(new Date());
		setStatus(1);
		setStatusName(0);
	}
	public long getOrderId() {
		return orderId;
	}
	public void setOrderId(long orderId) {
		this.orderId = orderId;
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
	public double getTotalamount() {
		return totalamount;
	}
	public void setTotalamount(double totalamount) {
		this.totalamount = totalamount;
	}
	@JsonIgnore
	public Date getCreatedAt() {
		return createdAt;
	}
	@JsonProperty
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	@JsonIgnore
	public Date getUpdatedAt() {
		return updatedAt;
	}
	@JsonProperty
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	@JsonIgnore
	public Date getDeletedAt() {
		return deletedAt;
	}
	@JsonProperty
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
	public void setStatusName(int status) {
		switch (status) {
		case 0 : statusName = "New order";break;
		case 1 : statusName = "Under cooking";break;
		case 2 : statusName = "finished";break;
		case 3 : statusName = "canceled";break;
		
		}
	}
	public Cart getCart() {
		return cart;
	}
	public void setCart(Cart cart) {
		this.cart = cart;
	}
	public long getBranchId() {
		return branchId;
	}
	public void setBranchId(long branchId) {
		this.branchId = branchId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public Users getUser() {
		return user;
	}
	public void setUser(Users user) {
		this.user = user;
	}
}