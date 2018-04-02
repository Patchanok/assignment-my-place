package com.patchanok.assigmentmyplace.nearby;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.patchanok.assigmentmyplace.NearbyEvent;
import com.patchanok.assigmentmyplace.R;
import com.patchanok.assigmentmyplace.base.BaseFragment;
import com.patchanok.assigmentmyplace.databinding.FragmentNearbyBinding;
import com.patchanok.assigmentmyplace.favorite.FavoriteViewmodel;
import com.patchanok.assigmentmyplace.map.MapsActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by patchanok on 3/29/2018 AD.
 */

public class NearbyFragment extends BaseFragment {

    public static final String TYPE_NEARBY = "nearby";
    private FragmentNearbyBinding binding;
    private NearbyRecyclerAdapter adapter;
    private RecyclerView recyclerView;

    private FavoriteViewmodel favoriteViewmodel;

    public static NearbyFragment newInstance() {
        NearbyFragment fragment = new NearbyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_nearby, container, false);
        favoriteViewmodel = ViewModelProviders.of(this).get(FavoriteViewmodel.class);
        View rootView = binding.getRoot();
        initialView(rootView);
        EventBus.getDefault().register(this);
        return rootView;
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onNearbyEvent(NearbyEvent nearbyEvent) {
        isEnable(nearbyEvent);
    }

    private void initialView(View view) {
        recyclerView = view.findViewById(R.id.nearby_recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new NearbyRecyclerAdapter(getContext(), TYPE_NEARBY, (isFav, nearbyItemObject) ->
                favoriteViewmodel.createFavoritePlace(isFav, nearbyItemObject)
                        .observe(this, result -> { }));
        recyclerView.setAdapter(adapter);
        binding.setView(this);
    }

    private void isEnable(NearbyEvent nearbyEvent) {
        if (nearbyEvent.getPlaceObject().getPlaceDetailObject().size() != 0) {
            binding.setIsEnable(true);
            adapter.setPlaceObject(nearbyEvent.getPlaceObject().getNearbyItemObjectList());
        } else {
            binding.setIsEnable(false);

        }
    }

    public View.OnClickListener openMap() {
        return v -> {
            Intent intentToMap = new Intent(getActivity(), MapsActivity.class);
            startActivity(intentToMap);
            getTransitionActivity();
        };
    }

}
