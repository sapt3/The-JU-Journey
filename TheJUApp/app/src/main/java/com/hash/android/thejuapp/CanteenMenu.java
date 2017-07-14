package com.hash.android.thejuapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.hash.android.thejuapp.adapter.CanteenMenuRecyclerAdapter;

import java.util.ArrayList;

import static com.hash.android.thejuapp.adapter.CanteenListRecyclerAdapter.INTENT_KEY;
import static com.hash.android.thejuapp.fragment.CanteenListFragment.KEY_SURUCHI;

public class CanteenMenu extends AppCompatActivity {

    private static final String TAG = CanteenMenu.class.getSimpleName();
    private ArrayList<com.hash.android.thejuapp.Model.MenuItem> mArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String key = getIntent().getStringExtra(INTENT_KEY);
        updateUI(key);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.menuRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new CanteenMenuRecyclerAdapter(mArrayList));
    }

    private void updateUI(String key) {
        switch (key) {
            case KEY_SURUCHI:
                mArrayList.clear();
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chappati", "5"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Rice", "20"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Roll", "18"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Fried Rice", "35"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Momo", "20"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chowmein", "20"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Chop", "15"));
                break;

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}
