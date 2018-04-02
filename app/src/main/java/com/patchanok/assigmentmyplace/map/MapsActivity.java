package com.patchanok.assigmentmyplace.map;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.patchanok.assigmentmyplace.NearbyEvent;
import com.patchanok.assigmentmyplace.R;
import com.patchanok.assigmentmyplace.base.BaseActivity;
import com.patchanok.assigmentmyplace.databinding.ActivityMapsBinding;
import com.patchanok.assigmentmyplace.main.NearbyViewmodel;
import com.patchanok.assigmentmyplace.model.AllPlaceDataObject;

import org.greenrobot.eventbus.EventBus;

public class MapsActivity extends BaseActivity implements
        OnMapReadyCallback, GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveListener {

    private ActivityMapsBinding binding;
    private FusedLocationProviderClient mFusedLocationClient;
    private GoogleMap mMap;
    private LatLng latLng;

    private NearbyViewmodel nearbyViewmodel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(MapsActivity.this, R.layout.activity_maps);

        ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        initialBaseView(viewGroup, true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        nearbyViewmodel = ViewModelProviders.of(this).get(NearbyViewmodel.class);
        binding.setIsEnableSnippet(false);
        binding.setView(MapsActivity.this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraIdleListener(this);
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        initLocationService();
    }

    @Override
    public void onCameraIdle() {
        latLng = mMap.getCameraPosition().target;
        binding.setIsEnableSnippet(true);
        onClickSnippetMarker();

    }

    @Override
    public void onCameraMove() {
        binding.setIsEnableSnippet(false);
    }

    private void initLocationService() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 40.0f));
                            mMap.setMinZoomPreference(6.0f);
                            mMap.setMaxZoomPreference(14.0f);
                        }
                    });
        }
    }

    public View.OnClickListener onClickSnippetMarker() {
        return v -> nearbyViewmodel.checkAuthFirebase(latLng.latitude, latLng.longitude,
                "cafe", getResources().getString(R.string.google_place_key))
                .observe(MapsActivity.this, allPlaceDataObject -> {
                    EventBus.getDefault().postSticky(new NearbyEvent(allPlaceDataObject.getPlaceObject()));
                    finish();
                });
    }

}
