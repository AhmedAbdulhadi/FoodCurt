package com.novent.foodordering.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Administrator extends Users implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public enum Privilege {
		SUPER,EDITOR, MODERATOR 
	};
	private Privilege privilege;
	
	//
	@OneToMany
	private List<Admin> admins;
	@OneToMany
	private List<Area> areas;
	
	
	public Privilege getPrivilege() {
		return privilege;
	}
	public void setPrivilege(Privilege privilege) {
		this.privilege = privilege;
	}
	@JsonIgnore
	public List<Admin> getAdmins() {
		return admins;
	}
	@JsonProperty
	public void setAdmins(List<Admin> admins) {
		this.admins = admins;
	}
	@JsonIgnore
	public List<Area> getAreas() {
		return areas;
	}
	@JsonProperty
	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}
}