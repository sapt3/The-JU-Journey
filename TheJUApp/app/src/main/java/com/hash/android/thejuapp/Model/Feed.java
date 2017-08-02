package com.hash.android.thejuapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;


@IgnoreExtraProperties
public class Feed implements Parcelable {

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
    public int starCount = 0;
    public String contact; //Comma separated
    public Map<String, Boolean> stars = new HashMap<>();
    private String time;
    private String imageURL;
    private String author;
    private String heading;
    private String shortDesc;
    private String longDesc;
    private String logo;
    private String club;

    public Feed() {
    }

    public Feed(String time, String imageURL, String author, String heading, String shortDesc, String longDesc, String logo, String club, String contact) {
        this.time = time;
        this.imageURL = imageURL;
        this.author = author;
        this.heading = heading;
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
        this.logo = logo;
        this.club = club;
        this.contact = contact;
    }

    protected Feed(Parcel in) {
        time = in.readString();
        imageURL = in.readString();
        author = in.readString();
        heading = in.readString();
        shortDesc = in.readString();
        longDesc = in.readString();
        logo = in.readString();
        club = in.readString();
        starCount = in.readInt();
        contact = in.readString();
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }



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
        parcel.writeString(logo);
        parcel.writeString(club);
        parcel.writeInt(starCount);
        parcel.writeString(contact);
    }
}
