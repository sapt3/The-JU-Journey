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
    private ImageView image;
    private TextView author, time, heading, shortDesc, ad;
    private FeedHolder.ClickListener mClickListener;


    public FeedHolder(View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.postImageView);
        author = itemView.findViewById(R.id.authorTextView);
        time = itemView.findViewById(R.id.timeTextView);
        heading = itemView.findViewById(R.id.headingTextView);
        shortDesc = itemView.findViewById(R.id.shortDescTextView);
        ad = itemView.findViewById(R.id.adTextView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());

            }
        });

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
                .placeholder(R.drawable.placeholder)
                .crossFade()
                .centerCrop()
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
//    public void bind(int pos) {
//        Glide.with(image.getContext())
//                .load(mFeedList.get(pos).getImageURL())
//                .placeholder(R.drawable.placeholder)
//                .crossFade()
//                .centerCrop()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(image);
//        author.setText(mFeedList.get(pos).getAuthor());
//        heading.setText(mFeedList.get(pos).getHeading());
//        shortDesc.setText(mFeedList.get(pos).getShortDesc());
//        try {
//            long timeNow = simpleDateFormat.parse(mFeedList.get(pos).getTime()).getTime();
//            PrettyTime prettyTime = new PrettyTime(Locale.getDefault());
//            String ago = prettyTime.format(new Date(timeNow));
//            time.setText(ago);
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }
