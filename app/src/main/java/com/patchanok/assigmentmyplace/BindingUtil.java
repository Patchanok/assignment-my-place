package com.patchanok.assigmentmyplace;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by patchanok on 3/30/2018 AD.
 */

public final class BindingUtil {

    private BindingUtil(){}

    @BindingAdapter("imagePath")
    public static void loadImage(ImageView imageView, Object url) {

        Glide.with(imageView.getContext()).load(url).into(imageView);
    }
}


