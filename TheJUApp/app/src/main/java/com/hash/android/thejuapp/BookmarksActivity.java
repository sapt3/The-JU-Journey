package com.hash.android.thejuapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseIndexRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hash.android.thejuapp.HelperClass.PreferenceManager;
import com.hash.android.thejuapp.Model.Feed;
import com.hash.android.thejuapp.ViewHolder.FeedHolder;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import static com.hash.android.thejuapp.adapter.FeedRecyclerAdapter.INTENT_EXTRA_FEED;

public class BookmarksActivity extends AppCompatActivity {

    private static final String TAG = BookmarksActivity.class.getSimpleName();
    private FirebaseIndexRecyclerAdapter<Feed, FeedHolder> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.bookmarksRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = mRef.child("posts").getRef();
        String uid = new PreferenceManager(this).getUID();
        DatabaseReference keyRef = mRef.child("users").child(uid).child("bookmarks").getRef();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        mAdapter = new FirebaseIndexRecyclerAdapter<Feed, FeedHolder>(
                Feed.class,
                R.layout.recycler_child_feed,
                FeedHolder.class,
                keyRef,
                ref) {
            @Override
            protected void populateViewHolder(FeedHolder viewHolder, Feed model, int position) {
//                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "populateViewHolder:: " + model.getHeading());
                viewHolder.setAuthor(model.getAuthor());
                viewHolder.setImage(model.getImageURL(), BookmarksActivity.this);
                viewHolder.setHeading(model.getHeading());
                viewHolder.setShortDesc(model.getShortDesc());
                viewHolder.setTime(model.getTime(), sdf);

            }

            @Override
            public FeedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                FeedHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.setOnClickListener(new FeedHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.d(TAG, "onClick::" + position);
                        Intent i = new Intent(BookmarksActivity.this, DetailsFeedActivity.class);
                        i.putExtra(INTENT_EXTRA_FEED, mAdapter.getItem(position));
                        i.putExtra(Intent.EXTRA_TEXT, mAdapter.getRef(position).getKey());
                        Pair<View, String> pair1 = Pair.create(view.findViewById(R.id.postImageView), "sharedImage");
                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(BookmarksActivity.this, pair1);
                        startActivity(i, optionsCompat.toBundle());
                    }
                });
                return viewHolder;
            }


        };


        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

}
