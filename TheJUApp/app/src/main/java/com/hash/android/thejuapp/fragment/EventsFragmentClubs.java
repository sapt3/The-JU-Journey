package com.hash.android.thejuapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hash.android.thejuapp.DashboardActivity;
import com.hash.android.thejuapp.EventsDetailsActivity;
import com.hash.android.thejuapp.HelperClass.ClubUtils;
import com.hash.android.thejuapp.Model.Club;
import com.hash.android.thejuapp.Model.Event;
import com.hash.android.thejuapp.R;
import com.hash.android.thejuapp.ViewHolder.EventViewHolder;


public class EventsFragmentClubs extends Fragment {
    private FirebaseRecyclerAdapter<Event, EventViewHolder> mAdapter;

    public EventsFragmentClubs() {
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

        View rootView = inflater.inflate(R.layout.events_club_fragment, container, false);


        RecyclerView mRecyclerView = rootView.findViewById(R.id.clubPostsRecyclerView);
        Query mRef = FirebaseDatabase.getInstance().getReference("events").orderByChild("startDate");
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        String tag = getArguments().getString("TAG");
        if (tag != null) {
            final Club club = new ClubUtils().findClub(tag);
            mAdapter = new FirebaseRecyclerAdapter<Event, EventViewHolder>(
                    Event.class,
                    R.layout.recycler_child_events,
                    EventViewHolder.class,
                    mRef
            ) {
                @Override
                protected void populateViewHolder(final EventViewHolder viewHolder, Event model, int position) {

                    if (model.club.equalsIgnoreCase(club.clubTag)) {
                        viewHolder.itemView.setVisibility(View.VISIBLE);
                        viewHolder.bind(model);
                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int position = viewHolder.getAdapterPosition();
                                Event event = mAdapter.getItem(position);
                                Intent i = new Intent(getActivity(), EventsDetailsActivity.class);
                                i.putExtra("KEY", mAdapter.getRef(position).getKey());
                                i.putExtra("EVENT", event);
                                startActivity(i);
                            }
                        });
                    } else {
                        viewHolder.hideItem();
                    }

                }
            };
        } else {
            startActivity(new Intent(getActivity(), DashboardActivity.class));
            getActivity().finish();
        }


        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        return rootView;
    }
}
