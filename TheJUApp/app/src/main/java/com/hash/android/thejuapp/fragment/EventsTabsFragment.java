package com.hash.android.thejuapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hash.android.thejuapp.R;
import com.hash.android.thejuapp.adapter.TabsPagerAdapter;

/**
 * Created by Spandita Ghosh on 7/28/2017.
 */

public class EventsTabsFragment extends android.support.v4.app.Fragment {
    public TabLayout tabLayout;
    public ViewPager viewPager;
    public int int_items = 3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View x = inflater.inflate(R.layout.tabs_event_layout, null);
//        getActivity().setTitle("Events");
        tabLayout = x.findViewById(R.id.tabLayout);
        viewPager = x.findViewById(R.id.vpPager);

        /*
         Set an Adapter for the View Pager
         */
        viewPager.setAdapter(new TabsPagerAdapter(getChildFragmentManager()));

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return x;

    }
}
