package com.novent.foodordering.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Admin implements Serializable{

private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@NotNull
	private long adminId;
	@NotNull
	@Column(unique=true)
	private String phoneNumber;
	@NotNull
	@Column(unique=true)
	private String userName;
	@NotNull
	private String fullName;
	@NotNull
	private String password;
	@NotNull
	@Column(unique=true)
	@Email(message = "Enter valide Email Address")
	private String email;
	@NotNull
	private long administratorId;
	public enum Privilege {
		EDITOR, MODERATOR 
	};
	private Privilege privilege;
	
	//
	private Date createdAt;
	private Date updatedAt;
	private Date deletedAt;
	private boolean status;
	
	//
	@OneToMany
	private List<Restaurant> restaurant;
	
	public Admin(){
		setCreatedAt(new Date());
		setStatus(true);
	}
	
	public long getAdminId() {
		return adminId;
	}
	public void setAdminId(long adminId) {
		this.adminId = adminId;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	
	public boolean isStatus() {
		return status;
	}
	
	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Privilege getPrivilege() {
		return privilege;
	}

	public void setPrivilege(Privilege privilege) {
		this.privilege = privilege;
	}

	public List<Restaurant> getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(List<Restaurant> restaurant) {
		this.restaurant = restaurant;
	}

	public long getAdministratorId() {
		return administratorId;
	}

	public void setAdministratorId(long administratorId) {
		this.administratorId = administratorId;
	}
}

