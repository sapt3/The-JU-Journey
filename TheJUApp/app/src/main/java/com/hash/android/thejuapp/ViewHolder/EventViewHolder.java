package com.hash.android.thejuapp.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hash.android.thejuapp.Model.Event;
import com.hash.android.thejuapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class EventViewHolder extends RecyclerView.ViewHolder {
    public TextView eventName, date, month, timeRange, generalEvent;
    View view;

    public EventViewHolder(View itemView) {
        super(itemView);
        eventName = itemView.findViewById(R.id.eventTV);
        date = itemView.findViewById(R.id.dateTV);
        month = itemView.findViewById(R.id.monthTV);
        timeRange = itemView.findViewById(R.id.timeTV);
        generalEvent = itemView.findViewById(R.id.generalEvent);
        view = itemView;
    }

    public void bind(Event event) {
        Date endDate = new Date(event.endDate);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(endDate);


        eventName.setText(event.event);
        generalEvent.setText(event.generalEvent);

        Date startDate = new Date(event.startDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);


        String month = new SimpleDateFormat("MMMM", Locale.getDefault()).format(startDate);
        int date = cal.get(Calendar.DATE);
        String formattedMonth = month.substring(0, 3).toUpperCase();
        this.date.setText(String.valueOf(date));
        this.month.setText(formattedMonth);
        int startHour = cal.get(Calendar.HOUR);
        int startMinute = cal.get(Calendar.MINUTE);
        String startAM_PM = "";

        switch (cal.get(Calendar.AM_PM)) {
            case Calendar.AM:
                startAM_PM = "AM";
                break;
            case Calendar.PM:
                startAM_PM = "PM";
                break;
        }

        int endHour = calEnd.get(Calendar.HOUR);
        int endMinute = calEnd.get(Calendar.MINUTE);

        String endAM_PM = "";
        switch (calEnd.get(Calendar.AM_PM)) {
            case Calendar.AM:
                endAM_PM = "AM";
                break;
            case Calendar.PM:
                endAM_PM = "PM";
                break;
        }

        String formattedTime = startHour + ":" + startMinute + startAM_PM + " - " + endHour + ":" + endMinute + endAM_PM;
        this.timeRange.setText(formattedTime);


    }
}
