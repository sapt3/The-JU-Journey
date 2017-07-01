package com.hash.android.thejuapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivityPre extends AppCompatActivity {

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeStatusBarColor();
        setContentView(R.layout.login_activity);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeName();

            }
        });
    }

    private void storeName() {
        com.hash.android.thejuapp.HelperClass.PreferenceManager mPreferenceManager = new com.hash.android.thejuapp.HelperClass.PreferenceManager(this);
        EditText editTextName = (EditText) findViewById(R.id.editText2);
        String name = editTextName.getText().toString().trim();
        if (!TextUtils.isEmpty(name)) { //If name is not empty
            mPreferenceManager.setName(name);
//            TextView welcomeTextView = (TextView) findViewById(R.id.textView);
//            welcomeTextView.animate().alpha(0f).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(200);
            Intent i = new Intent(LoginActivityPre.this, LoginActivity.class);
            Pair<View, String> pair = Pair.create(findViewById(R.id.textView1shared), getString(R.string.transition_login));
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginActivityPre.this, pair);
            startActivity(i, optionsCompat.toBundle());
        } else {
            Toast.makeText(this, "Name can't be empty. Enter your full name.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
//        startActivity(new Intent(this, DashboardActivity.class));

        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //TODO: Replace the dummy sign in logic later
        if (user != null) {
            startActivity(new Intent(this, DashboardActivity.class));
            LoginActivityPre.this.overridePendingTransition(0, 0);
        }
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
