package com.novent.foodordering.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Admin extends Users implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@NotNull
	private long administratorId;
	public enum Privilege {
		EDITOR, MODERATOR 
	};
	private Privilege privilege;
	
	//
	@OneToMany
	private List<Restaurant> restaurant;

	public long getAdministratorId() {
		return administratorId;
	}
	public void setAdministratorId(long administratorId) {
		this.administratorId = administratorId;
	}
	public Privilege getPrivilege() {
		return privilege;
	}
	public void setPrivilege(Privilege privilege) {
		this.privilege = privilege;
	}
	@JsonIgnore
	public List<Restaurant> getRestaurant() {
		return restaurant;
	}
	@JsonProperty
	public void setRestaurant(List<Restaurant> restaurant) {
		this.restaurant = restaurant;
	}	
}
