package com.hash.android.thejuapp.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hash.android.thejuapp.Model.Canteen;
import com.hash.android.thejuapp.R;

import java.util.Locale;

/**
 * Created by Spandita Ghosh on 6/27/2017.
 */

class ViewHolder extends RecyclerView.ViewHolder {
    private final TextView canteenNameTextView;
    private final TextView locationTextView;
    private final Button naviagateButton;
    private TextView campusTextView;
    private ViewHolder.ClickListener mClickListener;

    public ViewHolder(View itemView) {
        super(itemView);
        canteenNameTextView = itemView.findViewById(R.id.canteenNameTextView);
//        campusTextView = itemView.findViewById(R.id.campusTextView);
        locationTextView = itemView.findViewById(R.id.locationTextView);
        naviagateButton = itemView.findViewById(R.id.navigateButton);

        naviagateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });

    }


    public void bind(Canteen canteen) {
        canteenNameTextView.setText(canteen.getCanteenName());
        campusTextView.setText(canteen.getCampus());
        try {
            locationTextView.setText(String.format(Locale.getDefault(), "%.1f", canteen.getLocation()) + " Km away");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View view, int position);
    }
}