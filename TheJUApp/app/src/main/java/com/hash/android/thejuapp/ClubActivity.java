package com.hash.android.thejuapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.hash.android.thejuapp.Model.Feed;
import com.hash.android.thejuapp.ViewHolder.FeedHolder;
import com.hash.android.thejuapp.adapter.EventsPagerAdapter;

public class ClubActivity extends AppCompatActivity {

    public final static int ITEM_COUNT = 3;
    private static final String TAG = ClubActivity.class.getSimpleName();

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FirebaseRecyclerAdapter<Feed, FeedHolder> mAdapter;
    private String journal = "https://firebasestorage.googleapis.com/v0/b/the-ju-app.appspot.com/o/club_logo%2Fjournal%20logo%20(Small).jpg?alt=media&token=2c5eba1a-8f55-43ec-8912-4b5373f4bb67";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs_club_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        View x = inflater.inflate(R.layout.tabs_event_layout, null);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.vpPager);

        String club = getIntent().getStringExtra("club");

        if (club == null) {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        } else {
//            setTitle(club.clubName);
            viewPager.setAdapter(new EventsPagerAdapter(getSupportFragmentManager(), club));

//            TextView clubTag = (TextView) findViewById(R.id.clubTagTextView);
//            TextView clubName = (TextView) findViewById(R.id.clubNameTextView);
//            ImageView clubLogo = (ImageView) findViewById(R.id.clubImageView);
//
//            clubName.setText((club.clubName != null) ? club.clubName : "The JU Journal");
//            clubTag.setText((club.clubTag != null) ? club.clubTag : "@jujournal");
//
//            Resources r = getResources();
//            px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, r.getDisplayMetrics());
//

//
//            RecyclerView feedRecyclerView = (RecyclerView) findViewById(R.id.clubRecyclerView);
//            feedRecyclerView.setNestedScrollingEnabled(false);
////            feedRecyclerView.setHasFixedSize(true);
//            LinearLayoutManager manager = new LinearLayoutManager(this);
//            manager.setReverseLayout(true);
//            manager.setStackFromEnd(true);
//            feedRecyclerView.setLayoutManager(manager);
//            feedRecyclerView.setItemAnimator(new DefaultItemAnimator());
//            feedRecyclerView.addItemDecoration(new DividerItemDecoration(feedRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
//            //2017-06-19T14:13:24.100Z
//            final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
//            Query ref = mRef.child("posts").orderByChild("club").equalTo(club.clubTag);
//            ref.keepSynced(true);
//            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
//            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
//            String uid = new PreferenceManager(this).getUID();
//
//
//            mAdapter = new FirebaseRecyclerAdapter<Feed, FeedHolder>(
//                    Feed.class,
//                    R.layout.recycler_child_new_feed,
//                    FeedHolder.class,
//                    ref) {
//                @Override
//                protected void populateViewHolder(final FeedHolder viewHolder, Feed model, int position) {
////                    progressBar.setVisibility(View.GONE);
//                    viewHolder.setAd(false);
//                    viewHolder.setLogo((model.getLogo() != null) ? model.getLogo() : journal, ClubActivity.this);
//                    viewHolder.setTag((model.getClub() != null) ? model.getClub() : "@jujournal");
//                    Log.d(TAG, "populateViewHolder:: " + model.getHeading());
//                    viewHolder.setAuthor(model.getAuthor());
//                    if (model.getImageURL().length() > 0) {
//                        viewHolder.image.requestLayout();
//                        viewHolder.image.getLayoutParams().height = (int) px;
//                        viewHolder.image.setVisibility(View.VISIBLE);
//                        viewHolder.setImage(model.getImageURL(), ClubActivity.this);
//                    } else viewHolder.image.setVisibility(View.GONE);
//                    viewHolder.setHeading(model.getHeading());
//                    viewHolder.setShortDesc(model.getShortDesc());
//                    viewHolder.setTime(model.getTime(), sdf);
//                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            int position = viewHolder.getAdapterPosition();
//                            FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(ClubActivity.this);
//                            Bundle bundle = new Bundle();
//                            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, mAdapter.getRef(position).getKey());
//                            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, mAdapter.getItem(position).getHeading());
//                            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "feed");
//                            analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
//                            Log.d(TAG, "onClick::" + position);
//                            Intent i = new Intent(ClubActivity.this, DetailsFeedActivity.class);
//                            i.putExtra(INTENT_EXTRA_FEED, mAdapter.getItem(position));
//                            i.putExtra(Intent.EXTRA_TEXT, mAdapter.getRef(position).getKey());
//                            Pair<View, String> pair1 = Pair.create(view.findViewById(R.id.postImageView), "sharedImage");
//                            ActivityOptionsCompat optionsCompat = makeSceneTransitionAnimation(ClubActivity.this, pair1);
//                            startActivity(i, optionsCompat.toBundle());
//                        }
//                    });
//
//                }
//
//                @Override
//                public FeedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                    FeedHolder viewHolder = super.onCreateViewHolder(parent, viewType);
//                    viewHolder.image.setVisibility(View.GONE);
//                    return viewHolder;
//                }
//            };
//
//            mAdapter.notifyDataSetChanged();
//            feedRecyclerView.setAdapter(mAdapter);
            tabLayout.post(new Runnable() {
                @Override
                public void run() {
                    tabLayout.setupWithViewPager(viewPager);
                }
            });

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()

            {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });


        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

}



