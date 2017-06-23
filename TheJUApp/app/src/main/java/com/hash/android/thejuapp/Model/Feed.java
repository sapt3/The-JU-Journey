package com.hash.android.thejuapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Spandita Ghosh on 6/19/2017.
 */

@IgnoreExtraProperties
public class Feed implements Parcelable {

    private String time;
    private String imageURL;
    private String author;
    private String heading;
    private String shortDesc;
    private String longDesc;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();


    public Feed() {
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("time", time);
        result.put("author", author);
        result.put("imageURL", imageURL);
        result.put("heading", heading);
        result.put("shortDesc", shortDesc);
        result.put("longDesc", longDesc);
        result.put("starCount", starCount);
        result.put("stars", stars);

        return result;
    }
    public Feed(String time, String imageURL, String author, String heading, String shortDesc, String longDesc) {
        this.time = time;
        this.imageURL = imageURL;
        this.author = author;
        this.heading = heading;
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
    }

    protected Feed(Parcel in) {
        time = in.readString();
        imageURL = in.readString();
        author = in.readString();
        heading = in.readString();
        shortDesc = in.readString();
        longDesc = in.readString();
    }

    public static final Creator<Feed> CREATOR = new Creator<Feed>() {
        @Override
        public Feed createFromParcel(Parcel in) {
            return new Feed(in);
        }

        @Override
        public Feed[] newArray(int size) {
            return new Feed[size];
        }
    };

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(time);
        parcel.writeString(imageURL);
        parcel.writeString(author);
        parcel.writeString(heading);
        parcel.writeString(shortDesc);
        parcel.writeString(longDesc);
    }
}
