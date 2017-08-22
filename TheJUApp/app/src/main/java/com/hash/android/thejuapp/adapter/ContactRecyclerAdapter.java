package com.hash.android.thejuapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.hash.android.thejuapp.Model.User;
import com.hash.android.thejuapp.ProfileActivity;
import com.hash.android.thejuapp.R;

import java.util.ArrayList;

import static com.hash.android.thejuapp.adapter.StudentProfileRecyclerAdapter.INTENT_EXTRA_USER;


public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactRecyclerAdapter.ViewHolder> {

    private ArrayList<User> mArraylist = new ArrayList<>();

    public ContactRecyclerAdapter(ArrayList<User> mArraylist) {
        this.mArraylist = mArraylist;
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
    public ContactRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout_contact, parent, false));
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
    public void onBindViewHolder(final ContactRecyclerAdapter.ViewHolder holder, int position) {
        holder.bind(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(holder.itemView.getContext(), ProfileActivity.class);
                i.putExtra(INTENT_EXTRA_USER, mArraylist.get(holder.getAdapterPosition()));
                Pair<View, String> pair1 = Pair.create((View) holder.avatar, "profileTrans");
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) holder.avatar.getContext(), pair1);
                holder.avatar.getContext().startActivity(i, optionsCompat.toBundle());

            }
        });
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                String number = mArraylist.get(position).getPhoneNumber();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + number));
                holder.itemView.getContext().startActivity(intent);
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
        return mArraylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final TextView number;
        final ImageView avatar;
        final ImageView call;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameContact);
            number = itemView.findViewById(R.id.phoneContact);
            avatar = itemView.findViewById(R.id.avatarContact);
            call = itemView.findViewById(R.id.phoneImageView);
        }

        public void bind(int position) {
            name.setText(mArraylist.get(position).getName());
            number.setText(mArraylist.get(position).getPhoneNumber());
            Glide.with(avatar.getContext())
                    .load(mArraylist.get(position).getPhotoURL())
                    .asBitmap()
                    .centerCrop()
                    .into(new BitmapImageViewTarget(avatar) {
                        /**
                         * Sets the {@link Bitmap} on the view using
                         * {@link ImageView#setImageBitmap(Bitmap)}.
                         *
                         * @param resource The bitmap to display.
                         */
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(avatar.getContext().getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            avatar.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        }
    }

}
