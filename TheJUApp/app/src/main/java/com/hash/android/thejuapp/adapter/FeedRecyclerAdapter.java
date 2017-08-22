package com.hash.android.thejuapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.hash.android.thejuapp.DetailsFeedActivity;
import com.hash.android.thejuapp.Model.Feed;
import com.hash.android.thejuapp.R;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.support.v4.app.ActivityOptionsCompat.makeSceneTransitionAnimation;

/**
 * Created by Spandita Ghosh on 6/17/2017.
 */

public class FeedRecyclerAdapter extends RecyclerView.Adapter<FeedRecyclerAdapter.ViewHolder> {

    public static final String TAG = "FeedRecyclerView";
    public static final String INTENT_EXTRA_FEED = "extraParcelableFeed";
    public final ArrayList<Feed> mFeedList = new ArrayList<>();
    private final SimpleDateFormat simpleDateFormat;
    private final DatabaseReference mRef;
    private final ArrayList<String> mFeedIds = new ArrayList<>();
    private final Context mContext;


    public FeedRecyclerAdapter(SimpleDateFormat simpleDateFormat, DatabaseReference mRef, Context context) {
        this.simpleDateFormat = simpleDateFormat;
        this.mRef = mRef;
        this.mContext = context;

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Feed feed = dataSnapshot.getValue(Feed.class);
                String key = dataSnapshot.getKey();
                mFeedIds.add(0, key);
                mFeedList.add(0, feed);
//                notifyItemInserted(mFeedList.size());
                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        this.mRef.addChildEventListener(childEventListener);
    }


    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * . Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ViewHolder, int)
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = (LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_child_feed, parent, false));
        return new ViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Override  instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mFeedList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView image;
        final TextView author;
        final TextView time;
        final TextView heading;
        final TextView shortDesc;
        final TextView ad;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            image = itemView.findViewById(R.id.postImageView);
            author = itemView.findViewById(R.id.authorTextView);
            time = itemView.findViewById(R.id.timeTextView);
            heading = itemView.findViewById(R.id.headingTextView);
            shortDesc = itemView.findViewById(R.id.shortDescTextView);
            ad = itemView.findViewById(R.id.adTextView);
        }

        public void bind(int pos) {
            Glide.with(image.getContext())
                    .load(mFeedList.get(pos).getImageURL())
                    .placeholder(R.color.placeholder)
                    .crossFade()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(image);
            author.setText(mFeedList.get(pos).getAuthor());
            heading.setText(mFeedList.get(pos).getHeading());
            shortDesc.setText(mFeedList.get(pos).getShortDesc());
            try {
                long timeNow = simpleDateFormat.parse(mFeedList.get(pos).getTime()).getTime();
                PrettyTime prettyTime = new PrettyTime(Locale.getDefault());
                String ago = prettyTime.format(new Date(timeNow));
                time.setText(ago);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick::" + ((TextView) view.findViewById(R.id.headingTextView)).getText().toString());
            for (Feed feed : mFeedList) {
                if ((feed.getHeading().equalsIgnoreCase(((TextView) view.findViewById(R.id.headingTextView)).getText().toString())) && (feed.getAuthor().equalsIgnoreCase(((TextView) view.findViewById(R.id.authorTextView)).getText().toString()))) {
                    //Object found
                    Intent i = new Intent(mContext, DetailsFeedActivity.class);
                    Pair<View, String> pair1 = Pair.create(view.findViewById(R.id.postImageView), "sharedImage");
                    i.putExtra(INTENT_EXTRA_FEED, feed);
                    mContext.startActivity(i);
                    ActivityOptionsCompat optionsCompat = makeSceneTransitionAnimation((Activity) mContext, pair1);
                    mContext.startActivity(i, optionsCompat.toBundle());
                }
            }
        }
    }
}
