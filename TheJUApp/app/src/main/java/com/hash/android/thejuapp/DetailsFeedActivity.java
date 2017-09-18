package com.hash.android.thejuapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.hash.android.thejuapp.Model.Feed;
import com.hash.android.thejuapp.Model.User;
import com.hash.android.thejuapp.Utils.PreferenceManager;
import com.hash.android.thejuapp.adapter.ContactRecyclerAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

import static com.hash.android.thejuapp.adapter.FeedRecyclerAdapter.INTENT_EXTRA_FEED;

public class DetailsFeedActivity extends AppCompatActivity {

    private static final String TAG = DetailsFeedActivity.class.getSimpleName();
    private static boolean isBookmarked = false;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private final ArrayList<User> mArrayListUser = new ArrayList<>();
    TextView contactUsTextView;
    private TextView favouritesTextView;
    private final ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            try {
                int count = dataSnapshot.getValue(Integer.class);
                Log.d(TAG, "onDataChange ::" + count);
                favouritesTextView.setText(count + " Favourites");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    private DatabaseReference starPostRef;
    private DatabaseReference globalPostRef;
    private String uid;
    private Feed feed;
    private DatabaseReference bookmarksRef;
    private ContactRecyclerAdapter mAdapter;

    @Override
    protected void onPause() {
        super.onPause();
        starPostRef.removeEventListener(valueEventListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feed_menu, menu);
        return true;
//        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem item = menu.getItem(0);
        ValueEventListener bookmarksEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean isFav = (Boolean) dataSnapshot.getValue();
                if (isFav != null) {
                    isBookmarked = true;
                    VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(getResources(), R.drawable.ic_bookmark_white_24px, null);
                    item.setIcon(vectorDrawableCompat);
                } else {
                    isBookmarked = false;
                    VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(getResources(), R.drawable.ic_bookmark_border_white_24px, null);
                    item.setIcon(vectorDrawableCompat);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        bookmarksRef.addValueEventListener(bookmarksEventListener);


        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_details_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        Intent i = getIntent();
        feed = i.getParcelableExtra(INTENT_EXTRA_FEED);

        String key = i.getStringExtra(Intent.EXTRA_TEXT);

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());

        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            TextView timeTextView = (TextView) findViewById(R.id.dateTimeTextViewDetails);
            long timeNow = sdf.parse(feed.getTime()).getTime();
            Date date = new Date(timeNow);
//            String dateDisplay = new SimpleDateFormat("dd", Locale.getDefault()).format(date);
//            String month = new SimpleDateFormat("MMMM", Locale.getDefault()).format(date);
            String time = new SimpleDateFormat("dd MMMM, YYYY", Locale.getDefault()).format(date);
            timeTextView.setText(time);

        } catch (Exception e) {
            e.printStackTrace();
        }

        RecyclerView contactRecyclerView = (RecyclerView) findViewById(R.id.contactRecyclerView);
        contactRecyclerView.setHasFixedSize(true);
        contactRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ContactRecyclerAdapter(mArrayListUser);
        contactRecyclerView.setAdapter(mAdapter);
        contactRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        contactRecyclerView.setNestedScrollingEnabled(false);
        contactUsTextView = (TextView) findViewById(R.id.contactUsTextView2);

        updateList();

        TextView tx = (TextView) findViewById(R.id.contentTextView);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/opensans.ttf");

        //COMPLETED: REPLACE THIS WITH ORIGINAL CONTENT.
        //tx.setText(feed.getLongDesc());

        tx.setText(Html.fromHtml(feed.getLongDesc()));

        tx.setTypeface(custom_font);

        ImageView feedImage = (ImageView) findViewById(R.id.feedImageView);
        TextView heading = (TextView) findViewById(R.id.headingTextView);
        TextView author = (TextView) findViewById(R.id.authorTextView);
        favouritesTextView = (TextView) findViewById(R.id.favouritesTextView);

        starPostRef = FirebaseDatabase.getInstance().getReference().child("posts").child(key).child("starCount");
        globalPostRef = FirebaseDatabase.getInstance().getReference().child("posts").child(key);
        favouritesTextView.setText("");
        starPostRef.addValueEventListener(valueEventListener);


        if (!TextUtils.isEmpty(feed.getImageURL()))
        Glide.with(this)
                .load(feed.getImageURL())
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(feedImage);

        else feedImage.setImageResource(R.drawable.banner);

        heading.setText(feed.getHeading());
        author.setText(feed.getAuthor());
        try {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        uid = new PreferenceManager(this).getUID();
        bookmarksRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("bookmarks").child(key).getRef();


        final SharedPreferences.Editor editor = getSharedPreferences("myPrefs", 0).edit();
        boolean hasShown = getSharedPreferences("myPrefs", 0).getBoolean("hasShown", false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton3);
        if (!hasShown) {
            new MaterialTapTargetPrompt.Builder(this)
                    .setTarget(fab)
                    .setPrimaryText("Like this article?")
                    .setBackButtonDismissEnabled(true)
                    .setAutoFinish(true)
                    .setFocalColour(ContextCompat.getColor(DetailsFeedActivity.this, R.color.prompt))
                    .setSecondaryText("Favourite this article by clicking here.")
                    .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                        @Override
                        public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state) {
                            if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_DISMISSED) {
                                editor.putBoolean("hasShown", true);
                                editor.apply();
                                // User has pressed the prompt target
                            }
                        }
                    })
                    .show();

        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStarClicked(globalPostRef);
            }
        });
    }

    private void updateList() {
        mArrayListUser.clear();
        String uids = feed.contact;
        if (uids != null && uids.length() > 0) {
            String[] uid = uids.split(",");
            new FindUserList().execute(uid); //Do in background
        } else {
            contactUsTextView.setVisibility(View.GONE);
        }

        mAdapter.notifyDataSetChanged();
    }

    private void onStarClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                final Feed p = mutableData.getValue(Feed.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.stars.containsKey(uid)) {
                    // Unstar the post and remove self from stars
                    p.starCount = p.starCount - 1;
                    p.stars.remove(uid);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            favouritesTextView.setText(p.starCount + " Favourites");
                        }
                    });
                } else {
                    // Star the post and add self to stars
                    p.starCount = p.starCount + 1;
                    p.stars.put(uid, true);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            favouritesTextView.setText(p.starCount + " Favourites");
                        }
                    });
                }

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.bookmarkMenu) {
            saveToDatabase();
        } else if (item.getItemId() == R.id.shareMenu) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, feed.getHeading() + "\n" + "By " + feed.getAuthor() + "-  \n"
                    + "\n\n" + feed.getShortDesc() + "\n" + "To know more download the app at the Google Play Store\n");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

        }
        return super.onOptionsItemSelected(item);

    }

    private void saveToDatabase() {
        if (isBookmarked) {
            bookmarksRef.removeValue();
        } else {
            bookmarksRef.setValue(true);
        }
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
                //COMPLETED: Index data properly
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
