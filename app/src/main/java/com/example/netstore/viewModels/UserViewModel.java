package com.example.netstore.viewModels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.netstore.ObserverObject;
import com.example.netstore.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.Map;

public class UserViewModel {
    private MutableLiveData<ObserverObject> mutableLiveData = new MutableLiveData<>();
    public LiveData<ObserverObject> getInfoData() {
        return mutableLiveData;
    }

    public void checkLogin(String login) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        QuerySnapshot querySnapshot = firestore.collection("users").whereEqualTo("login", login).get().getResult();

        Log.d("check", querySnapshot.toString());
//        querySnapshot.getDocuments()
// TODO
//        for (QueryDocumentSnapshot q : querySnapshot)
//        }
    }

    public void authenticationUser(String email, String password) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                String uid = mAuth.getCurrentUser().getUid();

                FirebaseFirestore fStore = FirebaseFirestore.getInstance();
                CollectionReference collectionReference = fStore.collection("users");
                collectionReference.whereEqualTo("firebaseId", uid).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (DocumentSnapshot documentSnap : queryDocumentSnapshots.getDocuments()) {
                                    User authUser = documentSnap.toObject(User.class);
                                    mutableLiveData.postValue(new ObserverObject("auth user", true, authUser));
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mutableLiveData.postValue(new ObserverObject("auth user", false));
                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mutableLiveData.postValue(new ObserverObject("auth user", false));
            }
        });
    }

    public void registrationUser(String email, String password, User user) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    String uid = mAuth.getCurrentUser().getUid();
                    user.firebaseId = uid;

                    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
                    CollectionReference collectionReference = fStore.collection("users");
                    collectionReference.add(user)
                            .addOnSuccessListener(documentReference ->
                                    mutableLiveData.postValue(new ObserverObject("reg user", true)))
                            .addOnFailureListener(e ->
                                    mutableLiveData.postValue(new ObserverObject("reg user", false)));
                })
                .addOnFailureListener(e -> {
                    mutableLiveData.postValue(new ObserverObject("reg user", false));
                });
    }
}
