package com.hash.android.thejuapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hash.android.thejuapp.Model.MenuItem;
import com.hash.android.thejuapp.R;

import java.util.ArrayList;

public class CanteenMenuRecyclerAdapter extends RecyclerView.Adapter<CanteenMenuRecyclerAdapter.ViewHolder> {
    private ArrayList<com.hash.android.thejuapp.Model.MenuItem> menuItemArrayList = new ArrayList<>();

    public CanteenMenuRecyclerAdapter(ArrayList<MenuItem> menuItemArrayList) {
        this.menuItemArrayList = menuItemArrayList;
    }

    private int randomImage() {
        int[] randomImageIndex = new int[]{R.drawable.cone, R.drawable.ctwo, R.drawable.ctwo, R.drawable.cthree, R.drawable.cfour, R.drawable.cfive, R.drawable.csix, R.drawable.cseven, R.drawable.ceight, R.drawable.cnine, R.drawable.cten, R.drawable.celeven, R.drawable.ctweleve, R.drawable.cthirteen, R.drawable.cfourteen};
        int index = (int) (Math.random() * 14); //Generates a random number between 0-9
        return randomImageIndex[index];
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
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_child_canteen_new, parent, false));
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
        if (!TextUtils.isEmpty(menuItemArrayList.get(position).itemPrice)) {
            holder.itemPriceTV.setText("Rs. " + menuItemArrayList.get(position).itemPrice);
        } else {
            holder.itemPriceTV.setVisibility(View.INVISIBLE);
        }
        holder.itemNameTV.setText(menuItemArrayList.get(position).itemName);
        holder.image.setImageResource(randomImage());
    }

    public void setFilter(ArrayList<MenuItem> arrayList) {
        menuItemArrayList.clear();
        menuItemArrayList.addAll(arrayList);
        notifyDataSetChanged();
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return menuItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView itemPriceTV;
        final TextView itemNameTV;
        final ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            itemNameTV = itemView.findViewById(R.id.foodItemName);
            itemPriceTV = itemView.findViewById(R.id.foodItemPrice);
            image = itemView.findViewById(R.id.itemImage);
        }
    }
}
