package com.hash.android.thejuapp.ViewHolder;

import android.content.Context;
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


public class FeedHolder extends RecyclerView.ViewHolder {
    public final ImageView image;
    public final ImageView logo;
    public final TextView author;
    public final TextView time;
    public final TextView heading;
    public final TextView shortDesc;
    public final TextView ad;
    public final TextView tag;
    public FeedHolder.ClickListener mClickListener;


    public FeedHolder(View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.postImageView);
        author = itemView.findViewById(R.id.authorTextView);
        time = itemView.findViewById(R.id.timeTextView);
        heading = itemView.findViewById(R.id.headingTextView);
        shortDesc = itemView.findViewById(R.id.shortDescTextView);
        ad = itemView.findViewById(R.id.adTextView);
        logo = itemView.findViewById(R.id.logoImageView);
        tag = itemView.findViewById(R.id.clubTagTextView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());

            }
        });

    }

    public void setTag(String tag1) {
        tag.setText(tag1);
    }

    public void setLogo(String url, Context context) {
        Glide.with(context)
                .load(url)
                .placeholder(R.color.placeholder)
                .crossFade()
                .thumbnail(0.5f)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(logo);
    }

    public void setAd(boolean isAd) {
        ad.setVisibility((isAd) ? View.VISIBLE : View.GONE);
    }

    public void setAuthor(String string) {
        author.setText(string);
    }

    public void setImage(String string, Context context) {
        Glide.with(context)
                .load(string)
                .placeholder(R.color.placeholder)
                .crossFade()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(image);
    }

    public void setHeading(String string) {
        heading.setText(string);
    }

    public void setShortDesc(String string) {
        shortDesc.setText(string);
    }

    public void setTime(String string, SimpleDateFormat simpleDateFormat) {
        try {
            long timeNow = simpleDateFormat.parse(string).getTime();
            PrettyTime prettyTime = new PrettyTime(Locale.getDefault());
            String ago = prettyTime.format(new Date(timeNow));
            time.setText(ago);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setOnClickListener(FeedHolder.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View view, int position);
    }
}

