package com.abhinav.feedflood;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

public class MainActivity extends Activity {

	private ArrayList<String> topNewsTitleList = new ArrayList<String>();
	private ArrayList<NewsItem> topNews = new ArrayList<NewsItem>();
	
	private ArrayAdapter aTopNewsTitleList;
	
	private NewsItem createNewsItem(JSONObject article) throws JSONException {
		String title = article.getString("title");
		NewsItem newsItem = new NewsItem(title);
		newsItem.publishdate = article.getString("publish_date");
		newsItem.source = article.getString("source");
		newsItem.summary = article.getString("summary");
		newsItem.url = article.getString("url");
		return newsItem;
	}
	
	private void getTopNewsSuccessHandler(JSONObject articlelist) {
		JSONArray articles = null;
		topNewsTitleList.clear();
		topNews.clear();
		try {
			articles = (JSONArray) articlelist.get("articles");
			for(int i=0; i < articles.length(); i++) {
				JSONObject articleObj = ((JSONObject)articles.get(i));
				NewsItem newsItem = createNewsItem(articleObj);				
				topNewsTitleList.add(newsItem.title);
				topNews.add(newsItem);
			}
			
			aTopNewsTitleList = new ArrayAdapter(this, android.R.layout.simple_list_item_1, topNewsTitleList);

			ListView lvTopNews = (ListView) findViewById(R.id.lvTopNews);
			lvTopNews.setAdapter(aTopNewsTitleList);
			
			setUpOnClickListener(lvTopNews);			
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}
	
	private void setUpOnClickListener(ListView lv) {
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long rowid) {
				Intent intent = new Intent(getApplicationContext(), NewsSummaryActivity.class);
				intent.putExtra("news", topNews.get(position));
				intent.putExtra("categoryname", "Top News");
				startActivity(intent);
			}
		});
	}
	
	private void getTopNews() {
		FeedFloodRestClient.getTopNews( new JsonHttpResponseHandler() {
			@Override
            public void onSuccess(JSONObject articles) {
				getTopNewsSuccessHandler(articles);
            }
			
			@Override
    		public void onFailure(Throwable arg0) {
				Log.d("DEBUG", "getTopNews FAIL");
    		}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Top News");
		actionBar.setCustomView(R.layout.actionbar_search_view);
		getTopNews();
	}

	private void openCategories() {
		Intent intent = new Intent(getApplicationContext(), CategoryListActivity.class);
		startActivity(intent);
	}
	
	private void openSearch() {
		ActionBar actionBar = getActionBar();
		final EditText search = (EditText) actionBar.getCustomView().findViewById(R.id.searchfield);
		final String searchterm = search.getText().toString();
		Log.d("DEBUG", "searchterm:"+searchterm);
		if(!searchterm.isEmpty()) {
			Intent intent = new Intent(getApplicationContext(), SearchResultNewsListActivity.class);
	    	intent.putExtra("searchterm", searchterm);
	  	    startActivity(intent);
			Toast.makeText(MainActivity.this, "Search news on "+ searchterm,
		            Toast.LENGTH_SHORT).show();
			search.setText("");
		} else {
			search.setHint("Search News");
			actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
			
		    search.setOnEditorActionListener(new OnEditorActionListener() {
		    
		      @Override
		      public boolean onEditorAction(TextView v, int actionId,
		          KeyEvent event) {

		    	  Intent intent = new Intent(getApplicationContext(), SearchResultNewsListActivity.class);
		    	  intent.putExtra("searchterm", searchterm);
		  		  startActivity(intent);
		  		  Toast.makeText(MainActivity.this, "Search news on "+ searchterm,
		            Toast.LENGTH_LONG).show();
		  		  search.setText("");
		  		  return false;
		      }
		    });
		    
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case R.id.action_search:
            openSearch();
            return true;
        case R.id.action_categories:
        	openCategories();
            return true;
        default:
            return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
	}
}
