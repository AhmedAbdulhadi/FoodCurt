package com.novent.foodordering.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;


@Entity
public class Restaurant extends Users implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@NotNull
	private String restaurantName;
	@NotNull
	private String restaurantNameAR;
	@NotNull
	private long adminId;
	private int numberOfBranches;
	private String rate;
	private String workingHours;

	//Relations
	@OneToMany
	private List<Item> items;
	@OneToMany
	private List<Branch> branches;
	
	
	public String getRestaurantName() {
		return restaurantName;
	}
	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}
	public String getRestaurantNameAR() {
		return restaurantNameAR;
	}
	public void setRestaurantNameAR(String restaurantNameAR) {
		this.restaurantNameAR = restaurantNameAR;
	}
	public long getAdminId() {
		return adminId;
	}
	public void setAdminId(long adminId) {
		this.adminId = adminId;
	}
	public int getNumberOfBranches() {
		return numberOfBranches;
	}
	public void setNumberOfBranches(int numberOfBranches) {
		this.numberOfBranches = numberOfBranches;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getWorkingHours() {
		return workingHours;
	}
	public void setWorkingHours(String workingHours) {
		this.workingHours = workingHours;
	}
	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
	public List<Branch> getBranches() {
		return branches;
	}
	public void setBranches(List<Branch> branches) {
		this.branches = branches;
	}
}