package com.hash.android.thejuapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.hash.android.thejuapp.fragment.CanteenListFragment;
import com.hash.android.thejuapp.fragment.EventsTabsFragment;
import com.hash.android.thejuapp.fragment.LeaderBoardFragment;
import com.hash.android.thejuapp.fragment.MagazineFragment;
import com.hash.android.thejuapp.fragment.ProfileFragment;
import com.hash.android.thejuapp.fragment.StudentProfileFragment;

public class ExploreActivity extends AppCompatActivity {

    public static final String EXTRA_CLASS_NAME = "className";
    public static final String CANTEEN_FRAGMENT = "canteen";
    public static final String MAGAZINE_FRAGMENT = "magazine";
    public static final String STUDENT_FRAGMENT = "student";
    public static final String LEADERBOARD_FRAGMENT = "leaderboard";
    public static final String EVENTS_FRAGMENT = "events";
    public static final String PROFILE = "profile";


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent i = getIntent();

        String fragmentName = i.getStringExtra(EXTRA_CLASS_NAME);

        if (fragmentName == null || fragmentName.length() < 1) {
            finish();
            startActivity(new Intent(this, DashboardActivity.class));
        }

        Fragment fragment = null;
        switch (fragmentName) {
            case CANTEEN_FRAGMENT:
                fragment = new CanteenListFragment();
                break;

            case MAGAZINE_FRAGMENT:
                fragment = new MagazineFragment();
                break;

            case STUDENT_FRAGMENT:
                fragment = new StudentProfileFragment();
                break;

            case LEADERBOARD_FRAGMENT:
                fragment = new LeaderBoardFragment();
                break;

            case EVENTS_FRAGMENT:
                fragment = new EventsTabsFragment();
                break;

            case PROFILE:
                fragment = new ProfileFragment();
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment).commit();
        } else {
            finish();
            startActivity(new Intent(this, DashboardActivity.class));
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


}
