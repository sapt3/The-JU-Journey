package com.hash.android.thejuapp.Model;

/**
 * Created by Spandita Ghosh on 6/21/2017.
 */

public class Canteen {
    private String canteenName;
    private String campus;
    private float location;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Canteen(String canteenName, String campus, float location, String key) {
        this.canteenName = canteenName;
        this.campus = campus;
        this.location = location;
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
