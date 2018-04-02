package com.patchanok.assigmentmyplace.nearby;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.patchanok.assigmentmyplace.R;
import com.patchanok.assigmentmyplace.databinding.NearbyItemBinding;
import com.patchanok.assigmentmyplace.model.FavoriteItemObject;
import com.patchanok.assigmentmyplace.model.NearbyItemObject;

import java.util.ArrayList;
import java.util.List;

import static com.patchanok.assigmentmyplace.favorite.FavoriteFragment.TYPE_FAV;
import static com.patchanok.assigmentmyplace.nearby.NearbyFragment.TYPE_NEARBY;

/**
 * Created by patchanok on 3/30/2018 AD.
 */

public class NearbyRecyclerAdapter extends RecyclerView.Adapter<NearbyRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<NearbyItemObject> nearbyItemObjectList = new ArrayList<>();
    private List<FavoriteItemObject> favoriteItemObjectList = new ArrayList<>();
    private OnFavoriteItem favoriteItem;
    private String type;

    public NearbyRecyclerAdapter(Context mContext, String type){
        this.mContext = mContext;
        this.type = type;
    }

    public NearbyRecyclerAdapter(Context mContext, String type, OnFavoriteItem favoriteItem) {
        this.mContext = mContext;
        this.type = type;
        this.favoriteItem = favoriteItem;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.nearby_item, parent, false);
        return new NearbyRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (type.equals(TYPE_NEARBY)) {
            NearbyItemObject nearbyItemObject = nearbyItemObjectList.get(position);
            holder.bindNearbyData(nearbyItemObject);
        } else {
            FavoriteItemObject favoriteItemObject = favoriteItemObjectList.get(position);
            holder.bindFavoriteData(favoriteItemObject);
        }
    }

    @Override
    public int getItemCount() {
        return (type.equals(TYPE_NEARBY)) ? nearbyItemObjectList.size() : favoriteItemObjectList.size();
    }

    public void setPlaceObject(List<NearbyItemObject> nearbyItemObjectList) {
        this.nearbyItemObjectList = nearbyItemObjectList;
        notifyDataSetChanged();
    }

    public void setFavoriteObjectList(List<FavoriteItemObject> favoriteItemObjectList){
        this.favoriteItemObjectList = favoriteItemObjectList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LikeButton likeButton;
        private NearbyItemBinding itemBinding;

        public ViewHolder(View itemView) {
            super(itemView);
            itemBinding = DataBindingUtil.bind(itemView);
            likeButton = itemView.findViewById(R.id.favorite_button);
        }

        public void bindNearbyData(NearbyItemObject nearbyItemObject) {
            likeButton.setLiked(nearbyItemObject.isFavorite());
            likeButton.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    favoriteItem.isFavoriteListener(likeButton.isLiked(), nearbyItemObject);
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    favoriteItem.isFavoriteListener(likeButton.isLiked(), nearbyItemObject);
                }
            });
            itemBinding.setViewtype(TYPE_NEARBY);
            itemBinding.setObject(nearbyItemObject);
            itemBinding.executePendingBindings();
        }

        public void bindFavoriteData(FavoriteItemObject favoriteItemObject) {
            likeButton.setVisibility(View.GONE);
            itemBinding.setViewtype(TYPE_FAV);
            itemBinding.setFavObject(favoriteItemObject);
            itemBinding.executePendingBindings();
        }
    }

    public interface OnFavoriteItem {
        void isFavoriteListener(boolean isFav, NearbyItemObject nearbyItemObject);
    }

}
