package com.hash.android.thejuapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hash.android.thejuapp.ExploreActivity;
import com.hash.android.thejuapp.Model.User;
import com.hash.android.thejuapp.R;
import com.hash.android.thejuapp.adapter.StudentProfileRecyclerAdapter;

import java.util.ArrayList;

public class StudentProfileFragment extends Fragment {

    private static final String TAG = StudentProfileFragment.class.getSimpleName();

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private final Query query = FirebaseDatabase.getInstance().getReference("users").orderByChild("name");
    private final ArrayList<User> mArrayList = new ArrayList<>();
    private ProgressBar progressBar;
    private StudentProfileRecyclerAdapter mAdapter;


    public StudentProfileFragment() {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_canteen, menu);
        final MenuItem item = menu.findItem(R.id.action_search);

        final SearchView searchView = new SearchView(((ExploreActivity) getActivity()).getSupportActionBar().getThemedContext());
        searchView.setQueryHint("Search for anyone...");
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        MenuItemCompat.setActionView(item, searchView);
        MenuItemCompat.expandActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (mAdapter.getItemCount() == 0) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Oops! No user matching " + query + " found.", Toast.LENGTH_SHORT).show();
                }
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {

                mArrayList.clear();
                if (!TextUtils.isEmpty(newText)) {
                    progressBar.setVisibility(View.VISIBLE);

                    final String queryString = String.valueOf(newText).toLowerCase();

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                                try {
                                    User user = snapShot.getValue(User.class);
                                    if (user.getName().toLowerCase().startsWith(queryString)) {
                                        mArrayList.add(user);
                                        progressBar.setVisibility(View.GONE);
                                    }
                                    mAdapter.notifyDataSetChanged();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            if (mAdapter.getItemCount() == 0)
                                progressBar.setVisibility(View.VISIBLE);
                            else progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });

                } else {
                    mArrayList.clear();
                    mAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });
        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                progressBar.setVisibility(View.VISIBLE);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {

                searchView.setIconified(true);
                mArrayList.clear();
                mAdapter.notifyDataSetChanged();
                return true;
            }
        });

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
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.layout_student_profile, container, false);

        RecyclerView mRecyclerView = rootView.findViewById(R.id.studentRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        mAdapter = new StudentProfileRecyclerAdapter(mArrayList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        mRef.keepSynced(true);
        progressBar = rootView.findViewById(R.id.progressBar3);
        return rootView;

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
        getActivity().setTitle("Student Profile");
        this.setHasOptionsMenu(true);
    }


}
