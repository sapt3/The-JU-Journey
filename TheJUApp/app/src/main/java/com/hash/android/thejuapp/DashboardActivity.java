package com.hash.android.thejuapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hash.android.thejuapp.HelperClass.CircleTransform;
import com.hash.android.thejuapp.HelperClass.PreferenceManager;
import com.hash.android.thejuapp.Model.Feed;
import com.hash.android.thejuapp.Model.Topic;
import com.hash.android.thejuapp.Model.User;
import com.hash.android.thejuapp.ViewHolder.FeedHolder;
import com.hash.android.thejuapp.adapter.TopicsRecyclerAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

import static com.hash.android.thejuapp.adapter.FeedRecyclerAdapter.INTENT_EXTRA_FEED;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    ArrayList<Feed> mFeedList = new ArrayList<>();
    private RecyclerView feedRecyclerView;
    PreferenceManager mPrefsManager;
    private final String TAG = DashboardActivity.class.getSimpleName();
    private ArrayList<Topic> topicsArrayList = new ArrayList<>();
    private final String URL_NAV_BACKGROUND = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private CardView cardView;
    public static FirebaseRecyclerAdapter<Feed, FeedHolder> mAdapter;

    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            feedRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, feedRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mPrefsManager = new PreferenceManager(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(DashboardActivity.this, LoginActivityPre.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //If this message has been shown before then quickly skip this when layout is inflated.

        final PreferenceManager mPrefsManager = new PreferenceManager(this);
        User user = mPrefsManager.getUser();
        //User class has access to all the variables accessed by the user.

        View navHeader = navigationView.getHeaderView(0);
        TextView txtName = (TextView) navHeader.findViewById(R.id.name);
        TextView emailTextView = (TextView) navHeader.findViewById(R.id.website);
        ImageView header = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        ImageView profileImage = (ImageView) navHeader.findViewById(R.id.img_profile);
        ImageView noNotificationIV = (ImageView) findViewById(R.id.noNotifcationImageView);
        ImageView yesNotificationIV = (ImageView) findViewById(R.id.yesNotifcationImageView);
        TextView noNotificationTV = (TextView) findViewById(R.id.noNotificationsTextView);
        TextView yesNotificationTV = (TextView) findViewById(R.id.yesNotificationsTextView);
        cardView = (CardView) findViewById(R.id.cardView);
        if (mPrefsManager.isNotificationEnabled()) {
            cardView.setVisibility(View.GONE);
        }

//        StreamAnalyticsAuth auth = new StreamAnalyticsAuth(getString(R.string.API_KEY),getString(R.string.API_TOKEN));
//        StreamAnalytics client = StreamAnalyticsImpl.getInstance(auth);


        txtName.setText(user.getName());
        emailTextView.setText(user.getEmail());

        header.setImageResource(R.drawable.navheader);
        Glide.with(this)
                .load(user.getPhotoURL())
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(profileImage);


        noNotificationIV.setOnClickListener(this);
        noNotificationTV.setOnClickListener(this);
        yesNotificationIV.setOnClickListener(this);
        yesNotificationTV.setOnClickListener(this);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.topicsRecyclerView);
//        LinearSnapHelper helper = new LinearSnapHelper();
//        helper.attachToRecyclerView(mRecyclerView); //Attaching a linear helper to allow the vertical recycler view to snap to center.
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator()); //Create a default item animater when the view comes to range
        updateData(); //Call the function to update the data set.
        mRecyclerView.setAdapter(new TopicsRecyclerAdapter(topicsArrayList));


        feedRecyclerView = (RecyclerView) findViewById(R.id.feedRecyclerView);
//        feedRecyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        feedRecyclerView.setLayoutManager(manager);
        feedRecyclerView.setItemAnimator(new DefaultItemAnimator());
        feedRecyclerView.setNestedScrollingEnabled(false);
        feedRecyclerView.addItemDecoration(new DividerItemDecoration(feedRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
//        updateList();
        //2017-06-19T14:13:24.100Z
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        mAdapter = new FirebaseRecyclerAdapter<Feed, FeedHolder>(
                Feed.class,
                R.layout.recycler_child_feed,
                FeedHolder.class,
                mRef.child("posts").getRef()) {
            @Override
            protected void populateViewHolder(FeedHolder viewHolder, Feed model, int position) {
                Log.d(TAG, "populateViewHolder:: " + model.getHeading());
                viewHolder.setAuthor(model.getAuthor());
                viewHolder.setImage(model.getImageURL(), DashboardActivity.this);
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
                        Intent i = new Intent(DashboardActivity.this, DetailsFeedActivity.class);
                        i.putExtra(INTENT_EXTRA_FEED, mAdapter.getItem(position));
                        Pair<View, String> pair1 = Pair.create(view.findViewById(R.id.postImageView), "sharedImage");
                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(DashboardActivity.this, pair1);
                        startActivity(i, optionsCompat.toBundle());
                    }
                });
                return viewHolder;
            }


        };

        mAdapter.notifyDataSetChanged();
        feedRecyclerView.setAdapter(mAdapter);

//        "2016-06-18T19:43:03Z"
//        try {
//            long time = sdf.parse("2017-06-18T12:00:00.000Z").getTime();
//            PrettyTime prettyTime = new PrettyTime(Locale.getDefault());
//            Log.d(TAG, "Server Timestamp: +" + String.valueOf(ServerValue.TIMESTAMP));
//            Log.d(TAG, "time: Long" + time);
//            String ago = prettyTime.format(new Date(time));
//            Log.d(TAG, "Time Ago: " + ago);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }


    }


//    private void updateList() {
//
//        mFeedList.add(new Feed("2017-06-18T23:43:03Z", "https://firebasestorage.googleapis.com/v0/b/the-ju-app.appspot.com/o/brooke-lark-250695%20(Small).jpg?alt=media&token=83298d64-0ab4-4eda-bebd-d22fcb3b9cd4", "The JU Journal -", "Jadvpur University ranks 9th", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut consectetur ipsum et tellus sollicitudin, non gravida leo fringilla. Nulla at massa quis erat lacinia egestas. Nam ac enim ante. Ut eu porttitor est, non venenatis nunc.", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut consectetur ipsum et tellus sollicitudin, non gravida leo fringilla. Nulla at massa quis erat lacinia egestas. Nam ac enim ante. Ut eu porttitor est, non venenatis nunc. Nunc vitae diam sed dui ornare efficitur eu at dolor. Ut a molestie enim. Vestibulum laoreet non velit sit amet molestie. Suspendisse sit amet vulputate ligula. Vivamus volutpat quam eu metus placerat posuere. Aenean vel varius arcu."));
//        mFeedList.add(new Feed("2017-06-18T19:43:03Z", "https://firebasestorage.googleapis.com/v0/b/srijan-17-e257c.appspot.com/o/ui_start%2Fmanageeimage.webp?alt=media&token=8ba6a505-e6e3-4cc0-9018-300e81b0ff85", "The JU Journal -", "Jadvpur University ranks 9th", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut consectetur ipsum et tellus sollicitudin, non gravida leo fringilla. Nulla at massa quis erat lacinia egestas. Nam ac enim ante. Ut eu porttitor est, non venenatis nunc.", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut consectetur ipsum et tellus sollicitudin, non gravida leo fringilla. Nulla at massa quis erat lacinia egestas. Nam ac enim ante. Ut eu porttitor est, non venenatis nunc. Nunc vitae diam sed dui ornare efficitur eu at dolor. Ut a molestie enim. Vestibulum laoreet non velit sit amet molestie. Suspendisse sit amet vulputate ligula. Vivamus volutpat quam eu metus placerat posuere. Aenean vel varius arcu."));
//    }

    private void updateData() {
        topicsArrayList = new ArrayList<>();
        topicsArrayList.clear();
        topicsArrayList.add(new Topic(R.drawable.canteen, "Canteen"));
        topicsArrayList.add(new Topic(R.drawable.photography, "Photography"));
        topicsArrayList.add(new Topic(R.drawable.student, "Student Profile"));
        topicsArrayList.add(new Topic(R.drawable.magazine, "e-Magazine"));
        topicsArrayList.add(new Topic(R.drawable.events4, "Events"));
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.noNotifcationImageView:
                cardView.setVisibility(View.GONE);
                mPrefsManager.setNotificationPrefs(false);
                break;

            case R.id.noNotificationsTextView:
                cardView.setVisibility(View.GONE);
                mPrefsManager.setNotificationPrefs(false);
                break;

            case R.id.yesNotifcationImageView:
                cardView.setVisibility(View.GONE);
                mPrefsManager.setNotificationPrefs(true);
                break;

            case R.id.yesNotificationsTextView:
                cardView.setVisibility(View.GONE);
                mPrefsManager.setNotificationPrefs(true);
                break;

            default:
                break;
        }

    }
}
