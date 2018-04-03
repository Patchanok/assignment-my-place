package com.patchanok.assigmentmyplace.nearby;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.patchanok.assigmentmyplace.R;
import com.patchanok.assigmentmyplace.favorite.FavoriteItemObject;

import java.util.ArrayList;
import java.util.List;

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

    public NearbyRecyclerAdapter(Context mContext, String type) {
        this.mContext = mContext;
        this.type = type;
    }

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

        private View itemView;
        private ImageView imageView;
        private TextView placeTitle, desTitle;
        private LikeButton likeButton;
//        private NearbyItemBinding itemBinding;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
//            itemBinding = DataBindingUtil.bind(itemView);
            imageView = itemView.findViewById(R.id.icon_image);
            placeTitle = itemView.findViewById(R.id.place_name_textview);
            desTitle = itemView.findViewById(R.id.place_des_textview);
            likeButton = itemView.findViewById(R.id.favorite_button);
        }

        public void bindNearbyData(NearbyItemObject nearbyItemObject) {
            Glide.with(imageView.getContext()).load(nearbyItemObject.getUrl()).into(imageView);
            placeTitle.setText(nearbyItemObject.getName());
            desTitle.setText(nearbyItemObject.getVicinity());
            itemView.setOnClickListener(view -> onItemEventClick.nearbyItemClickListener(nearbyItemObject));

            likeButton.setLiked(nearbyItemObject.isFavorite());
            likeButton.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    onItemEventClick.isFavoriteListener(likeButton.isLiked(), nearbyItemObject);
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    onItemEventClick.isFavoriteListener(likeButton.isLiked(), nearbyItemObject);
                }
            });
//            itemBinding.setViewtype(TYPE_NEARBY);
//            itemBinding.setObject(nearbyItemObject);
//            itemBinding.executePendingBindings();
        }

        public void bindFavoriteData(FavoriteItemObject favoriteItemObject) {
            likeButton.setVisibility(View.INVISIBLE);
            Glide.with(imageView.getContext()).load(favoriteItemObject.getFavUrl()).into(imageView);
            placeTitle.setText(favoriteItemObject.getFavName());
            desTitle.setText(favoriteItemObject.getVicinity());
            itemView.setOnClickListener(view -> onFavoriteItemClick.favoriteItemClickListener(favoriteItemObject));
//            itemBinding.setViewtype(TYPE_FAV);
//            itemBinding.setFavObject(favoriteItemObject);
//            itemBinding.executePendingBindings();
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
