package com.abhinav.feedflood;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class NewsSummaryActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_summary);
		ActionBar actionBar = getActionBar();
		Intent intent = getIntent();
		final NewsItem news = (NewsItem) intent.getSerializableExtra("news");
		String categoryname = (String)intent.getStringExtra("categoryname");
		actionBar.setSubtitle(news.source);
		actionBar.setTitle(categoryname);
		TextView tvNewsTitle = (TextView)findViewById(R.id.tvNewsTitle);
		tvNewsTitle.setText(news.title);
		
		//TextView tvNewsSource = (TextView)findViewById(R.id.tvNewsSource);
		//tvNewsSource.setText(news.source);
		TextView tvNewsPublishDate = (TextView)findViewById(R.id.tvNewsPublishDate);
		tvNewsPublishDate.setText(news.publishdate);
		TextView tvNewsSummary = (TextView)findViewById(R.id.tvNewsSummary);
		tvNewsSummary.setText(news.summary);
		
		Button btnArticle = (Button)findViewById(R.id.btnOriginalArticle);
		btnArticle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(news.url));
				startActivity(browserIntent);
			}
			
		});
		
	}

}
