package com.hash.android.thejuapp.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.DefaultItemAnimator;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.gson.Gson;
import com.hash.android.thejuapp.ExploreActivity;
import com.hash.android.thejuapp.Model.User;
import com.hash.android.thejuapp.R;
import com.hash.android.thejuapp.adapter.StudentProfileRecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private Index index;
    private TextView queryTextView, noUsersFoundTV;


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
            public boolean onQueryTextSubmit(final String query) {
//                if (mAdapter.getItemCount() == 0) {
//                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(getActivity(), "Oops! No user matching " + query + " found.", Toast.LENGTH_SHORT).show();
//                }
                mArrayList.clear();
                progressBar.setVisibility(View.VISIBLE);
                index.searchAsync(new com.algolia.search.saas.Query(query), new CompletionHandler() {
                    @Override
                    public void requestCompleted(JSONObject jsonObject, AlgoliaException e) {
                        try {
//                            Log.d(TAG, "requestCompleted: " + jsonObject.toString(2));
                            String nbHits = jsonObject.getString("nbHits");
                            String ms = jsonObject.getString("processingTimeMS");
                            if (Integer.valueOf(nbHits) == 0) {
                                queryTextView.setVisibility(View.GONE);
                                noUsersFoundTV.setText("No users matching \"" + query + "\" found.");
                                noUsersFoundTV.setVisibility(View.VISIBLE);
                            }
                            else {
                                queryTextView.setVisibility(View.VISIBLE);
                                noUsersFoundTV.setVisibility(View.GONE);
                            }
                            queryTextView.setText("Showing " + nbHits + " results (" + ms + "ms)");
                            progressBar.setVisibility(View.GONE);
                            JSONArray jsonArray = jsonObject.getJSONArray("hits");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Gson gson = new Gson();
                                User user = gson.fromJson(String.valueOf(((JSONObject) jsonArray.get(i)).get("user")), User.class);
                                Log.d(TAG, "requestCompleted: " + user.getName());
                                mArrayList.add(user);
                                mAdapter.notifyDataSetChanged();

                            }
//                    Log.d(TAG, "requestCompleted: " + jsonObject.toString(2));
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {


//                if (!TextUtils.isEmpty(newText)) {
//                    progressBar.setVisibility(View.VISIBLE);
//
//                    final String queryString = String.valueOf(newText).toLowerCase();
//
//                    query.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
//                                try {
//                                    User user = snapShot.getValue(User.class);
//                                    if (user.getName().toLowerCase().startsWith(queryString)) {
//                                        mArrayList.add(user);
//                                        progressBar.setVisibility(View.GONE);
//                                    }
//                                    mAdapter.notifyDataSetChanged();
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                            if (mAdapter.getItemCount() == 0)
//                                progressBar.setVisibility(View.VISIBLE);
//                            else progressBar.setVisibility(View.GONE);
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//
//                    });
//
//                } else {
//                    mArrayList.clear();
//                    mAdapter.notifyDataSetChanged();
//                }
                return true;
            }
        });
//        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem menuItem) {
//                progressBar.setVisibility(View.VISIBLE);
//                return true;
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
//
//                searchView.setIconified(true);
//                mArrayList.clear();
//                mAdapter.notifyDataSetChanged();
//                return true;
//            }
//        });

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

        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/opensans.ttf");
        queryTextView = rootView.findViewById(R.id.response);
        noUsersFoundTV = rootView.findViewById(R.id.noUsersFoundTextView);
        noUsersFoundTV.setTypeface(custom_font);
        queryTextView.setTypeface(custom_font);

        Client client = new Client("TXBD9WWLH0", "933b378c91e407426dd6bee014eff479");
        index = client.getIndex("users");
        index.enableSearchCache();

        RecyclerView mRecyclerView = rootView.findViewById(R.id.studentRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

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
