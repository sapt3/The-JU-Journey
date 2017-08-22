package com.hash.android.thejuapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
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

import com.hash.android.thejuapp.Utils.PreferenceManager;

import java.util.ArrayList;

import static android.view.View.OnClickListener;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
import static android.view.View.VISIBLE;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    EditText phoneET, yearOfPassingET, emailET;
    CheckBox promoCB, termsCB;
    RadioButton fetsuRB, artsRB, scienceRB;
    RadioGroup rg;
    Spinner departmentSpinner;
    private String department;
    private PreferenceManager mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= 21)
            getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_STABLE | SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        changeStatusBarColor();

        mPrefs = new PreferenceManager(this);
        String email = mPrefs.getEmail();


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
                department = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        rg = (RadioGroup) findViewById(R.id.radioGroup);

        promoCB = (CheckBox) findViewById(R.id.checkBoxPromo);
        termsCB = (CheckBox) findViewById(R.id.checkBoxTerms);

        try {
            if (!TextUtils.isEmpty(email)) {
                emailET.setText(email);
                emailET.setEnabled(false);
                emailET.setFocusable(false);
            } else {
                emailET.setEnabled(true);
                emailET.setFocusable(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String phoneNumber = mPrefs.getPhoneNumber();
        if (phoneNumber != null) {
            phoneET.setText(phoneNumber);
            phoneET.setEnabled(false);
            phoneET.setFocusable(false);
        }
        FloatingActionButton signInFab = (FloatingActionButton) findViewById(R.id.floatingActionButton2);

        signInFab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                savePrefs();
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
            return;
        }

        if (TextUtils.isEmpty(emailId) && emailET.isEnabled()) {
            emailET.setError("Required");
            return;
        }

        if (TextUtils.isEmpty(yearOfPassing) || yearOfPassing.length() != 4) {
            yearOfPassingET.setError("Enter a valid year of joining.");
            return;
        }

        if (phoneNumber.length() != 10 && TextUtils.isDigitsOnly(phoneNumber)) {
            Toast.makeText(this, "Phone Number should be 10 digits", Toast.LENGTH_SHORT).show();
            return;
        }
        String faculty;
        switch (rg.getCheckedRadioButtonId()) {
            case R.id.radioButtonFET:
                faculty = "Engineering";
                break;

            case R.id.radioButtonARTS:
                faculty = "Arts";
                break;

            case R.id.radioButtonSC:
                faculty = "Science";
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
            termsCB.setError("Error");
            Toast.makeText(this, "You must agree to the terms before proceeding.", Toast.LENGTH_SHORT).show();
            return;
        }

        mPrefs.setPhoneNumber(phoneNumber);
        mPrefs.setFaculty(faculty);
        mPrefs.setEmail(emailId);
        mPrefs.setDepartment(department);
        mPrefs.setUniversity("Jadavpur University");
        mPrefs.setYear(yearOfPassing);
        mPrefs.setPromo(isOptInForPromo);
        mPrefs.saveUser();
        mPrefs.setFlowCompleted(true);

        startActivity(new Intent(this, DashboardActivity.class)); //move to dashboard activity
        finish();

    }


    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }


}