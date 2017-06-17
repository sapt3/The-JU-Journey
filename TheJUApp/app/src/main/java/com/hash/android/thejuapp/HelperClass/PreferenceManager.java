package com.hash.android.thejuapp.HelperClass;

import android.content.Context;
import android.content.SharedPreferences;

import com.hash.android.thejuapp.Model.User;


public class PreferenceManager {
    private static final String PREFS_USER_NAME = "username";
    private static final String PREFS_USER_PHONE = "phoneNumber";
    private static final String PREFS_USER_UNIVERSITY = "university";
    private static final String PREFS_USER_GENDER = "gender";
    private static final String PREFS_USER_PROMO = "promo";
    private static final String PREFS_USER_PHOTO_URL = "photo";
    private static final String PREFS_USER_UID = "uid";
    private static final String PREFS_USER_EMAIL = "mail";
    private static final String PREFS_USER_NOTIFICATION = "notification7";

    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;

    private final static String PREFS_FIRST_TIME_LAUNCH = "firstTimeLaunch";

    public PreferenceManager(Context context) {
        String PREFS_NAME_STRING = "JUAppPreferences";
        int PREFS_ACCESS_MODE = 0;
        mPrefs = context.getSharedPreferences(PREFS_NAME_STRING, PREFS_ACCESS_MODE);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        mEditor = mPrefs.edit();
        mEditor.putBoolean(PREFS_FIRST_TIME_LAUNCH, isFirstTime);
        mEditor.apply();
    }

    public void setName(String name) {
        mEditor = mPrefs.edit();
        mEditor.putString(PREFS_USER_NAME, name);
        mEditor.apply();
    }

    public void setNotificationPrefs(boolean isNotificationEnabled) {
        mEditor = mPrefs.edit();
        mEditor.putBoolean(PREFS_USER_NOTIFICATION, isNotificationEnabled);
        mEditor.commit();
    }

    public boolean isNotificationEnabled() {
        return mPrefs.getBoolean(PREFS_USER_NOTIFICATION, false);
    }

    public String getName() {
        return mPrefs.getString(PREFS_USER_NAME, "");
    }


    public void setPhoneNumber(String number) {
        mEditor = mPrefs.edit();
        mEditor.putString(PREFS_USER_PHONE, number);
        mEditor.apply();
    }

    public String getPhoneNumber() {
        return mPrefs.getString(PREFS_USER_PHONE, "");
    }


    public void setUniversity(String university) {
        mEditor = mPrefs.edit();
        mEditor.putString(PREFS_USER_UNIVERSITY, university);
        mEditor.apply();
    }

    public String getUniversity() {
        return mPrefs.getString(PREFS_USER_UNIVERSITY, "");
    }


    public void setGender(String gender) {
        mEditor = mPrefs.edit();
        mEditor.putString(PREFS_USER_GENDER, gender);
        mEditor.apply();
    }

    public String getGender() {
        return mPrefs.getString(PREFS_USER_GENDER, "");
    }


    public void setPromo(boolean optInForPromo) {
        mEditor = mPrefs.edit();
        mEditor.putBoolean(PREFS_USER_PROMO, optInForPromo);
        mEditor.apply();
    }


    public boolean getPromo() {
        return mPrefs.getBoolean(PREFS_USER_PROMO, false);
    }


    public String getEmail() {
        return mPrefs.getString(PREFS_USER_EMAIL, "");
    }


    public void setEmail(String email) {
        mEditor = mPrefs.edit();
        mEditor.putString(PREFS_USER_EMAIL, email);
        mEditor.apply();
    }


    public String getUID() {
        return mPrefs.getString(PREFS_USER_UID, "");
    }


    public void setUID(String uid) {
        mEditor = mPrefs.edit();
        mEditor.putString(PREFS_USER_UID, uid);
        mEditor.apply();
    }


    public String getPhotoURL() {
        return mPrefs.getString(PREFS_USER_PHOTO_URL, "");
    }


    public void setPhotoURL(String url) {
        mEditor = mPrefs.edit();
        mEditor.putString(PREFS_USER_PHOTO_URL, url);
        mEditor.apply();
    }


    public User getUser() {
        return new User(getName(), getPhoneNumber(), getUniversity(), getGender(), getPhotoURL(), getUID(), getPromo(), getEmail());
    }

    public boolean isFirstTimeLaunch() {
        return mPrefs.getBoolean(PREFS_FIRST_TIME_LAUNCH, true);
    }

}
