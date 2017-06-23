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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hash.android.thejuapp.CanteenListActivity;
import com.hash.android.thejuapp.DashboardActivity;
import com.hash.android.thejuapp.DetailsFeedActivity;
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

import static com.hash.android.thejuapp.adapter.FeedRecyclerAdapter.INTENT_EXTRA_FEED;

/**
 * Created by Spandita Ghosh on 6/23/2017.
 */

public class DashboardFragment extends Fragment implements View.OnClickListener {

    private static final int TAG_CANTEEN = 1;
    private static final int TAG_PHOTO = 2;
    private static final int TAG_STUDENT = 3;
    private static final int TAG_MAGAZINE = 4;
    private static final int TAG_EVENTS = 5;

    ArrayList<Feed> mFeedList = new ArrayList<>();
    private RecyclerView feedRecyclerView;
    PreferenceManager mPrefsManager;
    private final String TAG = DashboardActivity.class.getSimpleName();
    private ArrayList<Topic> topicsArrayList = new ArrayList<>();
    private final String URL_NAV_BACKGROUND = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private CardView cardView;
    public FirebaseRecyclerAdapter<Feed, FeedHolder> mAdapter;
    private FirebaseAuth mAuth;

    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";

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


        mAuth = FirebaseAuth.getInstance();
        mPrefsManager = new PreferenceManager(getActivity());






        //If this message has been shown before then quickly skip this when layout is inflated.

        final PreferenceManager mPrefsManager = new PreferenceManager(getActivity());
        User user = mPrefsManager.getUser();
        //User class has access to all the variables accessed by the user.



        ImageView noNotificationIV = (ImageView) rootView.findViewById(R.id.noNotifcationImageView);
        ImageView yesNotificationIV = (ImageView) rootView.findViewById(R.id.yesNotifcationImageView);
        TextView noNotificationTV = (TextView) rootView.findViewById(R.id.noNotificationsTextView);
        TextView yesNotificationTV = (TextView) rootView.findViewById(R.id.yesNotificationsTextView);
        cardView = (CardView) rootView.findViewById(R.id.cardView);
        if (mPrefsManager.isNotificationEnabled()) {
            cardView.setVisibility(View.GONE);
        }

        final ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.feedRecyclerViewProgressView);
//        progressBar.setVisibility(View.VISIBLE);

//        StreamAnalyticsAuth auth = new StreamAnalyticsAuth(getString(R.string.API_KEY),getString(R.string.API_TOKEN));
//        StreamAnalytics client = StreamAnalyticsImpl.getInstance(auth);




        noNotificationIV.setOnClickListener(this);
        noNotificationTV.setOnClickListener(this);
        yesNotificationIV.setOnClickListener(this);
        yesNotificationTV.setOnClickListener(this);

        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.topicsRecyclerView);
//        LinearSnapHelper helper = new LinearSnapHelper();
//        helper.attachToRecyclerView(mRecyclerView); //Attaching a linear helper to allow the vertical recycler view to snap to center.
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()   , LinearLayoutManager.HORIZONTAL, false));
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
                        startActivity(new Intent(getActivity(), CanteenListActivity.class));
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


        feedRecyclerView = (RecyclerView) rootView.findViewById(R.id.feedRecyclerView);
//        feedRecyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        feedRecyclerView.setLayoutManager(manager);
        feedRecyclerView.setItemAnimator(new DefaultItemAnimator());
        feedRecyclerView.setNestedScrollingEnabled(false);
        feedRecyclerView.addItemDecoration(new DividerItemDecoration(feedRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
//        updateList();
        //2017-06-19T14:13:24.100Z
        final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = mRef.child("posts").getRef();
        String uid = new PreferenceManager(getActivity()).getUID();
        DatabaseReference keyRef = mRef.child("users").child(uid).child("bookmarks").getRef();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
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

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.noNotifcationImageView:
                cardView.setVisibility(View.GONE);
                mPrefsManager.setNotificationPrefs(false);
                break;

            case R.id.noNotificationsTextView:
                cardView.setVisibility(View.GONE);
                mPrefsManager.setNotificationPrefs(false);
                break;

            case R.id.yesNotifcationImageView:
                cardView.setVisibility(View.GONE);
                mPrefsManager.setNotificationPrefs(true);
                break;

            case R.id.yesNotificationsTextView:
                cardView.setVisibility(View.GONE);
                mPrefsManager.setNotificationPrefs(true);
                break;

            default:
                break;
        }

    }
}
