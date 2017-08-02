package com.hash.android.thejuapp.fragment;

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
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hash.android.thejuapp.DashboardActivity;
import com.hash.android.thejuapp.DetailsFeedActivity;
import com.hash.android.thejuapp.HelperClass.ClubUtils;
import com.hash.android.thejuapp.HelperClass.PreferenceManager;
import com.hash.android.thejuapp.Model.Club;
import com.hash.android.thejuapp.Model.Feed;
import com.hash.android.thejuapp.R;
import com.hash.android.thejuapp.ViewHolder.FeedHolder;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import static android.support.v4.app.ActivityOptionsCompat.makeSceneTransitionAnimation;
import static com.hash.android.thejuapp.adapter.FeedRecyclerAdapter.INTENT_EXTRA_FEED;

/**
 * Created by Spandita Ghosh on 8/1/2017.
 */

public class PostsClubFragment extends Fragment {
    private static final String TAG = PostsClubFragment.class.getSimpleName();
    private FirebaseRecyclerAdapter<Feed, FeedHolder> mAdapter;
    private float px;

    public PostsClubFragment() {
    }

    /**
     * Called to do initial creation of a fragment.  This is called after
     * and before
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
        View rootView = inflater.inflate(R.layout.layout_club_post, container, false);

        String tag = getArguments().getString("TAG");
        Club club = new ClubUtils().findClub(tag);

        if (club != null) {
            RecyclerView mRecyclerView = rootView.findViewById(R.id.clubPostsRecyclerView);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());

            final ProgressBar pb = rootView.findViewById(R.id.progressBarClub1);
            mRecyclerView.setNestedScrollingEnabled(false);
            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
            manager.setReverseLayout(true);
            manager.setStackFromEnd(true);
            mRecyclerView.setLayoutManager(manager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
            //2017-06-19T14:13:24.100Z
            final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
            Query ref = mRef.child("posts").orderByChild("club").equalTo(club != null ? club.clubTag : null).limitToFirst(80);
            ref.keepSynced(true);
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            String uid = new PreferenceManager(getActivity()).getUID();
            Resources r = getResources();
            px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, r.getDisplayMetrics());

            mAdapter = new FirebaseRecyclerAdapter<Feed, FeedHolder>(
                    Feed.class,
                    R.layout.recycler_child_new_feed,
                    FeedHolder.class,
                    ref) {
                @Override
                protected void populateViewHolder(final FeedHolder viewHolder, Feed model, int position) {
//                    progressBar.setVisibility(View.GONE);
                    pb.setVisibility(View.GONE);
                    viewHolder.setAd(false);
                    viewHolder.setLogo((model.getLogo() != null) ? model.getLogo() : "", getActivity());
                    viewHolder.setTag((model.getClub() != null) ? model.getClub() : "@jujournal");
                    Log.d(TAG, "populateViewHolder:: " + model.getHeading());
                    viewHolder.setAuthor(model.getAuthor());
                    if (model.getImageURL().length() > 0) {
                        viewHolder.image.requestLayout();
                        viewHolder.image.getLayoutParams().height = (int) px;
                        viewHolder.image.setVisibility(View.VISIBLE);
                        viewHolder.setImage(model.getImageURL(), getActivity());
                    } else viewHolder.image.setVisibility(View.GONE);
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
                            Pair<View, String> pair1 = Pair.create(view.findViewById(R.id.postImageView), "sharedImage");
                            ActivityOptionsCompat optionsCompat = makeSceneTransitionAnimation(getActivity(), pair1);
                            startActivity(i, optionsCompat.toBundle());
                        }
                    });

                }

                @Override
                public FeedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    FeedHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                    viewHolder.image.setVisibility(View.GONE);
                    return viewHolder;
                }
            };

            mAdapter.notifyDataSetChanged();
            mRecyclerView.setAdapter(mAdapter);
        } else {
            startActivity(new Intent(getActivity(), DashboardActivity.class));
            getActivity().finish();
        }
        return rootView;
    }
}
