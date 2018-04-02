package com.patchanok.assigmentmyplace.favorite;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.patchanok.assigmentmyplace.FavoritePlaceEvent;
import com.patchanok.assigmentmyplace.R;
import com.patchanok.assigmentmyplace.base.BaseFragment;
import com.patchanok.assigmentmyplace.databinding.FragmentFavoriteBinding;
import com.patchanok.assigmentmyplace.nearby.NearbyRecyclerAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by patchanok on 3/29/2018 AD.
 */

public class FavoriteFragment extends BaseFragment {

    public static final String TYPE_FAV = "favorite";
    private FragmentFavoriteBinding binding;
    private NearbyRecyclerAdapter adapter;
    private RecyclerView recyclerView;

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

        adapter = new NearbyRecyclerAdapter(getContext(), TYPE_FAV);
        recyclerView.setAdapter(adapter);
    }

    private void isEnable(FavoritePlaceEvent favoritePlaceEvent) {
        if (favoritePlaceEvent.getFavoriteItemObjectList().size() != 0) {
            binding.setIsEnable(true);
            adapter.setFavoriteObjectList(favoritePlaceEvent.favoriteItemObjectList);
        } else {
            binding.setIsEnable(false);

        }
    }
}
