package com.hash.android.thejuapp.messaging;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.hash.android.thejuapp.Utils.PreferenceManager;


public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("token", "token:" + token);
        sendTokenToServer(token);


    }

    private void sendTokenToServer(String token) {

        new PreferenceManager(this).setNotificationKey(token);

    }
}
