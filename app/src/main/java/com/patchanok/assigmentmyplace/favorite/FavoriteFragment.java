package com.patchanok.assigmentmyplace.favorite;

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

import com.patchanok.assigmentmyplace.FavoritePlaceEvent;
import com.patchanok.assigmentmyplace.R;
import com.patchanok.assigmentmyplace.base.BaseFragment;
import com.patchanok.assigmentmyplace.databinding.FragmentFavoriteBinding;
import com.patchanok.assigmentmyplace.main.PlaceDetailViewmodel;
import com.patchanok.assigmentmyplace.nearby.NearbyRecyclerAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.patchanok.assigmentmyplace.Constants.TYPE_FAV;

/**
 * Created by patchanok on 3/29/2018 AD.
 */

public class FavoriteFragment extends BaseFragment {

    private FragmentFavoriteBinding binding;
    private NearbyRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private PlaceDetailViewmodel placeDetailViewmodel;

    public static FavoriteFragment newInstance() {
        FavoriteFragment fragment = new FavoriteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false);
        View rootView = binding.getRoot();
        initialView(rootView);
        placeDetailViewmodel = ViewModelProviders.of(this).get(PlaceDetailViewmodel.class);
        EventBus.getDefault().register(this);

        return rootView;
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onNearbyEvent(FavoritePlaceEvent favoritePlaceEvent) {
        isEnable(favoritePlaceEvent);
    }

    private void initialView(View view) {
        recyclerView = view.findViewById(R.id.favorite_recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new NearbyRecyclerAdapter(getContext(), TYPE_FAV, favoriteItemObject -> {
            placeDetailViewmodel.getPlaceDetailMutableLiveData(favoriteItemObject.getPlaceId(),
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
        });
        recyclerView.setAdapter(adapter);
    }

    private void isEnable(FavoritePlaceEvent favoritePlaceEvent) {
        if (favoritePlaceEvent.getFavoriteItemObjectList().size() != 0) {
            binding.setIsEnableTitle(false);
            adapter.setFavoriteObjectList(favoritePlaceEvent.favoriteItemObjectList);
        } else {
            binding.setIsEnableTitle(true);
        }
    }
}
