package com.patchanok.assigmentmyplace.main;

import android.Manifest;
import android.app.PendingIntent;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.patchanok.assigmentmyplace.FavoritePlaceEvent;
import com.patchanok.assigmentmyplace.NearbyEvent;
import com.patchanok.assigmentmyplace.R;
import com.patchanok.assigmentmyplace.base.BaseActivity;
import com.patchanok.assigmentmyplace.favorite.FavoriteFragment;
import com.patchanok.assigmentmyplace.favorite.FavoriteViewmodel;
import com.patchanok.assigmentmyplace.geofence.GeofenceBroadcastReceiver;
import com.patchanok.assigmentmyplace.favorite.FavoriteItemObject;
import com.patchanok.assigmentmyplace.nearby.NearbyFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.patchanok.assigmentmyplace.Constants.GEOFENCE_RADIUS_IN_METERS;
import static com.patchanok.assigmentmyplace.Constants.TYPE_REQUEST_CAFE;

/**
 * Created by patchanok on 3/28/2018 AD.
 */

public class MainFragmentActivity extends BaseActivity implements OnCompleteListener<Void> {

    private FusedLocationProviderClient mFusedLocationClient;
    private static GoogleApiClient mGoogleApiClient;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private NearbyViewmodel nearbyViewmodel;
    private FavoriteViewmodel favoriteViewmodel;

    private GeofencingClient geofencingClient;
    private PendingIntent mGeofencePendingIntent;
    private List<Geofence> geofenceList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_main);
        initialView();
        initLocationService();
        initialGeofence();
        nearbyViewmodel = ViewModelProviders.of(this).get(NearbyViewmodel.class);
        favoriteViewmodel = ViewModelProviders.of(this).get(FavoriteViewmodel.class);
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
        adapter.addFragment(FavoriteFragment.newInstance(), getResources().getString(R.string.favorite_tab));
        viewPager.setAdapter(adapter);
    }

    private void initLocationService() {

        if (ContextCompat.checkSelfPermission(MainFragmentActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            getNearbyPlace(location.getLatitude(), location.getLongitude());
                        }
                    });
        } else {
            requestPermissions();
        }
    }

    private void getNearbyPlace(double lat, double lng) {
        nearbyViewmodel.checkAuthFirebase(lat, lng, TYPE_REQUEST_CAFE, getResources().getString(R.string.google_place_key))
                .observe(this, placeObject -> {
                    EventBus.getDefault().postSticky(new NearbyEvent(placeObject));
                    getFavoritePlace();
                });
    }

    private void getFavoritePlace() {
        favoriteViewmodel.getFavoritePlace().observe(this, favoriteItemObjects -> {
            EventBus.getDefault().postSticky(new FavoritePlaceEvent(favoriteItemObjects));
            if (favoriteItemObjects.size() != 0) {
                populateGeofenceList(favoriteItemObjects);
            }
        });
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(MainFragmentActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
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
                    Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_LONG).show();
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

    private void initialGeofence() {
        geofenceList = new ArrayList<>();
        mGeofencePendingIntent = null;
        geofencingClient = LocationServices.getGeofencingClient(this);
    }

    private void populateGeofenceList(List<FavoriteItemObject> favoriteItemObjectList) {
        Map<String, LatLng> landmark = new HashMap<>();

        if (favoriteItemObjectList.size() != 0) {
            for (FavoriteItemObject favoriteItem : favoriteItemObjectList) {
                landmark.put(favoriteItem.getFavName(), new LatLng(favoriteItem.getFavLat(), favoriteItem.getFavLng()));
            }
        }


        for (Map.Entry<String, LatLng> entry : landmark.entrySet()) {
            geofenceList.add(new Geofence.Builder()
                    .setRequestId(entry.getKey())
                    .setCircularRegion(
                            entry.getValue().latitude,
                            entry.getValue().longitude,
                            GEOFENCE_RADIUS_IN_METERS
                    )
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());
        }

        addGeofences();
    }

    private void addGeofences() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
            return;
        }
        geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnCompleteListener(this);
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        mGeofencePendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
    }
}
