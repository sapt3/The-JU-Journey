package com.hash.android.thejuapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.AuthCredential;

public class PhoneNumberAuth extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_auth);

        AuthCredential credential = getIntent().getParcelableExtra("credential");
    }
}
