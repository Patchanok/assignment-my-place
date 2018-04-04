package com.patchanok.assigmentmyplace.firebase;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by patchanok on 4/1/2018 AD.
 */

public class RxFirebaseDatabase {

    public static final String ONCOMPLETE = "complete";

    public static Observable<Map<String, Boolean>> create(final DatabaseReference query, final Object value) {
        return Observable.create(e -> {
            query.setValue(value, (databaseError, databaseReference) -> {
                Map<String, Boolean> mapBoolean = new HashMap<>();
                mapBoolean.put(ONCOMPLETE, true);
                e.onNext(mapBoolean);
            });
        });
    }

    public static Observable<DataSnapshot> observeValueEvent(final Query query) {
        return Observable.create(e ->
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        e.onNext(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        e.onError(databaseError.toException());
                    }
                }));
    }
}
