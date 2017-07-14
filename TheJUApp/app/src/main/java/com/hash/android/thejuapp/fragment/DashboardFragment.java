package com.hash.android.thejuapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hash.android.thejuapp.DashboardActivity;
import com.hash.android.thejuapp.DetailsFeedActivity;
import com.hash.android.thejuapp.ExploreActivity;
import com.hash.android.thejuapp.HelperClass.PreferenceManager;
import com.hash.android.thejuapp.Model.Feed;
import com.hash.android.thejuapp.Model.Topic;
import com.hash.android.thejuapp.Model.User;
import com.hash.android.thejuapp.R;
import com.hash.android.thejuapp.ViewHolder.FeedHolder;
import com.hash.android.thejuapp.adapter.TopicsRecyclerAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import static com.hash.android.thejuapp.ExploreActivity.CANTEEN_FRAGMENT;
import static com.hash.android.thejuapp.ExploreActivity.EXTRA_CLASS_NAME;
import static com.hash.android.thejuapp.ExploreActivity.MAGAZINE_FRAGMENT;
import static com.hash.android.thejuapp.ExploreActivity.STUDENT_FRAGMENT;
import static com.hash.android.thejuapp.adapter.FeedRecyclerAdapter.INTENT_EXTRA_FEED;


public class DashboardFragment extends Fragment {

    private static final int TAG_CANTEEN = 1;
    private static final int TAG_PHOTO = 2;
    private static final int TAG_STUDENT = 3;
    private static final int TAG_MAGAZINE = 4;
    private static final int TAG_EVENTS = 5;
    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";
    private final String TAG = DashboardActivity.class.getSimpleName();
    private final String URL_NAV_BACKGROUND = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";
    public FirebaseRecyclerAdapter<Feed, FeedHolder> mAdapter;
    ArrayList<Feed> mFeedList = new ArrayList<>();
    PreferenceManager mPrefsManager;
    private ArrayList<Topic> topicsArrayList = new ArrayList<>();
    private CardView cardView;

    public DashboardFragment() {
    }

    /**
     * Called to do initial creation of a fragment.  This is called after
     * {@link #onAttach(Activity)} and before
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * <p>
     * <p>Note that this can be called while the fragment's activity is
     * still in the process of being created.  As such, you can not rely
     * on things like the activity's content view hierarchy being initialized
     * at this point.  If you want to do work once the activity itself is
     * created, see {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>Any restored child fragments will be created before the base
     * <code>Fragment.onCreate</code> method returns.</p>
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("JU Connect");
    }

    private void updateData() {
        topicsArrayList = new ArrayList<>();
        topicsArrayList.clear();
        topicsArrayList.add(new Topic(R.drawable.canteen, "Canteen", TAG_CANTEEN));
        topicsArrayList.add(new Topic(R.drawable.photography, "Photography", TAG_PHOTO));
        topicsArrayList.add(new Topic(R.drawable.student, "Student Profile", TAG_STUDENT));
        topicsArrayList.add(new Topic(R.drawable.magazine, "e-Magazine", TAG_MAGAZINE));
        topicsArrayList.add(new Topic(R.drawable.events4, "Events", TAG_EVENTS));
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
//        return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        FirebaseMessaging.getInstance().subscribeToTopic("news");
        mPrefsManager = new PreferenceManager(getActivity());


        //If this message has been shown before then quickly skip this when layout is inflated.

        final PreferenceManager mPrefsManager = new PreferenceManager(getActivity());
        User user = mPrefsManager.getUser();
        //User class has access to all the variables accessed by the user.


        Button yesNotifications = rootView.findViewById(R.id.yesNotificationsButton);
        Button noNotifications = rootView.findViewById(R.id.noNotificationsButton);
        cardView = rootView.findViewById(R.id.cardView);
        if (mPrefsManager.isNotificationEnabled()) {
            cardView.setVisibility(View.GONE);
        }

        final ProgressBar progressBar = rootView.findViewById(R.id.feedRecyclerViewProgressView);

        yesNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cardView.setVisibility(View.GONE);
                mPrefsManager.setNotificationPrefs(true);
            }
        });

        noNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardView.setVisibility(View.GONE);
                mPrefsManager.setNotificationPrefs(false);
            }
        });

        RecyclerView mRecyclerView = rootView.findViewById(R.id.topicsRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator()); //Create a default item animater when the view comes to range
        updateData(); //Call the function to update the data set.
        mRecyclerView.setAdapter(new TopicsRecyclerAdapter(topicsArrayList));
        mRecyclerView.addOnItemTouchListener(new DashboardActivity.RecyclerItemClickListener(getActivity(), mRecyclerView, new DashboardActivity.RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) throws ExecutionException, InterruptedException {
                int tag = (int) view.getTag();
                Log.d(TAG, "postion:: " + tag);
                switch (tag) {
                    case TAG_CANTEEN:
                        Intent i = new Intent(getActivity(), ExploreActivity.class);
                        i.putExtra(EXTRA_CLASS_NAME, CANTEEN_FRAGMENT);
                        startActivity(i);
                        break;

                    case TAG_MAGAZINE:
                        Intent intent = new Intent(getActivity(), ExploreActivity.class);
                        intent.putExtra(EXTRA_CLASS_NAME, MAGAZINE_FRAGMENT);
                        startActivity(intent);
                        break;

                    case TAG_STUDENT:
                        Intent intent1 = new Intent(getActivity(), ExploreActivity.class);
                        intent1.putExtra(EXTRA_CLASS_NAME, STUDENT_FRAGMENT);
                        startActivity(intent1);
                        break;


                }

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


        RecyclerView feedRecyclerView = rootView.findViewById(R.id.feedRecyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        feedRecyclerView.setLayoutManager(manager);
        feedRecyclerView.setItemAnimator(new DefaultItemAnimator());
        feedRecyclerView.setNestedScrollingEnabled(false);
        feedRecyclerView.addItemDecoration(new DividerItemDecoration(feedRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        //2017-06-19T14:13:24.100Z
        final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = mRef.child("posts").getRef();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String uid = new PreferenceManager(getActivity()).getUID();
        DatabaseReference keyRef = mRef.child("users").child(uid).child("bookmarks").getRef();

        mAdapter = new FirebaseRecyclerAdapter<Feed, FeedHolder>(
                Feed.class,
                R.layout.recycler_child_feed,
                FeedHolder.class,
                ref) {
            @Override
            protected void populateViewHolder(FeedHolder viewHolder, Feed model, int position) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "populateViewHolder:: " + model.getHeading());
                viewHolder.setAuthor(model.getAuthor());
                viewHolder.setImage(model.getImageURL(), getActivity());
                viewHolder.setHeading(model.getHeading());
                viewHolder.setShortDesc(model.getShortDesc());
                viewHolder.setTime(model.getTime(), sdf);

            }

            @Override
            public FeedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                FeedHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.setOnClickListener(new FeedHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.d(TAG, "onClick::" + position);
                        Intent i = new Intent(getActivity(), DetailsFeedActivity.class);
                        i.putExtra(INTENT_EXTRA_FEED, mAdapter.getItem(position));
                        i.putExtra(Intent.EXTRA_TEXT, mAdapter.getRef(position).getKey());
                        Pair<View, String> pair1 = Pair.create(view.findViewById(R.id.postImageView), "sharedImage");
                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), pair1);
                        startActivity(i, optionsCompat.toBundle());
                    }
                });
                return viewHolder;
            }


        };


        mAdapter.notifyDataSetChanged();
        feedRecyclerView.setAdapter(mAdapter);


        return rootView;
    }


}
