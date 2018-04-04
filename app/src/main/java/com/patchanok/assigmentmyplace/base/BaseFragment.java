package com.patchanok.assigmentmyplace.base;

import android.support.v4.app.Fragment;

import com.patchanok.assigmentmyplace.R;

/**
 * Created by patchanok on 3/29/2018 AD.
 */

public class BaseFragment extends Fragment {

    public BaseFragment() {
        super();
    }

    protected void getTransitionActivity() {
        getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }
}
