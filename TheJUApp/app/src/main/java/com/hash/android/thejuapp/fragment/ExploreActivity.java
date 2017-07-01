package com.hash.android.thejuapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.hash.android.thejuapp.DashboardActivity;
import com.hash.android.thejuapp.R;

public class ExploreActivity extends AppCompatActivity {

    public static final String EXTRA_CLASS_NAME = "className";
    public static final String CANTEEN_FRAGMENT = "canteen";

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
