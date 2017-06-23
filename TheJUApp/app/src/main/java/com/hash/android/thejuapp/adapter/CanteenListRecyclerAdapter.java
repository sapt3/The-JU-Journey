package com.hash.android.thejuapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hash.android.thejuapp.Model.Canteen;
import com.hash.android.thejuapp.R;
import com.hash.android.thejuapp.ViewHolder.FeedHolder;

import java.util.ArrayList;

/**
 * Created by Spandita Ghosh on 6/21/2017.
 */

public class CanteenListRecyclerAdapter extends RecyclerView.Adapter<CanteenListRecyclerAdapter.ViewHolder> {
    private static final String TAG = CanteenListRecyclerAdapter.class.getSimpleName();
    private ArrayList<Canteen> mArrayList;
    private FeedHolder.ClickListener mClickListener;

    public CanteenListRecyclerAdapter(ArrayList<Canteen> mArrayList) {
        this.mArrayList = mArrayList;
    }

    public interface ClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnClickListener(FeedHolder.ClickListener clickListener) {
        mClickListener = clickListener;
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

        CanteenListRecyclerAdapter.ViewHolder vh = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_child_canteen_item, parent, false));

        return vh;
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
        return mArrayList.size();
    }

    public void filter(ArrayList<Canteen> mArrayListNew) {
        mArrayList.clear();
        mArrayList.addAll(mArrayListNew);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView canteenNameTextView, campusTextView, locationTextView;
        Button naviagateButton;

        public ViewHolder(View itemView) {
            super(itemView);
            canteenNameTextView = (TextView) itemView.findViewById(R.id.canteenNameTextView);
            campusTextView = (TextView) itemView.findViewById(R.id.campusTextView);
            locationTextView = (TextView) itemView.findViewById(R.id.locationTextView);
            naviagateButton = (Button) itemView.findViewById(R.id.navigateButton);

            naviagateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickListener.onItemClick(view, getAdapterPosition());
                }
            });
        }

        public void bind(int position) {
            canteenNameTextView.setText(mArrayList.get(position).getCanteenName());
            campusTextView.setText(mArrayList.get(position).getCampus());
            try {
                locationTextView.setText(String.format("%.1f", mArrayList.get(position).getLocation()) + " Km away");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
