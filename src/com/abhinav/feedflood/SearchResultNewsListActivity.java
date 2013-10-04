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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;

public class SearchResultNewsListActivity extends Activity {
	private ArrayList<String> newTitleList = new ArrayList<String>();
	private ArrayList<NewsItem> newsList = new ArrayList<NewsItem>();
	
	private ArrayAdapter aNewsTitleList;
	private ActionBar actionBar;
	
	private void successHandler(JSONObject articlelist) {
		JSONArray articles = null;
		newTitleList.clear();
		newsList.clear();
		try {
			articles = (JSONArray) articlelist.get("articles");
			for(int i=0; i < articles.length(); i++) {
				JSONObject articleObj = ((JSONObject)articles.get(i));
				String title = articleObj.getString("title");
				NewsItem newsItem = new NewsItem(title);
				newsItem.publishdate = articleObj.getString("publish_date");
				newsItem.source = articleObj.getString("source");
				newsItem.summary = articleObj.getString("summary");
				newsItem.url = articleObj.getString("url");
				
				Log.d("DEBUG", title);
				newTitleList.add(title);
				newsList.add(newsItem);
			}
			
			aNewsTitleList = new ArrayAdapter(this, android.R.layout.simple_list_item_1, newTitleList);
			ListView lvTopNews = (ListView) findViewById(R.id.lvTopNews);
			lvTopNews.setAdapter(aNewsTitleList);
			
			lvTopNews.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position,
						long rowid) {
					Intent intent = new Intent(getApplicationContext(), NewsSummaryActivity.class);
					intent.putExtra("news", newsList.get(position));
					startActivity(intent);
				}
			});
			
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		String searchterm = (String)getIntent().getStringExtra("searchterm");
		actionBar = getActionBar();
		actionBar.setCustomView(R.layout.actionbar_search_view);
		triggerSearch(searchterm);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case R.id.action_search:
            openSearch();
            return true;
        default:
            return super.onOptionsItemSelected(item);
		}
	}

	private void triggerSearch(final String searchterm) {
		FeedFloodRestClient.getNews(searchterm,null, new JsonHttpResponseHandler() {
			@Override
            public void onSuccess(JSONObject articles) {
				successHandler(articles);
				actionBar.setTitle("News result for " + searchterm);
				actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
            }
			
			@Override
    		public void onFailure(Throwable arg0) {
				Log.d("DEBUG", "news search FAIL");
    		}
		});
	}
	
	private void openSearch() {
		final EditText search = (EditText) actionBar.getCustomView().findViewById(R.id.searchfield);
		final String searchterm = search.getText().toString();
		if(!searchterm.isEmpty()) {
			triggerSearch(searchterm);
			Toast.makeText(SearchResultNewsListActivity.this, "Search news on "+ searchterm,
		            Toast.LENGTH_SHORT).show();
			search.setText("");
		} else {
			search.setHint("Search News");
			actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		    
		    search.setOnEditorActionListener(new OnEditorActionListener() {
		      @Override
		      public boolean onEditorAction(TextView v, int actionId,
		          KeyEvent event) {
		    	triggerSearch(searchterm);
		        Toast.makeText(SearchResultNewsListActivity.this, "Search news on "+ searchterm,
		            Toast.LENGTH_LONG).show();
		        search.setText("");
		        return false;
		      }
		    }); 
		}
	    
	}
}
