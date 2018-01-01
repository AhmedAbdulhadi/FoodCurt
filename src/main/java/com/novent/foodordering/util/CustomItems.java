package com.novent.foodordering.util;

import java.util.List;

import com.novent.foodordering.entity.Item;

public class CustomItems {

	private long restaurantId;
	private List<Item> items;

	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
	public long getRestaurantId() {
		return restaurantId;
	}
	public void setRestaurantId(long restaurantId) {
		this.restaurantId = restaurantId;
	}
}
