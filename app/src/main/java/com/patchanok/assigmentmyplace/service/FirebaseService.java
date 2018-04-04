package com.patchanok.assigmentmyplace.service;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.patchanok.assigmentmyplace.firebase.RxFirebaseAuth;
import com.patchanok.assigmentmyplace.firebase.RxFirebaseDatabase;
import com.patchanok.assigmentmyplace.favorite.FavoriteItemObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

import static com.patchanok.assigmentmyplace.Constants.*;

/**
 * Created by patchanok on 3/31/2018 AD.
 */

public class FirebaseService {

    public DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    private RxFirebaseAuth authenticate = new RxFirebaseAuth();

    public Observable<Map<String, String>> loginAnonymous() {
        return authenticate.rxLoginAnonymous();
    }

    public Observable<Map<String, Boolean>> createFavoritePlace(FavoriteItemObject favoriteItemObject) {
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
                            mapResult.put(MAP_PLACEID, value.get(MAP_PLACEID));

                            mapList.add(mapResult);
                        }
                    }
                    return mapList;
                });
    }
}