package com.hash.android.thejuapp.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hash.android.thejuapp.fragment.AboutClubFragmentFragment;
import com.hash.android.thejuapp.fragment.EventsFragmentClubs;
import com.hash.android.thejuapp.fragment.PostsClubFragment;


public class EventsPagerAdapter extends FragmentStatePagerAdapter {


    private final String tag;

    public EventsPagerAdapter(FragmentManager fm, String club) {
        super(fm);
        this.tag = club;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
                Fragment fragment = new AboutClubFragmentFragment();
                Bundle bundle = new Bundle();
                bundle.putString("TAG", tag);
                fragment.setArguments(bundle);
                return fragment;

            case 1:
                Fragment fragment1 = new PostsClubFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString("TAG", tag);
                fragment1.setArguments(bundle1);
                return fragment1;

            case 2:
                Fragment fragment2 = new EventsFragmentClubs();
                Bundle bundle2 = new Bundle();
                bundle2.putString("TAG", tag);
                fragment2.setArguments(bundle2);
                return fragment2;

            default:
                return null;

        }
    }

    /**
     * Return the number of views available.
     */

    @Override
    public int getCount() {
        return 3;
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
                return "ABOUT";
            case 1:
                return "POSTS";
            case 2:
                return "EVENTS";
            default:
                return null;
        }
    }
}
