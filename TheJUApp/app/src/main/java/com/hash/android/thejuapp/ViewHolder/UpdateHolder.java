package com.hash.android.thejuapp.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hash.android.thejuapp.Model.Update;
import com.hash.android.thejuapp.R;
import com.hash.android.thejuapp.Utils.CircleTransform;

public class UpdateHolder extends RecyclerView.ViewHolder {

    public final ImageView avatar;
    public final ImageView leftNav;
    public final ImageView rightNav;
    private final TextView priorityTV;
    private final TextView updateTV;

    public UpdateHolder(View itemView) {
        super(itemView);
        priorityTV = itemView.findViewById(R.id.priorityTextView);
        updateTV = itemView.findViewById(R.id.updateTextView);
        avatar = itemView.findViewById(R.id.avatarImage);
        leftNav = itemView.findViewById(R.id.leftNavigateRecyclerView);
        rightNav = itemView.findViewById(R.id.rightNavigateRecyclerView);

    }

    public void bind(Update update, Context context) {
        switch (update.priority) {
            case 0:
                priorityTV.setText("Class");
                break;
            case 1:
                priorityTV.setText("Department");
                break;
            case 2:
                break;
            case 3:
                priorityTV.setText("University");
        }

        updateTV.setText(update.title);
        Glide.with(context)
                .load(update.photoURL)
                .crossFade()
                .bitmapTransform(new CircleTransform(context))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(avatar);


    }

}
