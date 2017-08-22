package com.hash.android.thejuapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hash.android.thejuapp.Model.Event;
import com.hash.android.thejuapp.Model.Feed;
import com.hash.android.thejuapp.Utils.PreferenceManager;

import static com.hash.android.thejuapp.adapter.FeedRecyclerAdapter.INTENT_EXTRA_FEED;

public class WelcomeActivity extends AppCompatActivity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private ImageView imgView;
    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private int[] layouts;
    private Button btnSkip, btnNext;

    @Override
    protected void onStart() {
        super.onStart();
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!new PreferenceManager(this).isFirstTimeLaunch()) {
            handleIntent(getIntent());
            startActivity(new Intent(this, FacebookLogin.class));
            WelcomeActivity.this.overridePendingTransition(0, 0);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_welcome);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);
        imgView = (ImageView) findViewById(R.id.imageView2);

        layouts = new int[]{
                R.layout.welcome_screen_1,
                R.layout.welcome_screen_2,
                R.layout.welcome_screen_3,
                R.layout.welcome_screen_4
        };

        addBottomDots(0);
        changeStatusBarColor();

        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
                if (position == layouts.length - 1) {
                    // last page. make button text to GOT IT
                    btnNext.setVisibility(View.VISIBLE);
                    btnNext.setText(getString(R.string.start));
                    btnSkip.setVisibility(View.GONE);
                    imgView.setVisibility(View.INVISIBLE);
                } else {
                    // still pages are left
                    imgView.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.INVISIBLE);
                    btnNext.setText(getString(R.string.next));
                    btnSkip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });
        // ATTENTION: This was auto-generated to handle app links.

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent appLinkIntent) {
        Uri appLinkData = appLinkIntent.getData();
        if (appLinkData != null && FirebaseAuth.getInstance().getCurrentUser() != null && new PreferenceManager(this).isFlowCompleted()) {
            String type = appLinkData.getPathSegments().get(0);
            String key = appLinkData.getLastPathSegment();
            if (key != null) {
                switch (type) {
                    case "posts":
                        FirebaseDatabase.getInstance().getReference("posts").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                try {
                                    if (dataSnapshot.getChildrenCount() == 0) {
                                        launchHomeScreen();
                                        Toast.makeText(WelcomeActivity.this, "Invalid post.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        String key = dataSnapshot.getKey();
                                        Feed feed = dataSnapshot.getValue(Feed.class);
                                        Intent i = new Intent(getApplicationContext(), FacebookLogin.class);
                                        if (FirebaseAuth.getInstance().getCurrentUser() != null && new PreferenceManager(WelcomeActivity.this).isFlowCompleted()) {
                                            i = new Intent(getApplicationContext(), DetailsFeedActivity.class);
                                            i.putExtra(INTENT_EXTRA_FEED, feed);
                                            i.putExtra(Intent.EXTRA_TEXT, key);
                                        }
                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(i);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        break;

                    case "events":
                        FirebaseDatabase.getInstance().

                                getReference("events").

                                child(key).

                                addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        try {
                                            if (dataSnapshot.getChildrenCount() == 0) {
                                                launchHomeScreen();
                                                Toast.makeText(WelcomeActivity.this, "Invalid event.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                String key = dataSnapshot.getKey();
                                                Event event = dataSnapshot.getValue(Event.class);
                                                Intent i = new Intent(getApplicationContext(), FacebookLogin.class);
                                                if (FirebaseAuth.getInstance().getCurrentUser() != null && new PreferenceManager(WelcomeActivity.this).isFlowCompleted()) {
                                                    i = new Intent(getApplicationContext(), EventsDetailsActivity.class);
                                                    i.putExtra("KEY", key);
                                                    i.putExtra("EVENT", event);
                                                    i.putExtra(Intent.EXTRA_TEXT, key);
                                                }
                                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                startActivity(i);
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                        break;
                    default:

                }

            }
        }
    }

    private void launchHomeScreen() {
        PreferenceManager prefManager = new PreferenceManager(this);
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(WelcomeActivity.this, FacebookLogin.class));
        finish();
    }

    private void addBottomDots(int currentPage) {
        TextView[] dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }


    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
