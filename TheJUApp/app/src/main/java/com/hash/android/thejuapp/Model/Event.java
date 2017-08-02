package com.hash.android.thejuapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Event implements Parcelable {
    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
    public long startDate;
    public long endDate;
    public String organisation;
    public String longDesc;
    public String generalEvent;
    public String event;
    public String contact;

    public Event() {
    }


    public Event(long startDate, long endDate, String organisation, String longDesc, String generalEvent, String event) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.organisation = organisation;
        this.longDesc = longDesc;
        this.generalEvent = generalEvent;
        this.event = event;
    }

    protected Event(Parcel in) {
        startDate = in.readLong();
        endDate = in.readLong();
        organisation = in.readString();
        longDesc = in.readString();
        generalEvent = in.readString();
        event = in.readString();
        contact = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(startDate);
        parcel.writeLong(endDate);
        parcel.writeString(organisation);
        parcel.writeString(longDesc);
        parcel.writeString(generalEvent);
        parcel.writeString(event);
        parcel.writeString(contact);
    }
}
