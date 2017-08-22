package com.hash.android.thejuapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hash.android.thejuapp.CanteenMenu;
import com.hash.android.thejuapp.Model.Canteen;
import com.hash.android.thejuapp.R;

import java.util.ArrayList;
import java.util.Locale;

public class CanteenListRecyclerAdapter extends RecyclerView.Adapter<CanteenListRecyclerAdapter.ViewHolder> {
    public static final String INTENT_KEY = "key";
    private static final String TAG = CanteenListRecyclerAdapter.class.getSimpleName();
    private final ArrayList<Canteen> mArrayList;
    private final Context context;
    private final int[] colorIndex = new int[]{R.color.canteen1, R.color.canteen2, R.color.canteen3, R.color.canteen4, R.color.canteen5, R.color.canteen1, R.color.canteen2, R.color.canteen3, R.color.canteen4};

    public CanteenListRecyclerAdapter(ArrayList<Canteen> mArrayList, Context context) {
        this.mArrayList = mArrayList;
        this.context = context;
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

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_canteen_new, parent, false));
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bind(position);
        holder.naviagateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                navigateTo(mArrayList.get(pos).getLatitude(), mArrayList.get(pos).getLongitude(), mArrayList.get(pos).getCanteenName());
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                Intent i = new Intent(context, CanteenMenu.class);
                i.putExtra(INTENT_KEY, (Parcelable) mArrayList.get(holder.getAdapterPosition()));
                context.startActivity(i);
            }
        });
    }

    private void navigateTo(double lat, double lng, String canteenName) {
        String format = "geo:0,0?q=" + lat + "," + lng + "(" + canteenName + ")";
        Uri uri = Uri.parse(format);

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
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

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView canteenNameTextView;
        final TextView locationTextView;
        final ImageView naviagateButton;
        final RelativeLayout frame1;
        final FrameLayout frame2;
//        ImageView canteenImageView;

        ViewHolder(View itemView) {
            super(itemView);
            frame1 = itemView.findViewById(R.id.distanceFrame);
            frame2 = itemView.findViewById(R.id.frameLayout);
            canteenNameTextView = itemView.findViewById(R.id.canteenNameTextView);
//            campusTextView = itemView.findViewById(R.id.campusTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            naviagateButton = itemView.findViewById(R.id.navigateButton);
//            canteenImageView = itemView.findViewById(R.id.canteenImageView);

        }


        void bind(int position) {
            frame1.setBackgroundColor(ContextCompat.getColor(context, colorIndex[position]));
            frame2.setBackgroundColor(ContextCompat.getColor(context, colorIndex[position]));

            canteenNameTextView.setText(mArrayList.get(position).getCanteenName());
            try {
                locationTextView.setText(String.format(Locale.getDefault(), "%.1f", mArrayList.get(position).getLocation()));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
