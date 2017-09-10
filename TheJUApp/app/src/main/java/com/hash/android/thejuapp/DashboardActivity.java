package com.hash.android.thejuapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hash.android.thejuapp.Model.User;
import com.hash.android.thejuapp.Utils.CircleTransform;
import com.hash.android.thejuapp.Utils.ClubUtils;
import com.hash.android.thejuapp.Utils.PreferenceManager;
import com.hash.android.thejuapp.fragment.BookmarksFragment;
import com.hash.android.thejuapp.fragment.DashboardFragment;
import com.hash.android.thejuapp.fragment.InterestedEventsFragment;
import com.hash.android.thejuapp.fragment.ProfileFragment;
import com.hash.android.thejuapp.fragment.SettingsFragment;

import java.util.concurrent.ExecutionException;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "DashboardActivity";

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private boolean doubleBackToExitPressedOnce = false;
    private Fragment fragment;
    private DrawerLayout drawer;
    private PreferenceManager mPrefs;

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

        mPrefs = new PreferenceManager(this);
        String token = FirebaseInstanceId.getInstance().getToken();
        if (token != null) {
            mPrefs.setNotificationKey(token);
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        FirebaseMessaging.getInstance().subscribeToTopic("student");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View navHeader = navigationView.getHeaderView(0);

        TextView txtName = navHeader.findViewById(R.id.name);
        TextView emailTextView = navHeader.findViewById(R.id.website);
        ImageView header = navHeader.findViewById(R.id.img_header_bg);
        ImageView profileImage = navHeader.findViewById(R.id.img_profile);

        User user = mPrefs.getUser();
        txtName.setText(user.getName());
        emailTextView.setText(user.getEmail());

        //Git tes - II
        Glide.with(this)
                .load(R.drawable.nav)
                .crossFade()
                .placeholder(R.color.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(header);
//        header.setImageResource(R.drawable.nav);

        Glide.with(this)
                .load(user.getPhotoURL())
                .crossFade()
                .thumbnail(0.5f)
                .placeholder(R.drawable.defaultdp)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(profileImage);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new ProfileFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_container, fragment)
                        .commit();
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
//            super.onBackPressed();
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
                                mPrefs.setFlowCompleted(false);
                            }
                        });
                break;

            case R.id.nav_dashboard:
                fragment = new DashboardFragment();
                break;

            case R.id.nav_bookmarks:
                fragment = new BookmarksFragment();
                break;

            case R.id.nav_interested_events:
                fragment = new InterestedEventsFragment();
                break;

            case R.id.nav_profile:
                fragment = new ProfileFragment();
                break;

            case R.id.nav_privacy:
                String url = "http://jux.jujournal.com/privacy_policy_juX.pdf";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                break;

            case R.id.nav_settings:
                fragment = new SettingsFragment();
                break;

            case R.id.nav_ecell:
                Intent i = new Intent(this, ClubActivity.class);
                i.putExtra("club", ClubUtils.ECELL);
                startActivity(i);
                DrawerLayout drawer0 = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer0.closeDrawer(GravityCompat.START);
                return true;

            case R.id.nav_journal:
                Intent i1 = new Intent(this, ClubActivity.class);
                i1.putExtra("club", ClubUtils.JOURNAL);
                startActivity(i1);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;

            case R.id.nav_science:
                Intent i2 = new Intent(this, ClubActivity.class);
                i2.putExtra("club", ClubUtils.SC);
                startActivity(i2);
                DrawerLayout drawer1 = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer1.closeDrawer(GravityCompat.START);
                return true;

            case R.id.nav_ds:
                Intent i3 = new Intent(this, ClubActivity.class);
                i3.putExtra("club", ClubUtils.DS);
                startActivity(i3);
                ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
                return true;

            case R.id.nav_pnc:
                Intent i4 = new Intent(this, ClubActivity.class);
                i4.putExtra("club", ClubUtils.PNC);
                startActivity(i4);
                ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
                return true;

            case R.id.nav_mc:
                Intent i5 = new Intent(this, ClubActivity.class);
                i5.putExtra("club", ClubUtils.MC);
                startActivity(i5);
                ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
                return true;

            case R.id.nav_jupc:
                Intent i6 = new Intent(this, ClubActivity.class);
                i6.putExtra("club", ClubUtils.PC);
                startActivity(i6);
                ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
                return true;

            case R.id.nav_qc:
                Intent i7 = new Intent(this, ClubActivity.class);
                i7.putExtra("club", ClubUtils.QC);
                startActivity(i7);
                ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
                return true;

            case R.id.nav_mhc:
                Intent i8 = new Intent(this, ClubActivity.class);
                i8.putExtra("club", ClubUtils.MHC);
                startActivity(i8);
                ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
                return true;

            case R.id.nav_dc:
                Intent i9 = new Intent(this, ClubActivity.class);
                i9.putExtra("club", ClubUtils.DC);
                startActivity(i9);
                ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
                return true;

            case R.id.nav_df:
                Intent i10 = new Intent(this, ClubActivity.class);
                i10.putExtra("club", ClubUtils.DF);
                startActivity(i10);
                ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
                return true;

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


        final GestureDetector mGestureDetector;
        private final OnItemClickListener mListener;

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
