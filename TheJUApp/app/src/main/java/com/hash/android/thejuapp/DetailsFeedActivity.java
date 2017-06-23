package com.hash.android.thejuapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.hash.android.thejuapp.HelperClass.PreferenceManager;
import com.hash.android.thejuapp.Model.Feed;

import static com.hash.android.thejuapp.adapter.FeedRecyclerAdapter.INTENT_EXTRA_FEED;

public class DetailsFeedActivity extends AppCompatActivity {
    private static final String TAG = DetailsFeedActivity.class.getSimpleName();
    private String key;
    private String uid;
    TextView favouritesTextView;
    DatabaseReference starPostRef;
    DatabaseReference globalPostRef;
    private Feed feed;

    @Override
    protected void onPause() {
        super.onPause();
        starPostRef.removeEventListener(valueEventListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feed_menu, menu);
        return true;
//        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_details_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        Intent i = getIntent();
        feed = i.getParcelableExtra(INTENT_EXTRA_FEED);

        key = i.getStringExtra(Intent.EXTRA_TEXT);

        TextView tx = (TextView) findViewById(R.id.contentTextView);
        tx.setTextSize(16f);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/opensans.ttf");

        tx.setTypeface(custom_font);
        ImageView feedImage = (ImageView) findViewById(R.id.feedImageView);
        TextView heading = (TextView) findViewById(R.id.headingTextView);
        TextView author = (TextView) findViewById(R.id.authorTextView);
        favouritesTextView = (TextView) findViewById(R.id.favouritesTextView);

        starPostRef = FirebaseDatabase.getInstance().getReference().child("posts").child(key).child("starCount");
        globalPostRef = FirebaseDatabase.getInstance().getReference().child("posts").child(key);
        favouritesTextView.setText("");
        starPostRef.addValueEventListener(valueEventListener);

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

        uid = new PreferenceManager(this).getUID();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton3);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStarClicked(globalPostRef);
            }
        });
    }


    private void onStarClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                final Feed p = mutableData.getValue(Feed.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.stars.containsKey(uid)) {
                    // Unstar the post and remove self from stars
                    p.starCount = p.starCount - 1;
                    p.stars.remove(uid);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            favouritesTextView.setText(p.starCount + " Favourites");
                        }
                    });
                } else {
                    // Star the post and add self to stars
                    p.starCount = p.starCount + 1;
                    p.stars.put(uid, true);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            favouritesTextView.setText(p.starCount + " Favourites");
                        }
                    });
                }

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (item.getItemId() == R.id.bookmarkMenu) {
            saveToDatabase();
        }
        return super.onOptionsItemSelected(item);

    }

    private void saveToDatabase() {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("bookmarks").child(key).getRef();
        mRef.setValue(true);
        //        mRef.setValue()
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            try {
                int count = dataSnapshot.getValue(Integer.class);
                Log.d(TAG, "onDataChange ::" + count);
                favouritesTextView.setText(count + " Favourites");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}
