package com.hash.android.thejuapp.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseIndexRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hash.android.thejuapp.Model.User;
import com.hash.android.thejuapp.R;
import com.hash.android.thejuapp.ViewHolder.LeaderBoardViewHolder;

public class LeaderBoardFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "LeaderboardFragment";
    private final static int MAX_LEADERBOARD_USERS = 30;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    FirebaseIndexRecyclerAdapter<User, LeaderBoardViewHolder> mAdapter;

    public LeaderBoardFragment() {
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

        View rootView = inflater.inflate(R.layout.leaderboard_fragment, container, false);

        final ProgressBar pb = rootView.findViewById(R.id.progressBar4);
        final RecyclerView mRecyclerView = rootView.findViewById(R.id.leaderBoardRecyclerView);
        LinearLayoutManager mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference userRef = mDatabase.getReference("users").getRef();
        Query keyRef = mDatabase.getReference("leaderboard").orderByValue().limitToLast(MAX_LEADERBOARD_USERS);

        mAdapter = new FirebaseIndexRecyclerAdapter<User, LeaderBoardViewHolder>(
                User.class,
                R.layout.recycler_item_leaderboard,
                LeaderBoardViewHolder.class,
                keyRef,
                userRef
        ) {
            @Override
            protected void populateViewHolder(LeaderBoardViewHolder viewHolder, User model, int position) {
                pb.setVisibility(View.GONE);
//                Log.d(TAG, String.valueOf(mAdapter.getItemCount()));
                viewHolder.bind(model, mAdapter.getRef(position).getKey(), MAX_LEADERBOARD_USERS - position);
            }
        };

        new CountDownTimer(1500, 500) {

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                mRecyclerView.setAdapter(mAdapter);
            }

        }.start();

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Leaderboard");
    }
}
