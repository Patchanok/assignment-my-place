package com.patchanok.assigmentmyplace.nearby;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.patchanok.assigmentmyplace.R;
import com.patchanok.assigmentmyplace.base.BaseFragment;
import com.patchanok.assigmentmyplace.map.MapsActivity;

/**
 * Created by patchanok on 3/29/2018 AD.
 */

public class NearbyFragment extends BaseFragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    public static NearbyFragment newInstance() {
        NearbyFragment fragment = new NearbyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_nearby, container, false);
        initialView(rootView);
        return rootView;
    }

    private void initialView(View view) {
        recyclerView = view.findViewById(R.id.nearby_recyclerview);
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                Intent intentToMap = new Intent(getActivity(), MapsActivity.class);
                startActivity(intentToMap);
                break;
        }
    }
}
