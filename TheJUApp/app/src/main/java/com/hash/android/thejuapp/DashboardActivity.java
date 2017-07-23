package com.hash.android.thejuapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hash.android.thejuapp.HelperClass.CircleTransform;
import com.hash.android.thejuapp.HelperClass.PreferenceManager;
import com.hash.android.thejuapp.Model.User;
import com.hash.android.thejuapp.fragment.BookmarksFragment;
import com.hash.android.thejuapp.fragment.CanteenListFragment;
import com.hash.android.thejuapp.fragment.DashboardFragment;
import com.hash.android.thejuapp.fragment.MagazineFragment;
import com.hash.android.thejuapp.fragment.ProfileFragment;

import java.util.concurrent.ExecutionException;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private static final String TAG = "DashboardActivity";
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, new DashboardFragment())
                    .commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String token = FirebaseInstanceId.getInstance().getToken();
        if (token != null) {
            new PreferenceManager(this).setNotificationKey(token);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View navHeader = navigationView.getHeaderView(0);

        TextView txtName = navHeader.findViewById(R.id.name);
        TextView emailTextView = navHeader.findViewById(R.id.website);
        ImageView header = navHeader.findViewById(R.id.img_header_bg);
        ImageView profileImage = navHeader.findViewById(R.id.img_profile);

        User user = new PreferenceManager(this).getUser();
        txtName.setText(user.getName());
        emailTextView.setText(user.getEmail());

        header.setImageResource(R.drawable.navheader);
//        Log.d(TAG, user.getCoverURL());
//        Glide.with(this)
//                .load(user.getCoverURL())
//                .crossFade()
//                .placeholder(R.drawable.navheader)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(header);


        Glide.with(this)
                .load(user.getPhotoURL())
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(profileImage);


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


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        fragment = null;

        switch (id) {
            case R.id.nav_logout:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // user is now signed out
                                startActivity(new Intent(DashboardActivity.this, FacebookLogin.class));
                                finish();
                                new PreferenceManager(DashboardActivity.this).setFlowCompleted(false);
                            }
                        });
                break;

            case R.id.nav_dashboard:
                fragment = new DashboardFragment();
                break;

            case R.id.nav_bookmarks:
                fragment = new BookmarksFragment();
                break;
            case R.id.nav_canteen:
                fragment = new CanteenListFragment();
                break;

            case R.id.nav_profile:
                fragment = new ProfileFragment();
                break;


            case R.id.nav_eMagazine:
                fragment = new MagazineFragment();


        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {


        GestureDetector mGestureDetector;
        private OnItemClickListener mListener;

        public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    try {
                        View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                        if (child != null && mListener != null) {
                            mListener.onLongItemClick(child, recyclerView.getChildAdapterPosition(child));
                        }
                    } catch (Exception ec) {
                        ec.printStackTrace();
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                try {
                    mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
                } catch (ExecutionException | InterruptedException e1) {
                    e1.printStackTrace();
                }
                return true;
            }
            return false;
        }


        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }

        public interface OnItemClickListener {
            void onItemClick(View view, int position) throws ExecutionException, InterruptedException;

            void onLongItemClick(View view, int position);
        }

    }


}
