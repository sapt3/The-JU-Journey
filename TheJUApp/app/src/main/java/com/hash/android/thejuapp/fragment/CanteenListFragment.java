package com.hash.android.thejuapp.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hash.android.thejuapp.HelperClass.PreferenceManager;
import com.hash.android.thejuapp.Model.Canteen;
import com.hash.android.thejuapp.R;
import com.hash.android.thejuapp.adapter.CanteenListRecyclerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CanteenListFragment extends android.support.v4.app.Fragment implements LocationListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static final String KEY_SURUCHI = "suruchi";
    private static final String TAG = CanteenListFragment.class.getSimpleName();
    private LocationManager locationManager;
    private String provider;
    private Location location;
    private ArrayList<Canteen> mCanteenArrayList = new ArrayList<>();
    private CanteenListRecyclerAdapter adapter;

    public CanteenListFragment() {
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
//                        locationManager.requestLocationUpdates(provider, 400, 1, this);
                    }

                } else {
                    new PreferenceManager(getActivity()).setLocationEnabled(false);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
            }

        }

    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * Called to do initial creation of a fragment.  This is called after
     * and before
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * <p>
     * <p>Note that this can be called while the fragment's activity is
     * still in the process of being created.  As such, you can not rely
     * on things like the activity's content view hierarchy being initialized
     * at this point.  If you want to do work once the activity itself is
     * created, see {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>Any restored child fragments will be created before the base
     * <code>Fragment.onCreate</code> method returns.</p>
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        getActivity().setTitle("My Canteens");
    }


    @Override
    public void onResume() {
        super.onResume();

        if (checkLocationPermission()) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                //Request location updates:
                try {
//                    locationManager.requestLocationUpdates();
                    locationManager.requestLocationUpdates(provider, 400, 1, this);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }


    public void updateCanteen() {
        mCanteenArrayList.clear();

        mCanteenArrayList.add(new Canteen("Staff Canteen", getString(R.string.sl_canteen), findDistance(22.560916d, 88.412865d, location), KEY_SURUCHI, 22.560916d, 88.412865d));
        mCanteenArrayList.add(new Canteen("Suruchi Canteen", getString(R.string.ju_canteen), findDistance(22.499757d, 88.370122, location), KEY_SURUCHI, 22.499757d, 88.370122));
        mCanteenArrayList.add(new Canteen("Aahar Canteen", getString(R.string.ju_canteen), findDistance(22.496600d, 88.371966d, location), KEY_SURUCHI, 22.496600d, 88.371966d));

    }

    private float findDistance(Double lat, Double lng, Location userLocation) {
        if (new PreferenceManager(getActivity()).isLocationEnabled()) {
            Location l = new Location("");
            l.setLatitude(lat);
            l.setLongitude(lng);
            return ((userLocation.distanceTo(l)) / 1000);
        } else {
            Toast.makeText(getActivity(), "Location disabled", Toast.LENGTH_SHORT).show();
            return 0f;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.canteen_menu, menu);
        if (!new PreferenceManager(getActivity()).isLocationEnabled()) {
            menu.removeItem(1);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuSortDistance) {
            sortByDistance();
        } else if (id == R.id.menuSortJU) {
            sortJU();
        } else if (id == R.id.menuSortatoz) {
            sortAlphabetically();
        } else if (id == R.id.menuSortSL) {
            sortSL();
        }

        return super.onOptionsItemSelected(item);
    }

    private void sortSL() {
        updateCanteen();

        ArrayList<Canteen> mArrayList = new ArrayList<>();
        mArrayList.clear();
        for (Canteen canteen : mCanteenArrayList) {
            if (canteen.getCampus().equals(getString(R.string.sl_canteen))) {
                mArrayList.add(canteen);
            }
        }

        adapter.filter(mArrayList);

    }

    private void sortJU() {
        updateCanteen();

        ArrayList<Canteen> mArrayList = new ArrayList<>();
        mArrayList.clear();
        for (Canteen canteen : mCanteenArrayList) {
            if (canteen.getCampus().equals(getString(R.string.ju_canteen))) {
                mArrayList.add(canteen);
            }
        }

        adapter.filter(mArrayList);

    }

    private void sortAlphabetically() {

        updateCanteen();
        Collections.sort(mCanteenArrayList, new Comparator<Canteen>() {
            @Override
            public int compare(Canteen canteen, Canteen t1) {
                return canteen.getCanteenName().compareToIgnoreCase(t1.getCanteenName());
            }
        });
        ArrayList<Canteen> mArrayList = new ArrayList<>();
        mArrayList.clear();
        mArrayList.addAll(mCanteenArrayList);
        adapter.filter(mArrayList);

    }

    private void sortByDistance() {
        if (new PreferenceManager(getActivity()).isLocationEnabled()) {
            updateCanteen();
            Collections.sort(mCanteenArrayList, new Comparator<Canteen>() {
                @Override
                public int compare(Canteen canteen, Canteen t1) {
                    return canteen.getLocation() > t1.getLocation() ? 1 : (canteen.getLocation() < t1.getLocation()) ? -1 : 0;
                }
            });

            ArrayList<Canteen> mArrayList = new ArrayList<>();
            mArrayList.clear();
            mArrayList.addAll(mCanteenArrayList);
            adapter.filter(mArrayList);
        }

    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.content_canteen_list, container, false);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        if (ActivityCompat.checkSelfPermission(
                getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                        getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {
            return rootView;
        }
        location = locationManager.getLastKnownLocation(provider);


        RecyclerView mRecyclerView = rootView.findViewById(R.id.canteenRecyclerView);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setNestedScrollingEnabled(false);
        updateCanteen();
        adapter = new CanteenListRecyclerAdapter(mCanteenArrayList, getActivity());
        mRecyclerView.setAdapter(adapter);


        return rootView;
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
