package com.hash.android.thejuapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Spandita Ghosh on 7/30/2017.
 */

public class Club implements Parcelable {
    public static final Creator<Club> CREATOR = new Creator<Club>() {
        @Override
        public Club createFromParcel(Parcel in) {
            return new Club(in);
        }

        @Override
        public Club[] newArray(int size) {
            return new Club[size];
        }
    };
    public String clubName, clubLink, clubTag, clubImage;

    public Club(String clubName, String clubLink, String clubTag, String clubImage) {
        this.clubName = clubName;
        this.clubLink = clubLink;
        this.clubTag = clubTag;
        this.clubImage = clubImage;
    }

    protected Club(Parcel in) {
        clubName = in.readString();
        clubLink = in.readString();
        clubTag = in.readString();
        clubImage = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(clubName);
        parcel.writeString(clubLink);
        parcel.writeString(clubTag);
        parcel.writeString(clubImage);
    }
}
