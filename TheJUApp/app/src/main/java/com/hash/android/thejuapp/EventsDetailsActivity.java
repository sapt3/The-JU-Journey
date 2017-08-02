package com.hash.android.thejuapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hash.android.thejuapp.HelperClass.PreferenceManager;
import com.hash.android.thejuapp.Model.Event;
import com.hash.android.thejuapp.Model.User;
import com.hash.android.thejuapp.adapter.ContactRecyclerAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EventsDetailsActivity extends AppCompatActivity {

    private static final String TAG = EventsDetailsActivity.class.getSimpleName();
    SharedPreferences mPrefs;
    private ContactRecyclerAdapter mAdapter;
    private ArrayList<User> mArrayListUser = new ArrayList<>();
    private TextView contactUsTextView;
    private boolean isRegistered;
    private Button registerButton;
    private String key;
    private PreferenceManager mPrefsManager;
    private boolean isRegistrationEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Event Details");

        mPrefsManager = new PreferenceManager(this);
        mPrefs = getSharedPreferences("sharedPrefs", 0);

        TextView detailsTextView = (TextView) findViewById(R.id.eventDetailsTextView);
        TextView eventNameTextView = (TextView) findViewById(R.id.eventsNameTextView);
        TextView organisationTextView = (TextView) findViewById(R.id.eventsOrganisationTextView);
        TextView eventsDateTextView = (TextView) findViewById(R.id.eventsDateTextView);

        registerButton = (Button) findViewById(R.id.registerButton);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/opensans.ttf");
        detailsTextView.setTypeface(typeface);
        detailsTextView.setTextSize(17f);

        final Event event = getIntent().getParcelableExtra("EVENT");
        key = getIntent().getStringExtra("KEY");

        Date endDate = new Date(event.endDate);
        isRegistrationEnabled = !new Date().after(endDate);

        isRegistered = mPrefs.getBoolean(key, false);

        eventNameTextView.setText(event.event);
        organisationTextView.setText(event.organisation);


        Date startDate = new Date(event.startDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);

        String month = new SimpleDateFormat("MMMM", Locale.getDefault()).format(startDate);
        int date = cal.get(Calendar.DATE);
        String formattedMonth = month.substring(0, 3).toUpperCase();
        String formattedDate = date + " " + formattedMonth;

        eventsDateTextView.setText(formattedDate);

        RecyclerView contactRecyclerView = (RecyclerView) findViewById(R.id.contactRecyclerViewEvent);
        contactRecyclerView.setHasFixedSize(true);
        contactRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ContactRecyclerAdapter(mArrayListUser);
        contactRecyclerView.setAdapter(mAdapter);
        contactRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        contactRecyclerView.setNestedScrollingEnabled(false);
        contactUsTextView = (TextView) findViewById(R.id.contactUsTextView2);
        updateList(event);


        if (event.longDesc != null) {
            if (Build.VERSION.SDK_INT >= 24) {
                detailsTextView.setText(Html.fromHtml(event.longDesc, Html.FROM_HTML_MODE_LEGACY)); // for 24 api and more
            } else {
                detailsTextView.setText(Html.fromHtml(event.longDesc)); // or for older api
            }
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButtonCalendar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEvent(event.event, event.startDate, event.endDate);
            }
        });

        if (isRegistrationEnabled) {
            if (isRegistered) registerButton.setText(R.string.unregister);
            else registerButton.setText(R.string.register);

            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (isRegistered) {
                        unregister();
                    } else register();

                }
            });
        } else {
            registerButton.setEnabled(false);
            registerButton.setFocusable(false);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void unregister() {
        isRegistered = false;
        mPrefs.edit().putBoolean(key, false).commit();
        registerButton.setText(R.string.register);
        Log.d(TAG, "Unregistering user" + key);
        FirebaseDatabase.getInstance().getReference("events").child(key).child("registeredUsers").child(mPrefsManager.getUID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) registerButton.setText(R.string.unregister);
                if (task.isSuccessful())
                    Snackbar.make(findViewById(R.id.root), "Un-Registered successfully", Snackbar.LENGTH_SHORT)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    register();
                                }
                            })
                            .setActionTextColor(ContextCompat.getColor(EventsDetailsActivity.this, R.color.colorAccent))
                            .show();

            }
        });
        FirebaseDatabase.getInstance().getReference("users").child(mPrefsManager.getUID()).child("registeredEvents").child(key).removeValue();
        FirebaseDatabase.getInstance().getReference("finalEvents").child(key).child("registeredUsers").child(mPrefsManager.getUID()).removeValue();

    }

    private void register() {
        isRegistered = true;
        Log.d(TAG, "Registering user" + key);
        mPrefs.edit().putBoolean(key, true).commit();
        registerButton.setText(R.string.unregister);

        FirebaseDatabase.getInstance().getReference("events").child(key).child("registeredUsers").child(mPrefsManager.getUID()).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) registerButton.setText(R.string.register);
                if (task.isSuccessful())
                    Snackbar.make(findViewById(R.id.root), "Registered successfully", Snackbar.LENGTH_SHORT)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    unregister();
                                }
                            })
                            .setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .show();
            }

        });

        FirebaseDatabase.getInstance().getReference("users").child(new PreferenceManager(this).getUID()).child("registeredEvents").child(key).setValue(true);
        FirebaseDatabase.getInstance().getReference("finalEvents").child(key).child("registeredUsers").child(mPrefsManager.getUID()).setValue(mPrefsManager.getUser());

    }

    public void addEvent(String title, long begin, long end) {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    private void updateList(Event event) {
        mArrayListUser.clear();
        String uids = event.contact;
        if (uids != null && uids.length() > 0) {
            String[] uid = uids.split(",");
            new FindUserList().execute(uid); //Do in background
        } else {
            contactUsTextView.setVisibility(View.GONE);
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class FindUserList extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(String... strings) {
            for (final String string : strings) {
                //TODO: Index data properly
                String formattedString = string.trim();
                Query mRef = FirebaseDatabase.getInstance().getReference("users").orderByChild("phoneNumber").equalTo(formattedString);
                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot shot : dataSnapshot.getChildren()) {
                            try {
                                User user = shot.getValue(User.class);
                                mArrayListUser.add(user);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            return null;
        }
    }


}
