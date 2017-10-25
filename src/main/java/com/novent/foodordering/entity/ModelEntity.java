package com.novent.foodordering.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class ModelEntity implements Serializable {

	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@NotNull
	private long modelEntityId; // primary key 
	@Column(unique = true)
	@NotNull
	private String email;
	@NotNull
	@Column(unique = true)
	private String phoneNumber;
	@NotNull
	@Column(unique = true)
	private String userName;
	@NotNull
	private String fullName; 
	@NotNull
	private Date createdAt;
	@NotNull
	private boolean status;
	@NotNull
	private String password;
	@NotNull
	private String token;
	
	

	//
	private Date updatedAt;
	private Date deletedAt;

	// Relations
	
	public ModelEntity(){
		setStatus(true);
		setCreatedAt(new Date());
		setToken("0000");
	}
	
	
	public ModelEntity(String fullName, String email, String phoneNumber , String password , String userName , int role) {
		this.fullName = fullName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.userName = userName;
		setStatus(true);
		setCreatedAt(new Date());
		setToken("0000");
		
		}
	
	
	
	
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber (String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public long getModelEntityId() {
		return modelEntityId;
	}

	public void setModelEntityId(long modelEntityId) {
		this.modelEntityId = modelEntityId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public void setPassword(String password) {
		this.password = password;
	}

	
	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "ModelEntity [fullName=" + fullName + ", email=" + email + ", password=" + password + "]";
	}
	public String getPassword() {
		return password;
	}

	public Date getDeletedAt() {
		return deletedAt;
	}
	public void setDeletedAt(Date deletedAt) {
		this.deletedAt = deletedAt;
	}
	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}



	

}
