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
    public String event;
    public String club;
    public String contact;

    public Event() {

    }

    public Event(long startDate, long endDate, String organisation, String longDesc, String event, String club, String contact) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.organisation = organisation;
        this.longDesc = longDesc;
        this.event = event;
        this.club = club;
        this.contact = contact;
    }

    protected Event(Parcel in) {
        startDate = in.readLong();
        endDate = in.readLong();
        organisation = in.readString();
        longDesc = in.readString();
        event = in.readString();
        club = in.readString();
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
        parcel.writeString(event);
        parcel.writeString(club);
        parcel.writeString(contact);
    }
}
