package com.hash.android.thejuapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.hash.android.thejuapp.HelperClass.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import static android.view.View.OnClickListener;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
import static android.view.View.VISIBLE;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 0;
    private static final String TAG = LoginActivity.class.getSimpleName();
    GoogleSignInOptions googleSignInOptions;
    GoogleApiClient mGoogleApiClient;
    EditText phoneET, yearOfPassingET;
    CheckBox promoCB, termsCB;
    RadioButton fetsuRB;
    RadioGroup rg;
    Spinner departmentSpinner;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= 21)
            getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_STABLE | SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        changeStatusBarColor();


        FacebookSdk.sdkInitialize(this);


        pd = new ProgressDialog(LoginActivity.this);
        pd.setMessage("Signing you in...");

        phoneET = (EditText) findViewById(R.id.editTextPhone);
        yearOfPassingET = (EditText) findViewById(R.id.editTextYear);
        departmentSpinner = (Spinner) findViewById(R.id.departmentSpinner);
        fetsuRB = (RadioButton) findViewById(R.id.radioButtonFET);
        fetsuRB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                departmentSpinner.setVisibility(VISIBLE);
                departmentSpinner.setAdapter(getEngineeringDepartmentAdapter());
            }
        });
        rg = (RadioGroup) findViewById(R.id.radioGroup);

        promoCB = (CheckBox) findViewById(R.id.checkBoxPromo);
        termsCB = (CheckBox) findViewById(R.id.checkBoxTerms);

//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    PreferenceManager mPrefsManager = new PreferenceManager(LoginActivity.this);
//                    mPrefsManager.setUID(user.getUid());
//                    mPrefsManager.setEmail(user.getEmail());
//                    mPrefsManager.setPhotoURL(user.getPhotoUrl().toString());
//                    Log.d(TAG, "PHOTO URL: " + user.getPhotoUrl().toString());
//                    Log.d(TAG, "Sign in successful");
//                    Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
//                    LoginActivity.this.overridePendingTransition(0, 0);
//                    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("users");
//                    startActivity(i);
//                    mRef.child(user.getUid()).setValue(new PreferenceManager(LoginActivity.this).getUser())
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    finish();
//                                }
//                            });
//
//                }
//            }
//        };

        FloatingActionButton signInFab = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
//        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .requestProfile()
//                .requestId()
//                .build();
//
//
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
//                .build();
//
//        mGoogleApiClient.connect(); //Connect to the server

//        mAuth = FirebaseAuth.getInstance();
        signInFab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                savePrefs();
                //Code to sign in
            }
        });


    }

    private ArrayAdapter getEngineeringDepartmentAdapter() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Computer Science Engineering");
        list.add("Electronics and Communication Engineering");
        list.add("Information Technology Engineering");
        list.add("Instrumentation and Electronics Engineering");
        list.add("Mechanical Engineering");
        list.add("Chemical Engineering");

        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
        return mAdapter;
    }


    private void savePrefs() {

//        String university = universityET.getText().toString().trim();
        String yearOfPassing = yearOfPassingET.getText().toString().trim();
        String phoneNumber = phoneET.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNumber)) {
            phoneET.setError("REQUIRED");
//            Toast.makeText(this, "Please enter your phone number.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(yearOfPassing)) {
            yearOfPassingET.setError("REQUIRED");
            return;
        }


        if (phoneNumber.length() != 10) {
            Toast.makeText(this, "Phone Number should be 10 digits", Toast.LENGTH_SHORT).show();
            return;
        }
        String faculty;
        switch (rg.getCheckedRadioButtonId()) {
            case R.id.radioButtonFET:
                faculty = "FETSU";
                break;

            case R.id.radioButtonARTS:
                faculty = "AFSU";
                break;

            case R.id.radioButtonSC:
                faculty = "SFSU";
                break;

            default:
                Toast.makeText(this, "Please select your faculty", Toast.LENGTH_SHORT).show();
                return;
        }

        boolean isOptInForPromo = promoCB.isChecked();
        boolean isAgreeToTerms = termsCB.isChecked();

        if (!isAgreeToTerms) {
            termsCB.setError("ERROR");
            Toast.makeText(this, "You must agree to the terms before proceeding.", Toast.LENGTH_SHORT).show();
            return;
        }

        PreferenceManager mPrefsManager = new PreferenceManager(this);
        mPrefsManager.setPhoneNumber(phoneNumber);
        mPrefsManager.setFaculty(faculty);
        mPrefsManager.setUniversity("Jadavpur University");
//        mPrefsManager.set;
        mPrefsManager.setYear(yearOfPassing);
//        mPrefsManager.setGender(gender);
        mPrefsManager.setPromo(isOptInForPromo);

        signIn();

    }

    private void signIn() {

        AuthUI.IdpConfig facebookIdp = new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER)
                .setPermissions(Arrays.asList("public_profile", "email", "user_about_me", "user_birthday"))
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
//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            if (result.isSuccess()) {
//                // Google Sign In was successful, authenticate with Firebase
//                GoogleSignInAccount account = result.getSignInAccount();
//                firebaseAuthWithGoogle(account);
//            } else {
//                // Google Sign In failed, update UI appropriately
//                // ...
//                Toast.makeText(this, "Failed to sign in. Try later!", Toast.LENGTH_SHORT).show();
//            }
//        }

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                AccessToken token = AccessToken.getCurrentAccessToken();
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {

                    fetchGraphData(token);
//                mAuth.signInWithCustomToken(response);
//                startActivity(SignedInActivity.createIntent(this, response));

                    PreferenceManager mPrefsManager = new PreferenceManager(LoginActivity.this);
                    mPrefsManager.setUID(user.getUid());
                    mPrefsManager.setEmail(user.getEmail());
                    mPrefsManager.setPhotoURL(user.getPhotoUrl().toString());
                    Log.d(TAG, "PHOTO URL: " + user.getPhotoUrl().toString());
                    Log.d(TAG, "Sign in successful");
                    Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                    LoginActivity.this.overridePendingTransition(0, 0);
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
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        pd.show();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        pd.hide();
                        pd.dismiss();

                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (mAuthListener != null)
//            mAuth.removeAuthStateListener(mAuthListener);
//
//        mGoogleApiClient.disconnect();
//        if (pd.isShowing())
//            pd.dismiss(); //To prevent view leaking
//

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            AccessToken token = AccessToken.getCurrentAccessToken();

            fetchGraphData(token);

            PreferenceManager mPrefsManager = new PreferenceManager(LoginActivity.this);
            mPrefsManager.setUID(user.getUid());
            mPrefsManager.setEmail(user.getEmail());
//            mPrefsManager.setPhotoURL(user.getPhotoUrl().toString());
            Log.d(TAG, "PHOTO URL: " + user.getPhotoUrl().toString());
            Log.d(TAG, "Sign in successful");
            Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
            LoginActivity.this.overridePendingTransition(0, 0);
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
                                    PreferenceManager mPrefsManager = new PreferenceManager(LoginActivity.this);
                                    String link = object.getString("link");
                                    String gender = object.getString("gender");
                                    JSONObject profilePhoto = object.getJSONObject("picture");
                                    JSONObject data = profilePhoto.getJSONObject("data");
                                    String photoURL = data.getString("url");

                                    JSONObject cover = object.getJSONObject("cover");
                                    String coverURL = cover.getString("source");

//                                    String name = object.getString("name");

                                    mPrefsManager.setLink(link);
                                    mPrefsManager.setGender(gender);
                                    mPrefsManager.setCoverURL(coverURL);
                                    mPrefsManager.setPhotoURL(photoURL);

                                    FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).setValue(new PreferenceManager(LoginActivity.this).getUser())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    finish();
                                                }
                                            });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "gender, email, link, cover, picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();
            }
        }
    }
}
