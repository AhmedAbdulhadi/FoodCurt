package com.novent.foodordering.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Users implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@NotNull
	private long userId;
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
	
	
	//
	private Date createdAt;
	private Date updatedAt;
	private Date deletedAt;
	private boolean status;

	
	public Users(){
		setCreatedAt(new Date());
		setStatus(true);
	}
	
	public Users(String userName, String phoneNumber){
		this.userName = userName;
		this.phoneNumber = phoneNumber;
		
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
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

//	public Orders getOrder() {
//		return order;
//	}
//
//	public void setOrder(Orders order) {
//		this.order = order;
//	}
}
