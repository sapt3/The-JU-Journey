package com.hash.android.thejuapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Canteen extends ArrayList<Parcelable> implements Parcelable {
    public static final Creator<Canteen> CREATOR = new Creator<Canteen>() {
        @Override
        public Canteen createFromParcel(Parcel in) {
            return new Canteen(in);
        }

        @Override
        public Canteen[] newArray(int size) {
            return new Canteen[size];
        }
    };
    public String shortTimings;
    public String longTimings;
    private String canteenName;
    private String campus;
    private float location;
    private String key;
    private Double latitude;
    private Double longitude;


    public Canteen(String canteenName, String campus, float location, String key, Double latitude, Double longitude, String shortTimings, String longTimings) {
        this.canteenName = canteenName;
        this.campus = campus;
        this.location = location;
        this.key = key;
        this.latitude = latitude;
        this.longitude = longitude;
        this.shortTimings = shortTimings;
        this.longTimings = longTimings;
    }

    protected Canteen(Parcel in) {
        canteenName = in.readString();
        campus = in.readString();
        location = in.readFloat();
        key = in.readString();
        shortTimings = in.readString();
        longTimings = in.readString();
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCanteenName() {
        return canteenName;
    }

    public void setCanteenName(String canteenName) {
        this.canteenName = canteenName;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public float getLocation() {
        return location;
    }

    public void setLocation(float location) {
        this.location = location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(canteenName);
        parcel.writeString(campus);
        parcel.writeFloat(location);
        parcel.writeString(key);
        parcel.writeString(shortTimings);
        parcel.writeString(longTimings);
    }


}
