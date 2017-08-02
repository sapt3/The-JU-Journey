package com.hash.android.thejuapp;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.hash.android.thejuapp.Model.Canteen;
import com.hash.android.thejuapp.adapter.CanteenMenuRecyclerAdapter;

import java.util.ArrayList;

import static com.hash.android.thejuapp.adapter.CanteenListRecyclerAdapter.INTENT_KEY;
import static com.hash.android.thejuapp.fragment.CanteenListFragment.KEY_AC;
import static com.hash.android.thejuapp.fragment.CanteenListFragment.KEY_CET;
import static com.hash.android.thejuapp.fragment.CanteenListFragment.KEY_CLUB;
import static com.hash.android.thejuapp.fragment.CanteenListFragment.KEY_GUEST;
import static com.hash.android.thejuapp.fragment.CanteenListFragment.KEY_MONI;
import static com.hash.android.thejuapp.fragment.CanteenListFragment.KEY_POST;
import static com.hash.android.thejuapp.fragment.CanteenListFragment.KEY_STAFF;
import static com.hash.android.thejuapp.fragment.CanteenListFragment.KEY_STUDENT;
import static com.hash.android.thejuapp.fragment.CanteenListFragment.KEY_SURUCHI;

public class CanteenMenu extends AppCompatActivity {

    private static final String TAG = CanteenMenu.class.getSimpleName();
    private ArrayList<com.hash.android.thejuapp.Model.MenuItem> mArrayList = new ArrayList<>();
    private String key;
    private CanteenMenuRecyclerAdapter adapter;
    private ArrayList<com.hash.android.thejuapp.Model.MenuItem> newList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Menu");
        setContentView(R.layout.activity_canteen_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ConstraintLayout tempLayout = (ConstraintLayout) findViewById(R.id.temp);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.menuRecyclerView);

        mRecyclerView.setFocusable(false);
        tempLayout.requestFocus();

        Canteen canteen = getIntent().getParcelableExtra(INTENT_KEY);
        key = canteen.getKey();
        updateUI(key);

        TextView timeTV = (TextView) findViewById(R.id.timeTextView);
        timeTV.setText("TIMINGS: " + canteen.shortTimings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setNestedScrollingEnabled(false);
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(CanteenMenu.this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CanteenMenuRecyclerAdapter(mArrayList);
        mRecyclerView.setAdapter(adapter);
    }

    private void updateUI(String key) {
        mArrayList.clear();
        switch (key) {
            case KEY_SURUCHI:
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Fried Rice", "30"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Fried Rice with Paneer", "70"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Fried Rice with Egg Curry", "38"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Fried Rice with Chicken Curry", "70"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Khichuri", "30"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Khichuri with Potato Fry", "35"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Khichuri with Beguni", "35"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Khichuri with Omlet", "20"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Rumali Roti", "4"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Paratha", "8"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Lachcha Paratha", "12"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Tomato Puri", "8"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Veg Chowmein", "25"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg Chowmein", "30"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Chowmein", "55"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Mixed Chowmein", "65"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Ghugni", "8"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Alur Dam", "10"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Cholar Dal", "10"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chana Masala", "15"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Soyabean Curry", "10"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chilli Chicken", "40"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Paneer Masala", "40"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Veg Patties", "18"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Plain Dosa", "30"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Masala Dosa", "35"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Dahi Vada", "35"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Tea", "3"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Coffee", "10"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Sweet", "5"));
                break;

            case KEY_CET:
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Rice with Veg", "25"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Rice with Egg", "30"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Rice with Fish", "40"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Rice with meat", "50"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Extra Rice", "5"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Singara", "5"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Veg chop", "5"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg chop", "8"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Chop", "10"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Lollipop", "10"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Pakoda", "10"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Fish Chop", "10"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Bread Chicken Pakoda", "10"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Fish Fry", "30"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Veg Roll", "17"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg Roll", "25"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg Veg Roll", "25"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Double Egg Roll", "25"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Roll", "35"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg Chicken Roll", "40"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Veg Chow", "20"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg Chow", "30"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg Chicken Chow", "40"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Chow", "35"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Moghlai Parotha", "50"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chana Batora", "25"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Plain Parotha", "5"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Aludum", "10"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Ghugai", "10"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Plain Tarka", "12"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chilly Potato", "12"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chilli Chicken(2 pcs)", "30"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Stuff Aloodum(1pcs)", "15"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Veg Pulao", "30"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Freid Rice", "30"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Cake", "10/5"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Biscuit Packet", "10/5"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Laddu", "3"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Goja", "6"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Cold Drinks(200 ml)", "12"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Mineral Water(1L)", "20"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Tea", "5/4"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Coffee", "10"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Aampooa sharbat", "10"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Nan Parotha(1 PC)", "5"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Fried Chicken", "20"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Gravy Chop", "15"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Fish Kachuri", "25"));
                break;

            case KEY_AC:

                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("3 Pcs. Bread, 1 Pc Banana, 1 Boiled Egg\n(Breakfast)", "15"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Tea\n(Breakfast)", "4"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Veg Curry, Dal, Bhaji with Rice\n(Lunch)", "20"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg Curry (1 Pc.), Dal, Bhaji with Rice\n(Lunch", "25)"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Fish Curry (1 Pc.), Dal, Bhaji with Rice\n(Lunch)", "35"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Curry (2 Pcs.), Dal, Bhaji with Rice\n(Lunch)", "40"));
                break;

            case KEY_GUEST:

                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Veg Chowmein", "30"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chickent Chowmein", "50"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg Chowmein", "40"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Sandwich", "40"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Paratha", "10"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chilli Chicken", "60/70/80"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Veg Sandwich", "25"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Veg Thali", "40"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Thali", "70"));
                break;

            case KEY_POST:


                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Tea\n(Breakfast)", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg Toast\n(Breakfast)", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Bread Butter\n(Breakfast)", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Rice Meal (Veg) \n(Lunch)", "25"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Rice Meal (Egg) \n(Lunch)", "30"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Rice Meal (Fish) \n(Lunch)", "40"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Rice Meal (Chicken) \n(Lunch)", "50"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Roti", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Vegetable Curry", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg Curry", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Curry", ""));

            case KEY_STAFF:

                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Lemon Tea ", "6"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Elaichi Tea", "6"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Cold Tea", "22"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Hot Coffee", "10"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Cold Coffee", "25"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Jelly Toast", "14"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Butter Toast", "18"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Patties", "28"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Sandwich", "28"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Fish Fry", "38"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Fish Finger", "10"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Ghoogni", "18"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Aloo Dum", "10"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Cholar Dal", "15"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chana", "19"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Tarka", "18"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chilli Chicken", "35"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Curry", "48"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Paneer Curry", "35"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Palak Paneer", "35"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Butter Paneer", "35"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Luchi", "28"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Peas Kaachuri", "28"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Paratha", "28"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Roti", "28"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Fried Rice", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Biriyani", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg Roll", "20"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Roll", "35"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg Chicken Roll", "40"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Paneer Roll", "35"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg Chow", "30"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Veg Chow", "20"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg Chicken Chow", "40"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Plain Dhosa", "25"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Masala Dhosa", "35"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Onion Dhosa", "40"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Paper Masala Dhosa", "45"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Idli(2pcs)", "18"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Sambar Bada", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Dohi Bada(2pcs)", "18"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Plain Uttapam", "38"));
                break;

            case KEY_CLUB:

                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Tea", "5/6/7"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Coffee", "8"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Roti", "3"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Luchi", "4"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Plain Parotha", "6"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Kachuri", "5"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Aloo Dum/Ghugni", "10"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Cholar Dal", "15"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Seasonal Sabzi", "15"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Plain Toast", "8"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Butter Toast", "15"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Omlette", "10"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Double Egg Omelette", "18"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg Poach", "10"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Double Egg Poach", "18"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg Toast", "20"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Veg Sandwich", "15"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg Sandwich", "20"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Sandwich", "30"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Veg Pakora", "8"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Pakora", "15"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Paneer Pakora", "20"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Veg Roll", "15"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg Roll", "20"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Double Egg Roll", "30"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg Chicken Roll", "40"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Double Egg Chicken Roll", "45"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Plain Maggi", "20"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg Maggi", "25"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Special Maggi", "35"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Veg Chowmein", "20"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg Chowmein", "30"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg Chicken Chowmein", "30"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Fried Rice", "35"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg Fried Rice", "45"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg Chicken Fried Rice", "55"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Pulao", "45"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg Chicken Fried Rice", "55"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Pulao", "45"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Special Pulao", "60"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chilli Chicken", "30"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chilli Fish", "45"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chilli Paneer", "50"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Tadka", "20"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg Tadka", "30"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Veg Thali", "30"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg Thali", "35"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Thali", "50"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Mutton Thali", "90"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Extra Rice", "5"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg Curry", "15"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Fish Curry", "25"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Curry", "30"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Khichuri with Veg Fry", "35"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Keem Khichuri", "60"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Moglai Paratha", "40"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Kasa", "60"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Mutton Kasa", "70"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Dai Chicken", "10"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Plain Curd", "15"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Sweet Curd", "15"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Mango Curd", "15"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Gulab Jamun", "10"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Cold Drinks", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Ice Cream", ""));
                break;

            case KEY_MONI:

                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Veg Soup", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Mushroom Soup", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Clear Soup", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Sweet Corn  Soup ", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Noodles Soup ", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Hot & Sour Soup ", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Mix Noodles Soup ", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Thai Soup", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Steam Momo", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Fried Momo ", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Pan-Fried Momo", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Crispy Chilly Babycorn", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Veg Roll", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Paratha & Aloo Dum", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Veggy Pasta(Schezwan/Red/White/Green)", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Mushrrom Pasta(Schezwan/Red/White/Green)", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Veg Wrap", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Spring Chicken", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Spring Rolls", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg Pasta(Schezwan/Red/White/Green)", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Pasta (Schezwan/Red/White/Green)", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken & Mushroom Pasta (Schezwan/Red/White/Green)", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Fish Fry", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Fish Finger", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Wrap", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Fried Rice(Veg, Chicken, Mix)(Seg/Thai/Chilly-Garlic/Garlic)", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Noodles(Veg, Egg, Chicken, Mix) (Seg/Thai/Chilly-Garlic/Garlic)(Hakka/Gravy)", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("American Chopsuey", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chilly  Chicken/Mushroom/Babycorn", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Manchurian Chicken/Mushroom/Babycorn ", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Thai Chicken/Mushroom/Babycorn", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Lemon Chicken/Mushroom/Babycorn ", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Ginger Chicken/Mushroom/Babycorn ", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Garlic Chicken/Mushroom/Babycorn ", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Schezwan Chicken/Mushroom/Babycorn ", ""));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chilly Chicken", ""));
                break;


            case KEY_STUDENT:

                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Roti", "2.5"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Luchi", "2.5"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Dalpuri", "4"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Parota", "5"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Tomato Puri", "4"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Radha Bhallavi", "4"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Cholar Dal", "6"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Ghoogni", "5"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Aloo Dum", "8"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Kashmiri Aloo Dum", "12"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg Curry", "10"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chana Masala", "10"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Aloo Soyabean Curry", "10"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Aloo Paneer Masala", "18"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Omelette Curry", "12"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Curry", "22"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chilli Chicken (2 Pcs.)", "18"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Mix Veg Curry", "12"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Simui", "7"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Govinda Veg Kheer", "8"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg Tarka", "14"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Rajma", "10"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Bread Pudding", "6"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Mughlai", "30"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Mughlai", "35"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg Chop", "6"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Chop", "7"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Veg Chop", "6"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Fish Chop", "6"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Paneer Chop", "7"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Veg Pakora", "5"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Pakora", "15"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Cutlet", "25"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Fish Cutlet", "25"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Fry", "15"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Lollipop", "15"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken 65", "15"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Samosa", "5"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Breadchop", "8"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Nimki", "4"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Aloo Paratha", "12"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Tea", "5"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Coffee", "6"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Milk", "10"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Bread", "4"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Butter Toast", "6"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Jelly Toast", "6"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg Toast", "15"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg Boil", "6"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Omelette", "8"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Poach", "8"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg Roll", "18"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Veg Roll", "15"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Roll", "25"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Mix Roll", "30"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg Chowmein", "20"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Veg Chowmein", "15"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Chowmein", "25"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Mix Chowmein", "30"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Veg Meal", "28"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Fish Meal", "38"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Chicken Meal", "42"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Egg Meal", "30"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Mutton Meal", "70"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Bhuji Halwa", "6"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Custard", "10"));
                mArrayList.add(new com.hash.android.thejuapp.Model.MenuItem("Banana", "5"));

            default:

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_canteen, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView sv = new SearchView((getSupportActionBar().getThemedContext()));
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, sv);
        newList = new ArrayList<>();
        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.action_search), new MenuItemCompat.OnActionExpandListener() {
            /**
             * Called when a menu item with
             * is expanded.
             *
             * @param item Item that was expanded
             * @return true if the item should expand, false if expansion should be suppressed.
             */
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            /**
             * Called when a menu item with
             * is collapsed.
             *
             * @param item Item that was collapsed
             * @return true if the item should collapse, false if collapsing should be suppressed.
             */
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                updateUI(key);
                adapter.setFilter(mArrayList);
                return true;
            }
        });

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newList.clear();
                String text = newText.toLowerCase();
                updateUI(key);
                for (com.hash.android.thejuapp.Model.MenuItem menuItem : mArrayList) {
                    String name = menuItem.itemName.toLowerCase();
                    if (name.contains(text)) {
                        //If the food item matches
                        newList.add(menuItem);
                    }
                }
                adapter.setFilter(newList);
                return true;
            }
        });

        return true;
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
