package com.grocery;

import java.util.*;

public class Basket {

	private List<Item> bItems = new ArrayList<>();
	private static List<Basket> bList = new ArrayList<>();
	Basket(){
		bList.add(this);
	}
	
	public void addToBasket(Item item) {
		bItems.add(item);
	}
	public List<Item> getList() {
		return bItems;
	}
	public boolean isEmpty() {
		return bItems.isEmpty();
	}
	
	
	
}
