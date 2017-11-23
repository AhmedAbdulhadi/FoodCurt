package com.novent.foodordering.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class OrderItem implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@NotNull
	private long ordreItemId;
	@NotNull
	private long itemId;
	@NotNull
	private String itemName;
	@NotNull
	private double price;
	@NotNull 
	private int quantity;
	private String comment;
	
	@JsonIgnore
	public long getOrdreItemId() {
		return ordreItemId;
	}
	@JsonProperty
	public void setOrdreItemId(long ordreItemId) {
		this.ordreItemId = ordreItemId;
	}
	public long getItemId() {
		return itemId;
	}
	public void setItemId(long itemId) {
		this.itemId = itemId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}