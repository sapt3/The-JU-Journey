package com.hash.android.thejuapp.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hash.android.thejuapp.Model.Club;
import com.hash.android.thejuapp.fragment.AboutClubFragmentFragment;
import com.hash.android.thejuapp.fragment.PostsClubFragment;


public class EventsPagerAdapter extends FragmentStatePagerAdapter {


    private Club club;

    public EventsPagerAdapter(FragmentManager fm, Club club) {
        super(fm);
        this.club = club;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
                Fragment fragment = new AboutClubFragmentFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("CLUB", club);
                fragment.setArguments(bundle);
                return fragment;

            case 1:
                Fragment fragment1 = new PostsClubFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putParcelable("CLUB", club);
                fragment1.setArguments(bundle1);
                return fragment1;

            case 2:
                Fragment fragment2 = new AboutClubFragmentFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putParcelable("CLUB", club);
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
