package com.hash.android.thejuapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hash.android.thejuapp.EventsDetailsActivity;
import com.hash.android.thejuapp.Model.Event;
import com.hash.android.thejuapp.R;
import com.hash.android.thejuapp.ViewHolder.EventViewHolder;

import java.util.Date;

import static android.view.View.GONE;

public class EventsFragment extends android.support.v4.app.Fragment {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    int key;
    private FirebaseRecyclerAdapter<Event, EventViewHolder> mAdapter;

    public EventsFragment() {
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
        getActivity().setTitle("Events");
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
        View rootView = inflater.inflate(R.layout.fragment_events, container, false);
        Query mRef = FirebaseDatabase.getInstance().getReference("events").orderByChild("startDate");
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        final ProgressBar pb = rootView.findViewById(R.id.progressBarBookmark);
        final ImageView img = rootView.findViewById(R.id.bookmarkImageViewPlaceHolder);
        final TextView txt = rootView.findViewById(R.id.bookmarkTextViewPlaceHolder);

        if (getArguments() != null) {
            key = getArguments().getInt("KEY", -1);
            if (key == 0) {
                mRef = FirebaseDatabase.getInstance().getReference("events").orderByChild("startDate");
            } else if (key == 1) {
                mRef = FirebaseDatabase.getInstance().getReference("previousEvents").orderByChild("startDate");
                manager.setStackFromEnd(true);
                manager.setReverseLayout(true);
            }
        }

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0)
                    pb.setVisibility(GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        RecyclerView mRecyclerView = rootView.findViewById(R.id.eventsRecyclerView);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new FirebaseRecyclerAdapter<Event, EventViewHolder>(
                Event.class,
                R.layout.recycler_child_events,
                EventViewHolder.class,
                mRef
        ) {
            @Override
            protected void populateViewHolder(final EventViewHolder viewHolder, Event model, int position) {
                pb.setVisibility(GONE);
                img.setVisibility(GONE);
                txt.setVisibility(GONE);
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
                Date endDate = new Date(model.endDate);
                if (key == 0) {
                    if (new Date().after(endDate)) {
                        Event event = mAdapter.getItem(position);
                        String key = mAdapter.getRef(position).getKey();
                        FirebaseDatabase.getInstance().getReference("previousEvents").child(key).setValue(event);
                        mAdapter.getRef(position).removeValue();
                    }
                }
            }
        };

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        return rootView;

    }
}
