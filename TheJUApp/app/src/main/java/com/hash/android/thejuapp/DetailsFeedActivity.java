package com.hash.android.thejuapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hash.android.thejuapp.Model.Feed;

import static com.hash.android.thejuapp.adapter.FeedRecyclerAdapter.INTENT_EXTRA_FEED;

public class DetailsFeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_details_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        Intent i = getIntent();
        Feed feed = i.getParcelableExtra(INTENT_EXTRA_FEED);


        TextView tx = (TextView) findViewById(R.id.contentTextView);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/opensans.ttf");

        tx.setTypeface(custom_font);
        ImageView feedImage = (ImageView) findViewById(R.id.feedImageView);
        TextView heading = (TextView) findViewById(R.id.headingTextView);
        TextView author = (TextView) findViewById(R.id.authorTextView);

        Glide.with(this)
                .load(feed.getImageURL())
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(feedImage);
        heading.setText(feed.getHeading());
        author.setText(feed.getAuthor());
        try {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
