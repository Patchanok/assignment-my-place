package com.patchanok.assigmentmyplace.firebase;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

import static com.patchanok.assigmentmyplace.Constants.MAP_USER_UID;

/**
 * Created by patchanok on 3/31/2018 AD.
 */

public class RxFirebaseAuth {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public static FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public Observable<Map<String, String>> rxLoginAnonymous() {
        return Observable.create(e ->
                firebaseAuth.signInAnonymously()
                        .addOnCompleteListener(task -> {
                            if (task.isComplete()) {
                                Map<String, String> user = new HashMap<>();
                                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                user.put(MAP_USER_UID, firebaseUser.getUid());
                                e.onNext(user);
                            } else {
                                Log.e("RxFirebaseAuth", "else onComplete : " + task.isSuccessful());
                            }
                        }));
    }
}
