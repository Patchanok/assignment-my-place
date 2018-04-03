package com.patchanok.assigmentmyplace.map;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.patchanok.assigmentmyplace.NearbyEvent;
import com.patchanok.assigmentmyplace.R;
import com.patchanok.assigmentmyplace.base.BaseActivity;
import com.patchanok.assigmentmyplace.main.NearbyViewmodel;

import org.greenrobot.eventbus.EventBus;

import static com.patchanok.assigmentmyplace.Constants.TYPE_REQUEST_CAFE;

public class MapsActivity extends BaseActivity implements
        OnMapReadyCallback, GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveListener {

    //    private ActivityMapsBinding binding;
    private FusedLocationProviderClient mFusedLocationClient;
    private GoogleMap mMap;
    private LatLng latLng;
    private NearbyViewmodel nearbyViewmodel;

    private RelativeLayout snippetLayout;
    private TextView snippetLabelTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
//        binding = DataBindingUtil.setContentView(MapsActivity.this, R.layout.activity_maps);

        ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        initialBaseView(viewGroup, true);
        snippetLayout = findViewById(R.id.snippet_marker);
        snippetLabelTV = findViewById(R.id.snippet_label);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        nearbyViewmodel = ViewModelProviders.of(this).get(NearbyViewmodel.class);
        snippetLayout.setVisibility(View.GONE);
        snippetLabelTV.setOnClickListener(v -> onClickSnippetMarker());
//        binding.setIsEnableSnippet(false);
//        binding.setView(MapsActivity.this);

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
//        binding.setIsEnableSnippet(true);
        snippetLayout.setVisibility(View.VISIBLE);
//        onClickSnippetMarker();

    }

    @Override
    public void onCameraMove() {
//        binding.setIsEnableSnippet(false);
        snippetLayout.setVisibility(View.GONE);

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

    public void onClickSnippetMarker() {
        nearbyViewmodel.checkAuthFirebase(latLng.latitude, latLng.longitude,
                TYPE_REQUEST_CAFE, getResources().getString(R.string.google_place_key))
                .observe(MapsActivity.this, allPlaceDataObject -> {
                    EventBus.getDefault().postSticky(new NearbyEvent(allPlaceDataObject.getPlaceObject()));
                    finish();
                });
    }

}
