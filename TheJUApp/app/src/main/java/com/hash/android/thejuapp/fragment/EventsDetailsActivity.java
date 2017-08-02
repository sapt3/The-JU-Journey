package com.hash.android.thejuapp.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hash.android.thejuapp.Model.Event;
import com.hash.android.thejuapp.Model.User;
import com.hash.android.thejuapp.R;
import com.hash.android.thejuapp.adapter.ContactRecyclerAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EventsDetailsActivity extends AppCompatActivity {

    private static final String TAG = EventsDetailsActivity.class.getSimpleName();
    private ContactRecyclerAdapter mAdapter;
    private ArrayList<User> mArrayListUser = new ArrayList<>();
    private TextView contactUsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Event Details");


        TextView detailsTextView = (TextView) findViewById(R.id.eventDetailsTextView);
        TextView eventNameTextView = (TextView) findViewById(R.id.eventsNameTextView);
        TextView organisationTextView = (TextView) findViewById(R.id.eventsOrganisationTextView);
        TextView eventsDateTextView = (TextView) findViewById(R.id.eventsDateTextView);
        Button addToCalendar = (Button) findViewById(R.id.addToCalendarButton);


        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/opensans.ttf");
        detailsTextView.setTypeface(typeface);
        detailsTextView.setTextSize(17f);

        final Event event = getIntent().getParcelableExtra("EVENT");

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


        if (event.longDesc != null)
            detailsTextView.setText(event.longDesc);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButtonCalendar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEvent(event.event, event.startDate, event.endDate);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                                Log.d(TAG, user.getName());
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
