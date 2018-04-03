package com.patchanok.assigmentmyplace.nearby;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.patchanok.assigmentmyplace.NearbyEvent;
import com.patchanok.assigmentmyplace.R;
import com.patchanok.assigmentmyplace.base.BaseFragment;
import com.patchanok.assigmentmyplace.favorite.FavoriteItemObject;
import com.patchanok.assigmentmyplace.favorite.FavoriteViewmodel;
import com.patchanok.assigmentmyplace.main.PlaceDetailViewmodel;
import com.patchanok.assigmentmyplace.map.MapsActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.patchanok.assigmentmyplace.Constants.TYPE_NEARBY;

/**
 * Created by patchanok on 3/29/2018 AD.
 */

public class NearbyFragment extends BaseFragment {

    private FloatingActionButton fab;
    private NearbyRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView noDataTV;

    private FavoriteViewmodel favoriteViewmodel;
    private PlaceDetailViewmodel placeDetailViewmodel;

    public static NearbyFragment newInstance() {
        NearbyFragment fragment = new NearbyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_nearby, container, false);
        View rootView = inflater.inflate(R.layout.fragment_nearby, container, false);
        initialView(rootView);
        favoriteViewmodel = ViewModelProviders.of(this).get(FavoriteViewmodel.class);
        placeDetailViewmodel = ViewModelProviders.of(this).get(PlaceDetailViewmodel.class);
        EventBus.getDefault().register(this);
        return rootView;
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onNearbyEvent(NearbyEvent nearbyEvent) {
        isEnable(nearbyEvent);
    }

    private void initialView(View view) {
        fab = view.findViewById(R.id.fab);
        noDataTV = view.findViewById(R.id.no_data_textview);
        progressBar = view.findViewById(R.id.nearby_progressbar);
        recyclerView = view.findViewById(R.id.nearby_recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new NearbyRecyclerAdapter(getContext(), TYPE_NEARBY, new NearbyRecyclerAdapter.OnItemEventClick() {
            @Override
            public void isFavoriteListener(boolean isFav, NearbyItemObject nearbyItemObject) {
                favoriteViewmodel.createFavoritePlace(isFav, nearbyItemObject)
                        .observe(getActivity(), result -> {
                        });
            }

            @Override
            public void nearbyItemClickListener(NearbyItemObject nearbyItemObject) {
                placeDetailViewmodel.getPlaceDetailMutableLiveData(nearbyItemObject.getPlaceId(),
                        getString(R.string.google_place_key)).observe(getActivity(),
                        result -> {
                            String url;
                            if (!TextUtils.isEmpty(result.getWebsite())){
                                url = (!TextUtils.isEmpty(result.getWebsite())) ?
                                        result.getWebsite() : result.getUrl();
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.dont_have_url), Toast.LENGTH_SHORT).show();
                            }

                        });
            }
        });
        recyclerView.setAdapter(adapter);
        fab.setOnClickListener(v -> openMap());
        progressBar.setVisibility(View.VISIBLE);
    }

    private void isEnable(NearbyEvent nearbyEvent) {
        progressBar.setVisibility(View.GONE);
        if (nearbyEvent.getPlaceObject().getPlaceDetailObject().size() != 0) {
            noDataTV.setVisibility(View.GONE);
            adapter.setPlaceObject(nearbyEvent.getPlaceObject().getNearbyItemObjectList());
        } else {
            noDataTV.setVisibility(View.VISIBLE);
        }
    }

    public void openMap() {
        Intent intentToMap = new Intent(getActivity(), MapsActivity.class);
        startActivity(intentToMap);
        getTransitionActivity();
    }

}
