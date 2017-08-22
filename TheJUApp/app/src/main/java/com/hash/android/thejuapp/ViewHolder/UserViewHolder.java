package com.hash.android.thejuapp.ViewHolder;

import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.hash.android.thejuapp.Model.User;
import com.hash.android.thejuapp.R;

class UserViewHolder extends RecyclerView.ViewHolder {
    private final TextView nameTV;
    private final TextView departmentTV;
    private final ImageView profileIV;
    private final View view;

    public UserViewHolder(View itemView) {
        super(itemView);
        nameTV = itemView.findViewById(R.id.nameTextView);
        departmentTV = itemView.findViewById(R.id.departmentTextView);
        profileIV = itemView.findViewById(R.id.profilePictureImageView);
        view = itemView;
    }

    public void bind(User user) {
        nameTV.setText(user.getName());
        Glide.with(profileIV.getContext())
                .load(user.getPhotoURL())
                .asBitmap()
                .centerCrop()
                .into(new BitmapImageViewTarget(profileIV) {
                    /**
                     * Sets the {@link Bitmap} on the view using
                     * {@link ImageView#setImageBitmap(Bitmap)}.
                     *
                     * @param resource The bitmap to display.
                     */
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(profileIV.getContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        profileIV.setImageDrawable(circularBitmapDrawable);
                    }
                });
        departmentTV.setText(user.getDepartment());

    }
}

