package com.patchanok.assigmentmyplace.favorite;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.patchanok.assigmentmyplace.R;
import com.patchanok.assigmentmyplace.base.BaseFragment;

/**
 * Created by patchanok on 3/29/2018 AD.
 */

public class FavoriteFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }
}
