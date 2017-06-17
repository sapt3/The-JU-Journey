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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hash.android.thejuapp.HelperClass.PreferenceManager;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 0;
    private static final String TAG = LoginActivity.class.getSimpleName();
    GoogleSignInOptions googleSignInOptions;
    GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    EditText phoneET, universityET;
    RadioGroup genderRG;
    RadioButton maleRB, femaleRB;
    CheckBox promoCB, termsCB;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= 21)
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        changeStatusBarColor();


        pd = new ProgressDialog(LoginActivity.this);
        pd.setMessage("Signing you in...");

        phoneET = (EditText) findViewById(R.id.editTextPhone);
        universityET = (EditText) findViewById(R.id.editTextUniversity);
        genderRG = (RadioGroup) findViewById(R.id.radioGroup);
        maleRB = (RadioButton) findViewById(R.id.radioButtonMale);
        femaleRB = (RadioButton) findViewById(R.id.radioButtonFemale);

        promoCB = (CheckBox) findViewById(R.id.checkBoxPromo);
        termsCB = (CheckBox) findViewById(R.id.checkBoxTerms);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    PreferenceManager mPrefsManager = new PreferenceManager(LoginActivity.this);
                    mPrefsManager.setUID(user.getUid());
                    mPrefsManager.setEmail(user.getEmail());
                    mPrefsManager.setPhotoURL(user.getPhotoUrl().toString());
                    Log.d(TAG, "PHOTO URL: " + user.getPhotoUrl().toString());
                    Log.d(TAG, "Sign in successful");
                    Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                    LoginActivity.this.overridePendingTransition(0, 0);
                    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("users");
                    startActivity(i);
                    mRef.child(user.getUid()).setValue(new PreferenceManager(LoginActivity.this).getUser())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    finish();
                                }
                            });

                }
            }
        };

        FloatingActionButton signInFab = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .requestId()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        mGoogleApiClient.connect(); //Connect to the server

        mAuth = FirebaseAuth.getInstance();
        signInFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePrefs();
                //Code to sign in
            }
        });


    }

    private void savePrefs() {

        String university = universityET.getText().toString().trim();
        if (TextUtils.isEmpty(university)) {
            Toast.makeText(this, "Please enter your university.", Toast.LENGTH_SHORT).show();
            return;
        }

        String phoneNumber = phoneET.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNumber)) {

            Toast.makeText(this, "Please enter your phone number.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (phoneNumber.length() != 10) {
            Toast.makeText(this, "Phone Number should be 10 digits", Toast.LENGTH_SHORT).show();
            return;
        }
        String gender;
        switch (genderRG.getCheckedRadioButtonId()) {
            case R.id.radioButtonMale:
                gender = "M";
                break;

            case R.id.radioButtonFemale:
                gender = "F";
                break;

            default:
                Toast.makeText(this, "Please select your gender", Toast.LENGTH_SHORT).show();
                return;
        }

        boolean isOptInForPromo = promoCB.isChecked();
        boolean isAgreeToTerms = termsCB.isChecked();

        if (!isAgreeToTerms) {
            Toast.makeText(this, "You must agree to the terms before proceeding.", Toast.LENGTH_SHORT).show();
            return;
        }

        PreferenceManager mPrefsManager = new PreferenceManager(this);
        mPrefsManager.setPhoneNumber(phoneNumber);
        mPrefsManager.setUniversity(university);
        mPrefsManager.setGender(gender);
        mPrefsManager.setPromo(isOptInForPromo);

        signIn();

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
                Toast.makeText(this, "Failed to sign in. Try later!", Toast.LENGTH_SHORT).show();
            }
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
        if (mAuthListener != null)
            mAuth.removeAuthStateListener(mAuthListener);

        mGoogleApiClient.disconnect();
        if (pd.isShowing())
            pd.dismiss(); //To prevent view leaking


    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        mAuth.addAuthStateListener(mAuthListener);

    }
}
