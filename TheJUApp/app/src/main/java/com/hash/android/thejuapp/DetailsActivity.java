package com.hash.android.thejuapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hash.android.thejuapp.Model.Feed;

import static com.hash.android.thejuapp.adapter.FeedRecyclerAdapter.INTENT_EXTRA_FEED;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = DetailsActivity.class.getSimpleName();
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feed_menu, menu);
        return true;
//        return super.onCreateOptionsMenu(menu);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        ImageView imageView = (ImageView) findViewById(R.id.image);




        Intent i = getIntent();
        Feed feed = i.getParcelableExtra(INTENT_EXTRA_FEED);

        collapsingToolbarLayout.setTitle(feed.getHeading());
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);


        TextView tx = (TextView)findViewById(R.id.contentTextView);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/opensans.ttf");

        tx.setTypeface(custom_font);

        Glide.with(this)
                .load(feed.getImageURL())
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
