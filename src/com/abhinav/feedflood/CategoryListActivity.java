package com.abhinav.feedflood;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class CategoryListActivity extends Activity {
	ArrayList<String> newsCategories = new ArrayList<String>();
	ArrayList<Category> categoryList =  new ArrayList<Category>();
	ArrayAdapter aNewsCategories;
	
	private void getCategoriesSuccessHandler(JSONArray categories) {
		try {
			for(int i=0; i < categories.length(); i++) {
				JSONObject categoryObj = ((JSONObject)categories.get(i));
				String name = categoryObj.getString("english_category_name");
				String id = categoryObj.getString("category_id");
				Category category = new Category(name, id);
				
				newsCategories.add(name);
				categoryList.add(category);
			}
			
			aNewsCategories = new ArrayAdapter(this, android.R.layout.simple_list_item_1, newsCategories);
			ListView lvCategoryList = (ListView) findViewById(R.id.lvCategoryList);
			lvCategoryList.setAdapter(aNewsCategories);
			
			lvCategoryList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position,
						long rowid) {
					Intent intent = new Intent(getApplicationContext(), CategoryNewsListActivity.class);
					intent.putExtra("category", categoryList.get(position));
					startActivity(intent);
				}
			});
			
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}
	
	private void getCategories() {
		FeedFloodRestClient.getNewsCategories( new JsonHttpResponseHandler() {
			@Override
            public void onSuccess(JSONArray categories) {
				getCategoriesSuccessHandler(categories);
            }
			
			@Override
    		public void onFailure(Throwable arg0) {
				Log.d("DEBUG", "getNewsCategories FAIL");
    		}
		});
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_list);
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("News Categories");
		getCategories();
	}
}
