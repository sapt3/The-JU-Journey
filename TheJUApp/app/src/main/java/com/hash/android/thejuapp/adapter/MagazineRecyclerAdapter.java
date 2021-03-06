package com.hash.android.thejuapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hash.android.thejuapp.MagazineActivity;
import com.hash.android.thejuapp.Model.Magazine;
import com.hash.android.thejuapp.R;

import java.util.ArrayList;

public class MagazineRecyclerAdapter extends RecyclerView.Adapter<MagazineRecyclerAdapter.ViewHolder> {
    private final Context context;
    private ArrayList<Magazine> magazineArrayList = new ArrayList<>();

    public MagazineRecyclerAdapter(ArrayList<Magazine> magazineArrayList, Context context) {
        this.magazineArrayList = magazineArrayList;
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
    public MagazineRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_child_magazine, parent, false));
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
    public void onBindViewHolder(final MagazineRecyclerAdapter.ViewHolder holder, int position) {

        holder.editionDate.setText(magazineArrayList.get(position).editionDate);
        holder.editionName.setText(magazineArrayList.get(position).editionName);
        holder.coverPage.setImageResource(magazineArrayList.get(position).coverPage);
        Glide.with(context)
                .load(magazineArrayList.get(position).coverPage)
                .placeholder(R.color.placeholder)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.coverPage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                String url = magazineArrayList.get(pos).downloadURL;

                Intent i = new Intent(context, MagazineActivity.class);
                i.putExtra("DOWNLOAD_URL", url);
                context.startActivity(i);
            }
        });

    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return magazineArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView coverPage;
        final TextView editionName;
        final TextView editionDate;
        public ViewHolder(View itemView) {
            super(itemView);
            coverPage = itemView.findViewById(R.id.coverPageImageView);
            editionName = itemView.findViewById(R.id.editionTextView);
            editionDate = itemView.findViewById(R.id.editionPublishDateTextView);
        }
    }
}
