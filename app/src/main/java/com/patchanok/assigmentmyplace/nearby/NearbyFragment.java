package com.patchanok.assigmentmyplace.nearby;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.patchanok.assigmentmyplace.NearbyEvent;
import com.patchanok.assigmentmyplace.R;
import com.patchanok.assigmentmyplace.base.BaseFragment;
import com.patchanok.assigmentmyplace.databinding.FragmentNearbyBinding;
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

    private FragmentNearbyBinding binding;
    private NearbyRecyclerAdapter adapter;
    private RecyclerView recyclerView;

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_nearby, container, false);
        View rootView = binding.getRoot();
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
                            if (!TextUtils.isEmpty(result.getWebsite())) {
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
        binding.setView(this);
        binding.setIsEnableProgress(true);
    }

    private void isEnable(NearbyEvent nearbyEvent) {
        binding.setIsEnableProgress(false);
        if (nearbyEvent.getPlaceObject().getPlaceDetailObject().size() != 0) {
            binding.setIsEnableTitle(false);
            adapter.setPlaceObject(nearbyEvent.getPlaceObject().getNearbyItemObjectList());
        } else {
            binding.setIsEnableTitle(true);

        }
    }

    public View.OnClickListener openMap() {
        return view -> {
            Intent intentToMap = new Intent(getActivity(), MapsActivity.class);
            startActivity(intentToMap);
            getTransitionActivity();
        };
    }

}
