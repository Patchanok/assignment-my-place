package com.patchanok.assigmentmyplace.service;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.patchanok.assigmentmyplace.firebase.RxFirebaseAuth;
import com.patchanok.assigmentmyplace.firebase.RxFirebaseChildEvent;
import com.patchanok.assigmentmyplace.firebase.RxFirebaseDatabase;
import com.patchanok.assigmentmyplace.model.FavoriteItemObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by patchanok on 3/31/2018 AD.
 */

public class FirebaseService {

    public static final String KEY_FAV = "favorites";
    public static final String MAP_USER_UID = "userUid";

    public static final String MAP_FAVID = "favId";
    public static final String MAP_FAVNAME = "favName";
    public static final String MAP_FAVLAT = "favLat";
    public static final String MAP_FAVLNG = "favLng";
    public static final String MAP_FAVURL = "favUrl";
    public static final String MAP_IS_FAV = "favorite";
    public static final String MAP_FAVVICI = "vicinity";


    public DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    private RxFirebaseAuth authenticate = new RxFirebaseAuth();

    public Observable<Map<String, String>> loginAnonymous() {
        return authenticate.rxLoginAnonymous();
    }

    public Observable<Map<String, Boolean>> createFavoriteItem(FavoriteItemObject favoriteItemObject) {
        return RxFirebaseDatabase.create(mDatabaseReference
                .child(KEY_FAV).child(RxFirebaseAuth.getCurrentUser().getUid())
                .child(favoriteItemObject.getFavId()), favoriteItemObject);
    }

    public Observable<List<Map<String, Object>>> getFavoritePlace() {
        final List<Map<String, Object>> mapList = new ArrayList<>();
        return RxFirebaseDatabase.observeValueEvent(mDatabaseReference.child(KEY_FAV)
                .child(RxFirebaseAuth.getCurrentUser().getUid()))
                .map(result -> {
                    if ( result.getKey().equals(RxFirebaseAuth.getCurrentUser().getUid())) {
                        mapList.clear();
                        for (DataSnapshot dataSnapshot : result.getChildren()) {
                            Map<String, Object> mapResult = new HashMap<>();
                            Map<String, String> value = (Map<String, String>) dataSnapshot.getValue();
                            mapResult.put(MAP_FAVID, value.get(MAP_FAVID));
                            mapResult.put(MAP_FAVNAME, value.get(MAP_FAVNAME));
                            mapResult.put(MAP_FAVLAT, value.get(MAP_FAVLAT));
                            mapResult.put(MAP_FAVLNG, value.get(MAP_FAVLNG));
                            mapResult.put(MAP_FAVURL, value.get(MAP_FAVURL));
                            mapResult.put(MAP_IS_FAV, value.get(MAP_IS_FAV));
                            mapResult.put(MAP_FAVVICI, value.get(MAP_FAVVICI));
                            mapList.add(mapResult);
                        }
                    }
                    return mapList;
                });
    }
}