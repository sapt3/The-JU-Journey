package com.hash.android.thejuapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.hash.android.thejuapp.HelperClass.PreferenceManager;
import com.hash.android.thejuapp.Model.Canteen;
import com.hash.android.thejuapp.adapter.CanteenListRecyclerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CanteenListActivity extends AppCompatActivity implements LocationListener {

    private static final String TAG = CanteenListActivity.class.getSimpleName();
    private static final String KEY_SURUCHI = "suruchi";
    private LocationManager locationManager;
    private String provider;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private Location location;
    private ArrayList<Canteen> mCanteenArrayList = new ArrayList<>();
    private CanteenListRecyclerAdapter adapter;


    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on {@link #requestPermissions(String[], int)}.
     * <p>
     * <strong>Note:</strong> It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     * </p>
     *
     * @param requestCode  The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either {@link PackageManager#PERMISSION_GRANTED}
     *                     or {@link PackageManager#PERMISSION_DENIED}. Never null.
     * @see #requestPermissions(String[], int)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
//                        locationManager.requestLocationUpdates(provider, 400, 1, this);
                    }

                } else {
                    new PreferenceManager(this).setLocationEnabled(false);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
            }

        }

    }


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(CanteenListActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (checkLocationPermission()) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                //Request location updates:
                try {
                    locationManager.requestLocationUpdates(provider, 400, 1, this);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }

    public void updateCanteen() {
        mCanteenArrayList.clear();

        mCanteenArrayList.add(new Canteen("Staff Canteen", "SL Campus", findDistance(22.560916d, 88.412865d, location), KEY_SURUCHI));
        mCanteenArrayList.add(new Canteen("Suruchi Canteen", "Jadavpur University", findDistance(22.499757d, 88.370122, location), KEY_SURUCHI));
        mCanteenArrayList.add(new Canteen("Aahar Canteen", "Jadavpur University", findDistance(22.496600d, 88.371966d, location), KEY_SURUCHI));

    }

    private float findDistance(Double lat, Double lng, Location userLocation) {
        Location l = new Location("");
        l.setLatitude(lat);
        l.setLongitude(lng);
        return ((userLocation.distanceTo(l)) / 1000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.canteen_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuSortDistance) {
            sortByDistance();
        } else if (id == R.id.menuSortDefault) {
            if (mCanteenArrayList == null) updateCanteen();
            if (adapter != null)
                adapter.notifyDataSetChanged();
            else adapter = new CanteenListRecyclerAdapter(mCanteenArrayList);
        } else if (id == R.id.menuSortatoz) {
            sortAlphabetically();
        }

        return super.onOptionsItemSelected(item);
    }

    private void sortAlphabetically() {

        if (mCanteenArrayList == null) updateCanteen();
        Collections.sort(mCanteenArrayList, new Comparator<Canteen>() {
            @Override
            public int compare(Canteen canteen, Canteen t1) {
                return canteen.getCanteenName().compareToIgnoreCase(t1.getCanteenName());
            }
        });

        if (adapter != null)
            adapter.notifyDataSetChanged();
        else {
            adapter = new CanteenListRecyclerAdapter(mCanteenArrayList);
        }

    }

    private void sortByDistance() {

        if (mCanteenArrayList == null) updateCanteen();
        Collections.sort(mCanteenArrayList, new Comparator<Canteen>() {
            @Override
            public int compare(Canteen canteen, Canteen t1) {
                return canteen.getLocation() > t1.getLocation() ? 1 : (canteen.getLocation() < t1.getLocation()) ? -1 : 0;
            }
        });

        if (adapter != null)
            adapter.notifyDataSetChanged();
        else {
            adapter = new CanteenListRecyclerAdapter(mCanteenArrayList);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = locationManager.getLastKnownLocation(provider);
        Log.d(TAG, "last Position:: " + location.getLongitude() + ":: " + location.getLongitude());


//        Location canteenLocation = new Location("");
//        canteenLocation.setLatitude(22.560916d);
//        canteenLocation.setLongitude(22.560916d);

//        float distance = location.distanceTo(canteenLocation);
//        Log.d(TAG, "distance:: " + distance);
//        distance is in metres
//        float distanceinKms = distance/1000;
//        Log.d(TAG, "formated distance in Kms:: " +  String.format("%.1f", distanceinKms));

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.canteenRecyclerView);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setNestedScrollingEnabled(false);
        updateCanteen();
        adapter = new CanteenListRecyclerAdapter(mCanteenArrayList);
        mRecyclerView.setAdapter(adapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        mRecyclerView.setOnClickListener();

    }

    private void navigateTo(double lat, double lng, String name) {
        String format = "geo:0,0?q=" + lat + "," + lng + "";
        Uri uri = Uri.parse(format);

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
