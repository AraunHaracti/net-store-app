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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
//
//        for (QueryDocumentSnapshot q : querySnapshot)
//        }
    }

    public void Registration(User user) {
        String firebaseId = FirebaseAuthSignUp(user.login, user.password);
        user.firebaseId = firebaseId;

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = firestore.collection("users");
        collectionReference.add(user).addOnSuccessListener(documentReference -> {
            mutableLiveData.postValue(new ObserverObject("reg user", true));
        }).addOnFailureListener(e -> {
            mutableLiveData.postValue(new ObserverObject("reg user", false));
        });
    }

    private String FirebaseAuthSignUp(String login, String password) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(login, password);
        String uid = mAuth.getUid();
        return uid;
    }
}
