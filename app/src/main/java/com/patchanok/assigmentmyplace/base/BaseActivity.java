package com.patchanok.assigmentmyplace.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.patchanok.assigmentmyplace.R;

/**
 * Created by patchanok on 3/29/2018 AD.
 */

public class BaseActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static final int PERMISSIONS_REQUEST_CODE = 99;
    private GoogleApiClient mGoogleApiClient;

    private Toolbar toolbar;

    public BaseActivity() {
        super();
    }

    protected void initialBaseView(View view, boolean isHomeEnable) {
        toolbar = view.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(isHomeEnable);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

}
