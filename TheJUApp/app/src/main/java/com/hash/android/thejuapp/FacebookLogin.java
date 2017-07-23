package com.hash.android.thejuapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hash.android.thejuapp.HelperClass.PreferenceManager;

import org.json.JSONObject;

import java.util.Arrays;

public class FacebookLogin extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 12344;

    private static final String TAG = FacebookLogin.class.getSimpleName();
    ImageView loginWithFacebook;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);
        loginWithFacebook = (ImageView) findViewById(R.id.loginWithFacebookButton);
        loginWithFacebook.setOnClickListener(this);
        pd = new ProgressDialog(FacebookLogin.this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.loginWithFacebookButton) {
            //Start login flow
            signIn();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if(pd != null){
//            if(pd.isShowing())  pd.dismiss();
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        if (user != null && new PreferenceManager(this).isFlowCompleted()) {
            AccessToken token = AccessToken.getCurrentAccessToken();

            fetchGraphData(token);

//            PreferenceManager mPrefsManager = new PreferenceManager(FacebookLogin.this);
//            mPrefsManager.setUID(user.getUid());
//            mPrefsManager.setEmail(user.getEmail());
//            mPrefsManager.setPhotoURL(user.getPhotoUrl().toString());
            Log.d(TAG, "PHOTO URL: " + user.getPhotoUrl().toString());
            Log.d(TAG, "Sign in successful");
            Intent i = new Intent(FacebookLogin.this, DashboardActivity.class);
            FacebookLogin.this.overridePendingTransition(0, 0);
            startActivity(i);
        }
    }

    private void fetchGraphData(AccessToken token) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            if (token != null) {
                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    Log.d(TAG, "object:: " + object.toString());
                                    PreferenceManager mPrefsManager = new PreferenceManager(FacebookLogin.this);
                                    String link = object.getString("link");
                                    String gender = object.getString("gender");
                                    JSONObject profilePhoto = object.getJSONObject("picture");
                                    JSONObject data = profilePhoto.getJSONObject("data");
                                    String photoURL = data.getString("url");
                                    JSONObject cover = object.getJSONObject("cover");
                                    String coverURL = cover.getString("source");
                                    String name = object.getString("name");
                                    mPrefsManager.setLink(link);
                                    mPrefsManager.setGender(gender);
                                    mPrefsManager.setCoverURL(coverURL);
                                    mPrefsManager.setPhotoURL(photoURL);
                                    mPrefsManager.setName(name);

                                    new PreferenceManager(FacebookLogin.this).saveUser();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "name, gender, email, link, cover, picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();
            }
        }
    }


    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void signIn() {

        AuthUI.IdpConfig facebookIdp = new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER)
                .setPermissions(Arrays.asList("public_profile", "email"))
                .build();
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(
                                        facebookIdp
                                )
                        )
                        .build()
                , RC_SIGN_IN
        );
        pd.setMessage("Creating your brand new account!");
        pd.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                AccessToken token = AccessToken.getCurrentAccessToken();
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {

                    fetchGraphData(token);
                    PreferenceManager mPrefsManager = new PreferenceManager(FacebookLogin.this);
                    mPrefsManager.setUID(user.getUid());
                    mPrefsManager.setEmail(user.getEmail());
                    mPrefsManager.setPhotoURL(user.getPhotoUrl().toString());
                    Intent i = new Intent(FacebookLogin.this, LoginActivity.class);
                    if (user.getEmail() != null) {
                        i.putExtra("email", user.getEmail());
                    }
                    FacebookLogin.this.overridePendingTransition(0, 0);
                    startActivity(i);

                } else {
                    Toast.makeText(this, "Failed to sign in", Toast.LENGTH_SHORT).show();

                }

//                finish();
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(this, "Sign in cancelled!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, "Unknown Error", Toast.LENGTH_SHORT).show();
                    return;
                }
            }


            Toast.makeText(this, "Unknown Error", Toast.LENGTH_SHORT).show();
        }
        pd.hide();
        pd.dismiss();
    }
}
