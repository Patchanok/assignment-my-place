package com.patchanok.assigmentmyplace.nearby;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.patchanok.assigmentmyplace.R;
import com.patchanok.assigmentmyplace.databinding.NearbyItemBinding;
import com.patchanok.assigmentmyplace.favorite.FavoriteItemObject;

import java.util.ArrayList;
import java.util.List;

import static com.patchanok.assigmentmyplace.Constants.TYPE_FAV;
import static com.patchanok.assigmentmyplace.Constants.TYPE_NEARBY;

/**
 * Created by patchanok on 3/30/2018 AD.
 */

public class NearbyRecyclerAdapter extends RecyclerView.Adapter<NearbyRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<NearbyItemObject> nearbyItemObjectList = new ArrayList<>();
    private List<FavoriteItemObject> favoriteItemObjectList = new ArrayList<>();
    private OnItemEventClick onItemEventClick;
    private OnFavoriteItemClick onFavoriteItemClick;
    private String type;

    public NearbyRecyclerAdapter(Context mContext, String type, OnItemEventClick onItemEventClick) {
        this.mContext = mContext;
        this.type = type;
        this.onItemEventClick = onItemEventClick;
    }

    public NearbyRecyclerAdapter(Context mContext, String type, OnFavoriteItemClick onFavoriteItemClick){
        this.mContext = mContext;
        this.type = type;
        this.onFavoriteItemClick = onFavoriteItemClick;
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

    public void setFavoriteObjectList(List<FavoriteItemObject> favoriteItemObjectList) {
        this.favoriteItemObjectList = favoriteItemObjectList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private NearbyItemBinding itemBinding;

        private View itemView;
        private LikeButton likeButton;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            itemBinding = DataBindingUtil.bind(itemView);
            likeButton = itemView.findViewById(R.id.favorite_button);
        }

        public void bindNearbyData(NearbyItemObject nearbyItemObject) {
            itemView.setOnClickListener(view -> onItemEventClick.nearbyItemClickListener(nearbyItemObject));
            likeButton.setLiked(nearbyItemObject.isFavorite());
            likeButton.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    nearbyItemObject.setFavorite(true);
                    onItemEventClick.isFavoriteListener(nearbyItemObject.isFavorite(), nearbyItemObject);
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    nearbyItemObject.setFavorite(false);
                    onItemEventClick.isFavoriteListener(nearbyItemObject.isFavorite(), nearbyItemObject);
                }
            });
            itemBinding.setType(TYPE_NEARBY);
            itemBinding.setNearbyObject(nearbyItemObject);
            itemBinding.executePendingBindings();
        }

        public void bindFavoriteData(FavoriteItemObject favoriteItemObject) {
            likeButton.setVisibility(View.INVISIBLE);
            itemView.setOnClickListener(view -> onFavoriteItemClick.favoriteItemClickListener(favoriteItemObject));
            itemBinding.setType(TYPE_FAV);
            itemBinding.setFavoriteObject(favoriteItemObject);
            itemBinding.executePendingBindings();
        }

    }

    public interface OnItemEventClick {
        void isFavoriteListener(boolean isFav, NearbyItemObject nearbyItemObject);

        void nearbyItemClickListener(NearbyItemObject nearbyItemObject);
    }

    public interface OnFavoriteItemClick {
        void favoriteItemClickListener(FavoriteItemObject favoriteItemObject);
    }

}
