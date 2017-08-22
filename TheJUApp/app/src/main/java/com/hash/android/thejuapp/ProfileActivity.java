package com.hash.android.thejuapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hash.android.thejuapp.Model.User;
import com.hash.android.thejuapp.Utils.CircleTransform;
import com.hash.android.thejuapp.Utils.PreferenceManager;

import static com.hash.android.thejuapp.adapter.StudentProfileRecyclerAdapter.INTENT_EXTRA_USER;

public class ProfileActivity extends AppCompatActivity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private PreferenceManager mPrefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("View Profile");
        Intent i = getIntent();
        final User user = i.getParcelableExtra(INTENT_EXTRA_USER);
        if (user == null) {
            finish();
        } else {

            mPrefsManager = new PreferenceManager(this);

            TextView statusTextView = (TextView) findViewById(R.id.statusEditTextProfile);
            if (!TextUtils.isEmpty(user.getAbout())) {
                //If about is not empty
                statusTextView.setText(user.getAbout());
            } else {
                statusTextView.setText(R.string.placeholder_text);
            }
            ImageView profileImageView = (ImageView) findViewById(R.id.profileImageViewProfile);

            Glide.with(this)
                    .load(user.getPhotoURL())
                    .crossFade()
                    .placeholder(R.drawable.defaultdp)
                    .bitmapTransform(new CircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(profileImageView);

            TextView nameTextView = (TextView) findViewById(R.id.nameTextViewProfile);
            nameTextView.setText(user.getName());

            TextView facultyTextView = (TextView) findViewById(R.id.facultyTextViewProfile);
            facultyTextView.setText(user.getFaculty());

            TextView departmentTextView = (TextView) findViewById(R.id.departmentTextViewProfile);
            //COMPLETED: Replace with original department
            if (user.getDepartment() != null) {
                departmentTextView.setText(user.getDepartment());
            }

            TextView yearOfJoining = (TextView) findViewById(R.id.yearOfJoiningTextViewProfile);
            yearOfJoining.setText("Year of Joining: " + user.getYearOfPassing());

            TextView emailTextView = (TextView) findViewById(R.id.emailTextViewProfile);
            emailTextView.setText(user.getEmail());

            TextView phoneNumberTextView = (TextView) findViewById(R.id.phoneNumberProfile);
            ImageView privateButton = (ImageView) findViewById(R.id.privateAccessButton);
            if (user.isPrivate()) {
                privateButton.setImageResource(R.drawable.ic_lock_outline_black_24dp);
                phoneNumberTextView.setText("**********");
            } else {
                privateButton.setImageResource(R.drawable.ic_lock_open_black_24dp);
                phoneNumberTextView.setText(user.getPhoneNumber());
            }

            if (mPrefsManager.isPrivate()) {
                phoneNumberTextView.setText("**********");
            }

            TextView privacyMessage = (TextView) findViewById(R.id.privacyMessageTextView);
            FrameLayout messageHolder = (FrameLayout) findViewById(R.id.messagePrivacyFrameLayout);

            String message;
            if (user.isPrivate() && !mPrefsManager.isPrivate()) {
                message = "You cannot see this phone number because the user has set it to private.";
                privacyMessage.setText(message);
            } else if (mPrefsManager.isPrivate() && !user.isPrivate()) {
                message = "You cannot see this phone number because you have made your own phone number private. Change it to public.";
                privacyMessage.setText(message);
            } else if (mPrefsManager.isPrivate() && user.isPrivate()) {
                message = "You cannot see this phone number as both you and the user have made their own phone numbers private.";
                privacyMessage.setText(message);
            } else {
                messageHolder.setVisibility(View.GONE);
            }


            Button facebookButton = (Button) findViewById(R.id.clubContactButton);
            facebookButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (user.getLink() != null) {
                        Uri uriUrl = Uri.parse(user.getLink());
                        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                        startActivity(launchBrowser);
                    } else {
                        Toast.makeText(ProfileActivity.this, "Invalid link.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

//            ImageView emailButton = (ImageView) findViewById(R.id.emailButtonProfile);
//            emailButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
//                            "mailto", user.getEmail(), null));
//                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
//
//                }
//            });


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }
}
