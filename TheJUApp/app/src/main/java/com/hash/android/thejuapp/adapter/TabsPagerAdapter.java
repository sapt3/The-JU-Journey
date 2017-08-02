package com.hash.android.thejuapp.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hash.android.thejuapp.fragment.EventsFragment;


public class TabsPagerAdapter extends FragmentStatePagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Fragment fragment = new EventsFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("KEY", 0);
                fragment.setArguments(bundle);
                return fragment;
            case 1:
                Fragment fragment1 = new EventsFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putInt("KEY", 1);
                fragment1.setArguments(bundle1);
                return fragment1;
            default:
                return null;
        }
    }

    /**
     * Return the number of views available.
     */

    @Override
    public int getCount() {
        return 2;
    }

    /**
     * This method may be called by the ViewPager to obtain a title string
     * to describe the specified page. This method may return null
     * indicating no title for this page. The default implementation returns
     * null.
     *
     * @param position The position of the title requested
     * @return A title for the requested page
     */
    @Override
    public CharSequence getPageTitle(int position) {
//        return super.getPageTitle(position);
        switch (position) {
            case 0:
                return "Upcoming";
            case 1:
                return "Past Events";
            default:
                return null;
        }
    }
}
