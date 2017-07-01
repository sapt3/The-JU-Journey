package com.hash.android.thejuapp.Model;

/**
 * Created by Spandita Ghosh on 6/21/2017.
 */

public class Canteen {
    private String canteenName;
    private String campus;
    private float location;
    private String key;
    private Double latitude;
    private Double longitude;

    public Canteen(String canteenName, String campus, float location, String key, Double latitude, Double longitude) {
        this.canteenName = canteenName;
        this.campus = campus;
        this.location = location;
        this.key = key;
        this.latitude = latitude;
        this.longitude = longitude;
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
}
