package com.hash.android.thejuapp.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hash.android.thejuapp.R;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.hash.android.thejuapp.adapter.FeedRecyclerAdapter.mFeedList;

/**
 * Created by Spandita Ghosh on 6/20/2017.
 */

public class FeedHolder extends RecyclerView.ViewHolder {
    private ImageView image;
    private TextView author, time, heading, shortDesc, ad;
    private SimpleDateFormat simpleDateFormat;

    public FeedHolder(View itemView, SimpleDateFormat simpleDateFormat) {
        super(itemView);
        image = (ImageView) itemView.findViewById(R.id.postImageView);
        author = (TextView) itemView.findViewById(R.id.authorTextView);
        time = (TextView) itemView.findViewById(R.id.timeTextView);
        heading = (TextView) itemView.findViewById(R.id.headingTextView);
        shortDesc = (TextView) itemView.findViewById(R.id.shortDescTextView);
        ad = (TextView) itemView.findViewById(R.id.adTextView);
        this.simpleDateFormat = simpleDateFormat;
    }

    public void bind(int pos) {
        Glide.with(image.getContext())
                .load(mFeedList.get(pos).getImageURL())
                .placeholder(R.drawable.placeholder)
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
}