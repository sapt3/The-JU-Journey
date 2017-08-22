package com.hash.android.thejuapp.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hash.android.thejuapp.Model.Magazine;
import com.hash.android.thejuapp.R;
import com.hash.android.thejuapp.adapter.MagazineRecyclerAdapter;

import java.util.ArrayList;

public class MagazineFragment extends Fragment {

    private static final String URL_4th_EDITION = "https://docs.google.com/gview?embedded=true&url=jux.jujournal.com/newsletter/4th_edition.pdf";
    private static final String URL_3rd_EDITION = "https://docs.google.com/gview?embedded=true&url=jux.jujournal.com/newsletter/3rd_edition.pdf";
    private static final String URL_2nd_EDITION = "https://docs.google.com/gview?embedded=true&url=http://jux.jujournal.com/newsletter/2nd_edition.pdf";

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private ArrayList<Magazine> mArrayList = new ArrayList<>();

    public MagazineFragment() {
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     *
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.magazine_list, container, false);
        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/opensans.ttf");
        RecyclerView mRecyclerView = view.findViewById(R.id.magazineRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        updateData();
        MagazineRecyclerAdapter mAdapter = new MagazineRecyclerAdapter(mArrayList, getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    private void updateData() {

        mArrayList.clear();
//        mArrayList.add(new Magazine("Edition 4.0", R.drawable.magazine2, "July 2017", URL_4th_EDITION));
        mArrayList.add(new Magazine("Edition 3.0", R.drawable.edition3, "February 2017", URL_3rd_EDITION));
        mArrayList.add(new Magazine("Edition 2.0", R.drawable.edition1, "October 2016", URL_2nd_EDITION));

    }

    /**
     * Called when the fragment's activity has been created and this
     * fragment's view hierarchy instantiated.  It can be used to do final
     * initialization once these pieces are in place, such as retrieving
     * views or restoring state.  It is also useful for fragments that use
     * {@link #setRetainInstance(boolean)} to retain their instance,
     * as this callback tells the fragment when it is fully associated with
     * the new activity instance.  This is called after {@link #onCreateView}
     * and before {@link #onViewStateRestored(Bundle)}.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Newsletter");
    }
}
