package com.hash.android.thejuapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Spandita Ghosh on 6/13/2017.
 */

public class User implements Parcelable {

    private String name;
    private String phoneNumber;
    private String university;
    private String gender;
    private String photoURL;
    private String UID;
    private boolean targetPromo;
    private String email;

    protected User(Parcel in) {
        name = in.readString();
        phoneNumber = in.readString();
        university = in.readString();
        gender = in.readString();
        photoURL = in.readString();
        UID = in.readString();
        targetPromo = in.readByte() != 0;
        email = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User(String name, String phoneNumber, String university, String gender, String photoURL, String UID, boolean targetPromo, String email) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.university = university;
        this.gender = gender;
        this.photoURL = photoURL;
        this.UID = UID;
        this.targetPromo = targetPromo;
        this.email = email;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean getTargetPromo() {
        return targetPromo;
    }

    public void setTargetPromo(boolean targetPromo) {
        this.targetPromo = targetPromo;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(phoneNumber);
        parcel.writeString(university);
        parcel.writeString(gender);
        parcel.writeString(photoURL);
        parcel.writeString(UID);
        parcel.writeByte((byte) (targetPromo ? 1 : 0));
        parcel.writeString(email);
    }
}
