package com.hash.android.thejuapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.firebase.ui.database.FirebaseIndexRecyclerAdapter;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hash.android.thejuapp.ClubActivity;
import com.hash.android.thejuapp.DashboardActivity;
import com.hash.android.thejuapp.DetailsFeedActivity;
import com.hash.android.thejuapp.ExploreActivity;
import com.hash.android.thejuapp.Model.Feed;
import com.hash.android.thejuapp.Model.Topic;
import com.hash.android.thejuapp.Model.Update;
import com.hash.android.thejuapp.Model.User;
import com.hash.android.thejuapp.ProfileActivity;
import com.hash.android.thejuapp.R;
import com.hash.android.thejuapp.Utils.PreferenceManager;
import com.hash.android.thejuapp.ViewHolder.FeedHolder;
import com.hash.android.thejuapp.ViewHolder.UpdateHolder;
import com.hash.android.thejuapp.adapter.TopicsRecyclerAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import static android.support.v4.app.ActivityOptionsCompat.makeSceneTransitionAnimation;
import static android.view.View.GONE;
import static com.hash.android.thejuapp.ExploreActivity.CANTEEN_FRAGMENT;
import static com.hash.android.thejuapp.ExploreActivity.EVENTS_FRAGMENT;
import static com.hash.android.thejuapp.ExploreActivity.EXTRA_CLASS_NAME;
import static com.hash.android.thejuapp.ExploreActivity.LEADERBOARD_FRAGMENT;
import static com.hash.android.thejuapp.ExploreActivity.MAGAZINE_FRAGMENT;
import static com.hash.android.thejuapp.ExploreActivity.PROFILE;
import static com.hash.android.thejuapp.ExploreActivity.STUDENT_FRAGMENT;
import static com.hash.android.thejuapp.adapter.FeedRecyclerAdapter.INTENT_EXTRA_FEED;
import static com.hash.android.thejuapp.adapter.StudentProfileRecyclerAdapter.INTENT_EXTRA_USER;


public class DashboardFragment extends Fragment {

    private static final int TAG_CANTEEN = 1;
    private static final int TAG_STUDENT = 3;
    private static final int TAG_MAGAZINE = 4;
    private static final int TAG_EVENTS = 5;
    private static final int TAG_LEADERBOARD = 6;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private final String TAG = DashboardActivity.class.getSimpleName();
    ArrayList<Feed> mFeedList = new ArrayList<>();
    private FirebaseIndexRecyclerAdapter<Feed, FeedHolder> mAdapter;
    private FirebaseIndexRecyclerAdapter<Update, UpdateHolder> updatesAdapter;
    private PreferenceManager mPrefsManager;
    private String journal = "https://firebasestorage.googleapis.com/v0/b/the-ju-app.appspot.com/o/club_logo%2Fjournal%20logo%20(Small).jpg?alt=media&token=2c5eba1a-8f55-43ec-8912-4b5373f4bb67";
    private ArrayList<Topic> topicsArrayList = new ArrayList<>();
    private CardView cardView;
    private float px;

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
        getActivity().setTitle("juX");
        this.setHasOptionsMenu(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause::");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop::");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume::");
//        if (updatesAdapter != null) {
            updatesAdapter.notifyDataSetChanged();
//        }
    }

    private void updateData() {
        topicsArrayList = new ArrayList<>();
        topicsArrayList.clear();
        topicsArrayList.add(new Topic(R.drawable.leaderboard, "Leaderboard", TAG_LEADERBOARD));
        topicsArrayList.add(new Topic(R.drawable.student, "Student Profile", TAG_STUDENT));
        topicsArrayList.add(new Topic(R.drawable.magazine, "e-Magazine", TAG_MAGAZINE));
        topicsArrayList.add(new Topic(R.drawable.events4, "Events", TAG_EVENTS));
        topicsArrayList.add(new Topic(R.drawable.canteen, "Canteen", TAG_CANTEEN));

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
        Log.d(TAG, "onCreate::");

        Resources r = getResources();
        px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, r.getDisplayMetrics());
        mPrefsManager = new PreferenceManager(getActivity());

        Button yesNotifications = rootView.findViewById(R.id.yesNotificationsButton);
        Button noNotifications = rootView.findViewById(R.id.noNotificationsButton);
        cardView = rootView.findViewById(R.id.cardView);
        if (mPrefsManager.isNotificationEnabled()) {
            cardView.setVisibility(GONE);
        }

        final ProgressBar progressBar = rootView.findViewById(R.id.feedRecyclerViewProgressView);

        yesNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cardView.setVisibility(GONE);
                mPrefsManager.setNotificationPrefs(true);
            }
        });

        noNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardView.setVisibility(GONE);
                mPrefsManager.setNotificationPrefs(false);
            }
        });

        RecyclerView mRecyclerView = rootView.findViewById(R.id.topicsRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator()); //Create a default item animater when the view comes to range
        updateData(); //Call the function to update the data set.
        mRecyclerView.setAdapter(new TopicsRecyclerAdapter(topicsArrayList));
        mRecyclerView.addOnItemTouchListener(new DashboardActivity.RecyclerItemClickListener(getActivity(), mRecyclerView, new DashboardActivity.RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) throws ExecutionException, InterruptedException {
                int tag = (int) view.getTag();
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

                    case TAG_LEADERBOARD:
                        Intent intent2 = new Intent(getActivity(), ExploreActivity.class);
                        intent2.putExtra(EXTRA_CLASS_NAME, LEADERBOARD_FRAGMENT);
                        startActivity(intent2);
                        break;

                    case TAG_EVENTS:
                        Intent intent3 = new Intent(getActivity(), ExploreActivity.class);
                        intent3.putExtra(EXTRA_CLASS_NAME, EVENTS_FRAGMENT);
                        startActivity(intent3);
                        break;

                }

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


        final RelativeLayout mLayout = rootView.findViewById(R.id.updatesPlaceHolder);

        final RecyclerView announcementsRecyclerView = rootView.findViewById(R.id.announcementsRV);
        LinearLayoutManager layoutmanager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        announcementsRecyclerView.setHasFixedSize(false);
        layoutmanager.setStackFromEnd(true);
        layoutmanager.setReverseLayout(true);
        announcementsRecyclerView.setLayoutManager(layoutmanager);
        LinearSnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(announcementsRecyclerView);
        announcementsRecyclerView.setItemAnimator(new DefaultItemAnimator()); //Create a default item animater when the view comes to range
//        updateData(); //Call the function to update the data set.
        final DatabaseReference updatesRef = FirebaseDatabase.getInstance().getReference("updates").getRef();
        final DatabaseReference keyRef = FirebaseDatabase.getInstance().getReference("users").child(mPrefsManager.getUID()).child("updateKeys").getRef();

        updatesAdapter = new FirebaseIndexRecyclerAdapter<Update, UpdateHolder>(
                Update.class,
                R.layout.recycler_child_announcement,
                UpdateHolder.class,
                keyRef,
                updatesRef
        ) {
            @Override
            protected void populateViewHolder(final UpdateHolder viewHolder, final Update model, final int position) {
                mLayout.setVisibility(GONE);
//                Log.d(TAG, "populateViewHolder:: " + model.title);
                viewHolder.bind(model, getActivity());
                final int size = updatesAdapter.getItemCount();

                try {
                    long timeOfExpiry = model.timeOfExpiry;
                    Date dateOfExpiry = new Date(timeOfExpiry);
                    Log.d(TAG, dateOfExpiry.toString());
                    if (new Date().after(dateOfExpiry)) {
                        //delete the key
                        String key = updatesAdapter.getRef(position).getKey();
                        FirebaseDatabase.getInstance().getReference("users").child(mPrefsManager.getUID()).child("updateKeys").child(key).removeValue();
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                viewHolder.avatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        User user = model.user;
                        Intent i = new Intent(getActivity(), ProfileActivity.class);
                        i.putExtra(INTENT_EXTRA_USER, user);
                        Pair<View, String> pair1 = Pair.create((View) viewHolder.avatar, "profileTrans");
                        ActivityOptionsCompat optionsCompat = makeSceneTransitionAnimation(getActivity(), pair1);
                        viewHolder.avatar.getContext().startActivity(i, optionsCompat.toBundle());
                    }
                });
                viewHolder.rightNav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG, "leftArrowPressed");
                        if (position > 0) {
                            announcementsRecyclerView.smoothScrollToPosition(position - 1);
                        }
                    }
                });
                viewHolder.leftNav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (position < size) {
                            announcementsRecyclerView.smoothScrollToPosition(position + 1);
                        }

                    }
                });

            }

            @Override
            public void onChildChanged(EventType type, DataSnapshot snapshot, int index, int oldIndex) {
                super.onChildChanged(type, snapshot, index, oldIndex);
                if (type == EventType.REMOVED && updatesAdapter.getItemCount() == 0) {
                    mLayout.setVisibility(View.VISIBLE);
                }
            }
            //            @Override
//            public void onDataChanged() {
//                super.onDataChanged();
//                int size = updatesAdapter.getItemCount();
//
//                Log.d(TAG, "position item:: " + updatesAdapter.getItemCount());
//                if (size == 0) {
//
//                }
//            }
        };

        updatesAdapter.notifyDataSetChanged();
        announcementsRecyclerView.setAdapter(updatesAdapter);

        final RecyclerView feedRecyclerView = rootView.findViewById(R.id.feedRecyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        feedRecyclerView.setLayoutManager(manager);
        feedRecyclerView.setItemAnimator(new DefaultItemAnimator());
        feedRecyclerView.setNestedScrollingEnabled(false);
        feedRecyclerView.addItemDecoration(new DividerItemDecoration(feedRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = mRef.child("posts").getRef();
        ref.keepSynced(true);
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        DatabaseReference keyRef2 = mRef.child("postKeys").getRef();


        mAdapter = new FirebaseIndexRecyclerAdapter<Feed, FeedHolder>(
                Feed.class,
                R.layout.recycler_child_new_feed,
                FeedHolder.class,
                keyRef2,
                ref) {
            @Override
            public void onChildChanged(EventType type, DataSnapshot snapshot, int index, int oldIndex) {
                super.onChildChanged(type, snapshot, index, oldIndex);
                if (type == EventType.ADDED) {
                    feedRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
                }
            }


            @Override
            protected void populateViewHolder(final FeedHolder viewHolder, final Feed model, int position) {

                viewHolder.setAd(false);
                if (position == mAdapter.getItemCount())
                    progressBar.setVisibility(GONE);
                viewHolder.setLogo((model.getLogo() != null) ? model.getLogo() : "", getActivity());
                viewHolder.setTag((model.getClub() != null) ? model.getClub() : "");
                viewHolder.setAuthor(model.getAuthor());
                if (model.getImageURL().length() > 0) {
                    viewHolder.image.requestLayout();
                    viewHolder.image.getLayoutParams().height = (int) px;
                    viewHolder.image.setVisibility(View.VISIBLE);
                    viewHolder.setImage(model.getImageURL(), getActivity());
                } else viewHolder.image.setVisibility(GONE);
                viewHolder.setHeading(model.getHeading());
                viewHolder.setShortDesc(model.getShortDesc());
                viewHolder.setTime(model.getTime(), sdf);
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = viewHolder.getAdapterPosition();
                        FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(getActivity());
                        Bundle bundle = new Bundle();
                        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, mAdapter.getRef(position).getKey());
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, mAdapter.getItem(position).getHeading());
                        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "feed");
                        analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                        Log.d(TAG, "onClick::" + position);
                        Intent i = new Intent(getActivity(), DetailsFeedActivity.class);
                        i.putExtra(INTENT_EXTRA_FEED, mAdapter.getItem(position));
                        i.putExtra(Intent.EXTRA_TEXT, mAdapter.getRef(position).getKey());
                        if (!TextUtils.isEmpty(mAdapter.getItem(position).getImageURL())) {
                            Pair<View, String> pair1 = Pair.create(view.findViewById(R.id.postImageView), "sharedImage");
                            ActivityOptionsCompat optionsCompat = makeSceneTransitionAnimation(getActivity(), pair1);
                            startActivity(i, optionsCompat.toBundle());
                        } else startActivity(i);
                    }
                });


                viewHolder.logo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = viewHolder.getAdapterPosition();
                        Intent i = new Intent(getActivity(), ClubActivity.class);
                        i.putExtra("club", mAdapter.getItem(position).getClub());
                        startActivity(i);
                    }
                });
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public FeedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                FeedHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.image.setVisibility(GONE);
                return viewHolder;
            }
        };

        mAdapter.notifyDataSetChanged();
        feedRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.nav_profile_menu) {
            Intent intent3 = new Intent(getActivity(), ExploreActivity.class);
            intent3.putExtra(EXTRA_CLASS_NAME, PROFILE);
            startActivity(intent3);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Initialize the contents of the Fragment host's standard options menu.  You
     * should place your menu items in to <var>menu</var>.  For this method
     * to be called, you must have first called {@link #setHasOptionsMenu}.  See
     * {@link Activity#onCreateOptionsMenu(Menu) Activity.onCreateOptionsMenu}
     * for more information.
     *
     * @param menu     The options menu in which you place your items.
     * @param inflater
     * @see #setHasOptionsMenu
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_login, menu);
        MenuItem item = menu.getItem(0);
        item.setIcon(R.drawable.ic_action_name_person_white);

    }

    //    @Override
//    public void onCreateOptionsMenu(Menu menu) {
//
    //
//    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart::");
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        Log.d(TAG, "onDestroy::");
        mAdapter.cleanup();
        updatesAdapter.cleanup();

    }
}
