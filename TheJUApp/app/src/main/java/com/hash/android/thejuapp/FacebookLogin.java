package com.hash.android.thejuapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
    private static final int RC_SIGN_IN_PHONE = 1234;

    private static final String TAG = FacebookLogin.class.getSimpleName();
    private static AccessToken token;
    ImageView loginWithFacebook;
    private ProgressDialog pd;
    private PreferenceManager mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);
        TextView welcome, create;
        mPrefs = new PreferenceManager(this);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/opensans.ttf");
        welcome = (TextView) findViewById(R.id.welcomeTextViewLogin);
        create = (TextView) findViewById(R.id.createAccountTextViewLogin);
        welcome.setTypeface(custom_font);
        create.setTypeface(custom_font);
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
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        if (user != null && new PreferenceManager(this).isFlowCompleted()) {
            Intent i = new Intent(FacebookLogin.this, DashboardActivity.class);
            FacebookLogin.this.overridePendingTransition(0, 0);
            startActivity(i);
            finish();
        }
    }

    private void fetchGraphData(AccessToken token) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && token != null) {

                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    String link = object.getString("link");
                                    String gender = object.getString("gender");
                                    JSONObject profilePhoto = object.getJSONObject("picture");
                                    JSONObject data = profilePhoto.getJSONObject("data");
                                    String photoURL = data.getString("url");
                                    JSONObject cover = object.getJSONObject("cover");
                                    String coverURL = cover.getString("source");
                                    String name = object.getString("name");
                                    mPrefs.setLink(link);
                                    mPrefs.setGender(gender);
                                    mPrefs.setCoverURL(coverURL);
                                    mPrefs.setPhotoURL(photoURL);
                                    mPrefs.setName(name);
                                    mPrefs.saveUser();

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



    private void signIn() {

        //Create an IDPConfig for facebook
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
        if (!pd.isShowing()) {
            pd.setMessage("Creating your brand new account!");
            pd.show();
        }
    }


    private void verifyPhone() {

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(
                                        new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()
                                )
                        )
                        .build()
                , RC_SIGN_IN_PHONE
        );
        pd.hide();
        pd.setMessage("Verifying your phone number!");
        pd.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                token = AccessToken.getCurrentAccessToken();
                new PreferenceManager(FacebookLogin.this).setFacebookToken(token.toString());
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {

                    fetchGraphData(token);
                    mPrefs.setUID(user.getUid());
                    mPrefs.setEmail(user.getEmail());
                    if (user.getPhotoUrl() != null)
                        mPrefs.setPhotoURL(user.getPhotoUrl().toString());
                    FirebaseAuth.getInstance().signOut();
                    verifyPhone();

                } else {
                    Toast.makeText(this, "Failed to sign in", Toast.LENGTH_SHORT).show();

                }
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
        } else if (requestCode == RC_SIGN_IN_PHONE) {

            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    mPrefs.setPhoneNumber(user.getPhoneNumber());
                    Intent i = new Intent(FacebookLogin.this, LoginActivity.class);
                    FacebookLogin.this.overridePendingTransition(0, 0);
                    startActivity(i);
                    finish();


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
