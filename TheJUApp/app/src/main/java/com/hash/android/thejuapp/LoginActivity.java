package com.hash.android.thejuapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.hash.android.thejuapp.HelperClass.PreferenceManager;

import java.util.ArrayList;
import java.util.Arrays;

import static android.view.View.OnClickListener;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
import static android.view.View.VISIBLE;

public class LoginActivity extends AppCompatActivity {


    private static final int RC_SIGN_IN = 0;
    private static final String TAG = LoginActivity.class.getSimpleName();
    EditText phoneET, yearOfPassingET, emailET;
    CheckBox promoCB, termsCB;
    RadioButton fetsuRB, artsRB, scienceRB;
    RadioGroup rg;
    Spinner departmentSpinner;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog pd;
    private boolean isDepartmentSelected = false;
    private String department;
    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= 21)
            getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_STABLE | SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        changeStatusBarColor();

        email = getIntent().getStringExtra("email");

        pd = new ProgressDialog(LoginActivity.this);
        pd.setMessage("Signing you in...");

        phoneET = (EditText) findViewById(R.id.editTextPhone);
        yearOfPassingET = (EditText) findViewById(R.id.editTextYear);
        departmentSpinner = (Spinner) findViewById(R.id.departmentSpinner);
        fetsuRB = (RadioButton) findViewById(R.id.radioButtonFET);
        artsRB = (RadioButton) findViewById(R.id.radioButtonARTS);
        scienceRB = (RadioButton) findViewById(R.id.radioButtonSC);
        emailET = (EditText) findViewById(R.id.emailIdEditText);
        fetsuRB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                departmentSpinner.setVisibility(VISIBLE);
                departmentSpinner.setAdapter(getEngineeringDepartmentAdapter());
            }
        });
        artsRB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                departmentSpinner.setVisibility(VISIBLE);
                departmentSpinner.setAdapter(getArtsDepartmentAdapter());
            }
        });
        scienceRB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                departmentSpinner.setVisibility(VISIBLE);
                departmentSpinner.setAdapter(getScienceDepartmentAdapter());
            }
        });

        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "Department:: " + adapterView.getItemAtPosition(i));
                department = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        rg = (RadioGroup) findViewById(R.id.radioGroup);

        promoCB = (CheckBox) findViewById(R.id.checkBoxPromo);
        termsCB = (CheckBox) findViewById(R.id.checkBoxTerms);

        if (email != null) {
            emailET.setText(email);
            emailET.setEnabled(false);
            emailET.setFocusable(false);
        }

        FloatingActionButton signInFab = (FloatingActionButton) findViewById(R.id.floatingActionButton2);

        signInFab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                savePrefs();
                //Code to sign in
            }
        });


    }

    private SpinnerAdapter getArtsDepartmentAdapter() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Department of Bengali");
        list.add("Department of Comparative Literature");
        list.add("Department of Economics");
        list.add("Department of Education");
        list.add("Department of English");
        list.add("Department of Film Studies");
        list.add("Department of History");
        list.add("Department of International Relations");
        list.add("Department of Library & Information Science");
        list.add("Department of Philosophy");
        list.add("Department of Physical Education");
        list.add("Department of Sanskrit");
        list.add("Department of Sociology");

        return new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
    }

    private SpinnerAdapter getScienceDepartmentAdapter() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Department of Chemistry");
        list.add("Department of Geography");
        list.add("Department of Geological sciences");
        list.add("Department of Instrumentation Science");
        list.add("Department of Life science & Bio-technology");
        list.add("Department of Mathematics");
        list.add("Department of Physics");

        return new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
    }

    private ArrayAdapter getEngineeringDepartmentAdapter() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Architecture");
        list.add("Chemical Engineering");
        list.add("Civil Engineering");
        list.add("Computer Science & Engineering");
        list.add("Construction Engineering");
        list.add("Electrical Engineering");
        list.add("Electronics & Telecommunication Engineering");
        list.add("Food Technology & Bio-Chemical Engineering");
        list.add("Information Technology");
        list.add("Instrumentation & Electronics Engineering");
        list.add("Metallurgical & Material Engineering");
        list.add("Mechanical Engineering");
        list.add("Pharmaceutical Technology");
        list.add("Power Engineering");
        list.add("Printing Engineering");
        list.add("Production Engineering");

        return new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
    }


    private void savePrefs() {

//        String university = universityET.getText().toString().trim();
        String yearOfPassing = yearOfPassingET.getText().toString().trim();
        String phoneNumber = phoneET.getText().toString().trim();
        String emailId = emailET.getText().toString().trim();

        if (TextUtils.isEmpty(phoneNumber)) {
            phoneET.setError("Required");
//            Toast.makeText(this, "Please enter your phone number.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(emailId) && emailET.isEnabled()) {
            emailET.setError("Required");
            return;
        }

        if (TextUtils.isEmpty(yearOfPassing)) {
            yearOfPassingET.setError("Required");
            return;
        }


        if (phoneNumber.length() != 10 && TextUtils.isDigitsOnly(phoneNumber)) {
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

        if (department == null) {
            Toast.makeText(this, "Select your department", Toast.LENGTH_SHORT).show();
            return;
        }


        if (!isAgreeToTerms) {
            termsCB.setError("ERROR");
            Toast.makeText(this, "You must agree to the terms before proceeding.", Toast.LENGTH_SHORT).show();
            return;
        }

        PreferenceManager mPrefsManager = new PreferenceManager(this);
        mPrefsManager.setPhoneNumber(phoneNumber);
        mPrefsManager.setFaculty(faculty);
        mPrefsManager.setEmail(emailId);
        mPrefsManager.setDepartment(department);
        mPrefsManager.setUniversity("Jadavpur University");
        mPrefsManager.setYear(yearOfPassing);
        mPrefsManager.setPromo(isOptInForPromo);

        startActivity(new Intent(this, DashboardActivity.class));

        mPrefsManager.setFlowCompleted(true);
//        signIn();

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


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == RC_SIGN_IN) {
//            IdpResponse response = IdpResponse.fromResultIntent(data);
//
//            // Successfully signed in
//            if (resultCode == ResultCodes.OK) {
//                AccessToken token = AccessToken.getCurrentAccessToken();
//                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//                if (user != null) {
//
//                    fetchGraphData(token);
//                    PreferenceManager mPrefsManager = new PreferenceManager(LoginActivity.this);
//                    mPrefsManager.setUID(user.getUid());
//                    mPrefsManager.setEmail(user.getEmail());
//                    mPrefsManager.setPhotoURL(user.getPhotoUrl().toString());
//                    Log.d(TAG, "PHOTO URL: " + user.getPhotoUrl().toString());
//                    Log.d(TAG, "Sign in successful");
//                    Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
//                    LoginActivity.this.overridePendingTransition(0, 0);
//                    startActivity(i);
//
//
//                } else {
//                    Toast.makeText(this, "Failed to sign in", Toast.LENGTH_SHORT).show();
//
//                }
////                finish();
//                return;
//            } else {
//                // Sign in failed
//                if (response == null) {
//                    // User pressed back button
//                    Toast.makeText(this, "Sign in cancelled!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
//                    Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
//                    Toast.makeText(this, "Unknown Error", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//            }
//
//            Toast.makeText(this, "Unknown Error", Toast.LENGTH_SHORT).show();
//        }
//    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        final FirebaseUser user = auth.getCurrentUser();
//        if (user != null) {
//
//            Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
//            LoginActivity.this.overridePendingTransition(0, 0);
////            startActivity(i);
//
//        }

//    private void fetchGraphData(AccessToken token) {
//        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            if (token != null) {
//                GraphRequest request = GraphRequest.newMeRequest(
//                        AccessToken.getCurrentAccessToken(),
//                        new GraphRequest.GraphJSONObjectCallback() {
//                            @Override
//                            public void onCompleted(JSONObject object, GraphResponse response) {
//                                try {
//                                    Log.d(TAG, "object:: " + object.toString());
//                                    PreferenceManager mPrefsManager = new PreferenceManager(LoginActivity.this);
//                                    String link = object.getString("link");
//                                    String gender = object.getString("gender");
//                                    JSONObject profilePhoto = object.getJSONObject("picture");
//                                    JSONObject data = profilePhoto.getJSONObject("data");
//                                    String photoURL = data.getString("url");
//
//                                    JSONObject cover = object.getJSONObject("cover");
//                                    String coverURL = cover.getString("source");
//
////                                    String name = object.getString("name");
//
//                                    mPrefsManager.setLink(link);
//                                    mPrefsManager.setGender(gender);
//                                    mPrefsManager.setCoverURL(coverURL);
//                                    mPrefsManager.setPhotoURL(photoURL);
//
//                                    FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).setValue(new PreferenceManager(LoginActivity.this).getUser())
//                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    finish();
//                                                }
//                                            });
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//                Bundle parameters = new Bundle();
//                parameters.putString("fields", "gender, email, link, cover, picture.type(large)");
//                request.setParameters(parameters);
//                request.executeAsync();
//            }
//        }
//    }
    }
}