package com.abhinav.feedflood;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class FeedFloodRestClient {
	private static final String BASE_URL = "http://api.feedzilla.com/v1/";
	
	private static AsyncHttpClient client = new AsyncHttpClient();
	
	public static void getTopNews(AsyncHttpResponseHandler responseHandler) {
		String url = BASE_URL + "articles.json";
		client.get(url, responseHandler);
	}
	
	public static void getNewsCategories(AsyncHttpResponseHandler responseHandler) {
		String url = BASE_URL + "categories.json";
		client.get(url, responseHandler);
	}
	
	public static void getNewsForCategory(String categoryid, AsyncHttpResponseHandler responseHandler) {
		String url = BASE_URL + "categories/" + categoryid + "/articles.json";
		client.get(url, responseHandler);
	}
	
	public static void getNews(String searchterm, String searchcategoryid, AsyncHttpResponseHandler responseHandler) {
		String url = BASE_URL;
		if(searchcategoryid != null) {
			 url +=  "categories/" + searchcategoryid + "/";
		}
		url += "articles/search.json?q=" + searchterm;
		client.get(url, responseHandler);
	}
}
