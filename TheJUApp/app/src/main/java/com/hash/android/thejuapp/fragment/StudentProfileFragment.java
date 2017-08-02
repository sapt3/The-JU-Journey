package com.hash.android.thejuapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hash.android.thejuapp.ExploreActivity;
import com.hash.android.thejuapp.Model.User;
import com.hash.android.thejuapp.R;
import com.hash.android.thejuapp.ViewHolder.UserViewHolder;

import java.util.ArrayList;

public class StudentProfileFragment extends Fragment {
    private static final String TAG = StudentProfileFragment.class.getSimpleName();
    Query query;
    private ArrayList<User> mArrayList = new ArrayList<>();
    private FirebaseRecyclerAdapter<User, UserViewHolder> mAdapter;

    public StudentProfileFragment() {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_canteen, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = new SearchView(((ExploreActivity) getActivity()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    String first = newText.substring(0, 1);
                    String second = first.toUpperCase();
                    String formatted = second + newText.substring(1);
                    Log.d(TAG, "text:: " + formatted);

                    query = null;
                    query = FirebaseDatabase.getInstance().getReference("users").orderByChild("name").startAt(formatted);
                    mAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

        mAdapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(
                User.class,
                R.layout.recycler_student_item,
                UserViewHolder.class,
                query
        ) {
            /**
             * Each time the data at the given Firebase location changes,
             * this method will be called for each item that needs to be displayed.
             * The first two arguments correspond to the mLayout and mModelClass given to the constructor of
             * this class. The third argument is the item's position in the list.
             * <p>
             * Your implementation should populate the view using the data contained in the model.
             *
             * @param viewHolder The view to populate
             * @param model      The object containing the data used to populate the view
             * @param position   The position in the list of the view being populated
             */
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, User model, int position) {
                viewHolder.bind(model);
            }
        };

        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        mRef.keepSynced(true);

//        searchET.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                mArrayList.clear();
//                final String queryString = String.valueOf(charSequence);
//                query.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
//                            try {
//                                User user = snapShot.getValue(User.class);
//                                assert user != null;
//                                if (user.getName().startsWith(queryString)) {
//                                    mArrayList.add(user);
//
//                                }
//                                mAdapter.notifyDataSetChanged();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//                searchET.setOnKeyListener(new View.OnKeyListener()
//
//                {
//                    @Override
//                    public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                        if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
//                                (i == KeyEvent.KEYCODE_ENTER)) {
//                            View view1 = getActivity().getCurrentFocus();
//                            if (view1 != null) {
//                                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                                imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
//                            }
//                            if (mArrayList.size() == 0) {
//                                Toast.makeText(getActivity(), "No user matching the given name.", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                        return false;
//                    }
//                });
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });

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
