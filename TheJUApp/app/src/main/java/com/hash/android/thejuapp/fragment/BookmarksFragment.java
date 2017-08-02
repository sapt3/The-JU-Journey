package com.hash.android.thejuapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseIndexRecyclerAdapter;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hash.android.thejuapp.ClubActivity;
import com.hash.android.thejuapp.DetailsFeedActivity;
import com.hash.android.thejuapp.HelperClass.PreferenceManager;
import com.hash.android.thejuapp.Model.Feed;
import com.hash.android.thejuapp.R;
import com.hash.android.thejuapp.ViewHolder.FeedHolder;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import static android.view.View.GONE;
import static com.hash.android.thejuapp.adapter.FeedRecyclerAdapter.INTENT_EXTRA_FEED;

public class BookmarksFragment extends Fragment {

    private static final String TAG = BookmarksFragment.class.getSimpleName();
    private FirebaseIndexRecyclerAdapter<Feed, FeedHolder> mAdapter;
    private String journal = "https://firebasestorage.googleapis.com/v0/b/the-ju-app.appspot.com/o/club_logo%2Fjournal%20logo%20(Small).jpg?alt=media&token=2c5eba1a-8f55-43ec-8912-4b5373f4bb67";
    private float px;

    public BookmarksFragment() {
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
        getActivity().setTitle("Bookmarks");
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

        View rootView = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        Resources r = getResources();
        px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, r.getDisplayMetrics());
        final RecyclerView mRecyclerView = rootView.findViewById(R.id.bookmarkRecyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        final ProgressBar pb = rootView.findViewById(R.id.progressBarBookmark);
        final ImageView img = rootView.findViewById(R.id.bookmarkImageViewPlaceHolder);
        final TextView txt = rootView.findViewById(R.id.bookmarkTextViewPlaceHolder);


        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = mRef.child("posts").getRef();
        String uid = new PreferenceManager(getActivity()).getUID();
        DatabaseReference keyRef = mRef.child("users").child(uid).child("bookmarks").orderByValue().getRef();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        mAdapter = new FirebaseIndexRecyclerAdapter<Feed, FeedHolder>(
                Feed.class,
                R.layout.recycler_child_new_feed,
                FeedHolder.class,
                keyRef,
                ref) {
            @Override
            public void onChildChanged(EventType type, DataSnapshot snapshot, int index, int oldIndex) {
                super.onChildChanged(type, snapshot, index, oldIndex);
                if (type == EventType.ADDED) {
                    mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
                }
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
            }

            @Override
            protected void populateViewHolder(final FeedHolder viewHolder, final Feed model, int position) {

                pb.setVisibility(GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                img.setVisibility(GONE);
                txt.setVisibility(GONE);
                viewHolder.setAd(false);
                if (position == mAdapter.getItemCount())
                    viewHolder.setLogo((model.getLogo() != null) ? model.getLogo() : journal, getActivity());
                viewHolder.setTag((model.getClub() != null) ? model.getClub() : "@jujournal");
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
                            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), pair1);
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

            }

            @Override
            public FeedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                FeedHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.image.setVisibility(GONE);
                return viewHolder;
            }


        };


        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }
}
