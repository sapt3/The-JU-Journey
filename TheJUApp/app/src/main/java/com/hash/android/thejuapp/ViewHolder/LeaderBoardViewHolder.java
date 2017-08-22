package com.hash.android.thejuapp.ViewHolder;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hash.android.thejuapp.Model.User;
import com.hash.android.thejuapp.R;


public class LeaderBoardViewHolder extends RecyclerView.ViewHolder {

    private TextView pointsTV, nameTV, departmentTV, rankTV, rankPrefixTV;
    private ImageView avatar;
    private Typeface custom_font;
    private int[] colorList = new int[]{R.color.rank1, R.color.rank2};

    public LeaderBoardViewHolder(View itemView) {
        super(itemView);
        rankTV = itemView.findViewById(R.id.rankTextView);
        pointsTV = itemView.findViewById(R.id.pointsTextViewLeaderBoard);
        nameTV = itemView.findViewById(R.id.nameTextViewLeaderBoard);
        departmentTV = itemView.findViewById(R.id.departmentTextViewLeaderBoard);
        avatar = itemView.findViewById(R.id.avatarImageViewLeaderBoard);
        rankPrefixTV = itemView.findViewById(R.id.rankPrefixTextView);
        custom_font = Typeface.createFromAsset(rankTV.getContext().getAssets(), "fonts/opensans.ttf");

    }

    public void bind(User user, String key, int position) {

        rankTV.setTypeface(custom_font);
        pointsTV.setTypeface(custom_font);
        nameTV.setTypeface(custom_font);
        departmentTV.setTypeface(custom_font);
        rankPrefixTV.setTypeface(custom_font);
//        rankTV.setTextColor(R.color.colorAccent);
        int pos = position % 2;
        rankTV.setTextColor(ContextCompat.getColor(rankTV.getContext(), colorList[pos]));
        rankTV.setText(String.valueOf(position));
        if (position > 9) rankTV.setTextSize(36f);
        //Excuse this horrible practice below. Had no time :/
        switch (position) {
            case 1:
                rankPrefixTV.setText("st");
                break;

            case 2:
                rankPrefixTV.setText("nd");
                break;

            case 3:
                rankPrefixTV.setText("rd");
                break;

            case 21:
                rankPrefixTV.setText("st");
                break;

            case 22:
                rankPrefixTV.setText("nd");
                break;

            case 23:
                rankPrefixTV.setText("rd");
                break;

            default:
                rankPrefixTV.setText("th");
        }
        FirebaseDatabase.getInstance().getReference("leaderboard").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    int points = dataSnapshot.getValue(Integer.class);
                    pointsTV.setText(String.valueOf(points));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        pointsTV.setText(points);
        nameTV.setText(user.getName());
        departmentTV.setText(user.getDepartment());
        Glide.with(avatar.getContext())
                .load(user.getPhotoURL())
                .asBitmap()
                .placeholder(R.drawable.defaultdp)
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

