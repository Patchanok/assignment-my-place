package com.patchanok.assigmentmyplace.main;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.patchanok.assigmentmyplace.R;
import com.patchanok.assigmentmyplace.base.BaseActivity;
import com.patchanok.assigmentmyplace.favorite.FavoriteFragment;
import com.patchanok.assigmentmyplace.model.PlaceObject;
import com.patchanok.assigmentmyplace.nearby.NearbyFragment;

/**
 * Created by patchanok on 3/28/2018 AD.
 */

public class MainFragmentActivity extends BaseActivity {

    private FusedLocationProviderClient mFusedLocationClient;
    private static GoogleApiClient mGoogleApiClient;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private NearbyViewmodel nearbyViewmodel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_main);
        initialView();
        initLocationService();
        nearbyViewmodel = ViewModelProviders.of(this).get(NearbyViewmodel.class);

    }

    private void initLocationService() {
        if (ContextCompat.checkSelfPermission(MainFragmentActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                getNearbyPlace(location.getLatitude(),location.getLongitude());
                            }
                        }
                    });
        } else {
            requestPermissions();
        }
    }

    private void initialView() {
        ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        initialBaseView(viewGroup, false);
        viewPager = findViewById(R.id.viewpager);
        setupViewpager(viewPager);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewpager(ViewPager viewPager) {
        MainFragmentAdapter adapter = new MainFragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(NearbyFragment.newInstance(), getResources().getString(R.string.nearby_tab));
        adapter.addFragment(new FavoriteFragment(), getResources().getString(R.string.favorite_tab));
        viewPager.setAdapter(adapter);

    }

    private void getNearbyPlace(double lat, double lng) {
        nearbyViewmodel.getNearbyPlace(lat, lng, "food", getResources().getString(R.string.google_place_key))
                .observe(this, new Observer<PlaceObject>() {
            @Override
            public void onChanged(@Nullable PlaceObject placeObject) {
                Log.i("getNearbyPlace()","placeObject : "+placeObject.getPlaceDetailObject().get(0).getId());
            }
        });
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(MainFragmentActivity.this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainFragmentActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(MainFragmentActivity.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION)) {
                requestPermissionDialog();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_CODE);
            }
        } else {

        }
    }

    private void requestPermissionDialog() {
        ActivityCompat.requestPermissions(MainFragmentActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_CODE);
        ActivityCompat.requestPermissions(MainFragmentActivity.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        initLocationService();
                    }
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }


}
