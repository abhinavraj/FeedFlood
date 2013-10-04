package com.abhinav.feedflood;

import java.io.Serializable;

public class Category implements Serializable {
	String name;
	String id;
	
	public Category(String name, String id) {
		this.name = name;
		this.id = id;
	}
}
