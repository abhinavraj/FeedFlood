package com.abhinav.feedflood;

import java.io.Serializable;

public class NewsItem implements Serializable {
	String title;
	String source;
	String author;
	String publishdate;
	String summary;
	String url;
	
	public NewsItem(String title) {
		this.title = title;
	}
}
