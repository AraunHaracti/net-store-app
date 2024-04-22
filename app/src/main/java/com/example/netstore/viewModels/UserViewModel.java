package com.example.netstore.viewModels;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.netstore.config.Config;
import com.example.netstore.config.ObserverObject;
import com.example.netstore.models.Client;
import com.example.netstore.models.Employee;
import com.example.netstore.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

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
                    DocumentReference docRef = collectionReference.document();

                    user._id = docRef.getId();

                    docRef.set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    mutableLiveData.postValue(new ObserverObject("reg user", true));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    mutableLiveData.postValue(new ObserverObject("reg user", false));
                                }
                            });
                })
                .addOnFailureListener(e -> {
                    mutableLiveData.postValue(new ObserverObject("reg user", false));
                });
    }

    public void updateUser(User user) {
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = fStore.collection("users");
        DocumentReference docRef = collectionReference.document(user._id);

        docRef.set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        mutableLiveData.postValue(new ObserverObject("update user", true));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mutableLiveData.postValue(new ObserverObject("update user", false));
                    }
                });
    }

    public void getEmployee(User user) {
        if (user.type != User.UserType.Employee) {
            mutableLiveData.postValue(new ObserverObject("get employee", false));
            return;
        }

        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = fStore.collection("users");
        DocumentReference docRef = collectionReference.document(user._id);

        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Employee employee = documentSnapshot.toObject(Employee.class);
                            mutableLiveData.postValue(new ObserverObject("get employee", true, employee));
                        } else {
                            mutableLiveData.postValue(new ObserverObject("get employee", false));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mutableLiveData.postValue(new ObserverObject("get employee", false));
                    }
                });
    }

    public void getClient(User user) {
        if (user.type != User.UserType.Client) {
            mutableLiveData.postValue(new ObserverObject("get client", false));
            return;
        }

        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = fStore.collection("users");
        DocumentReference docRef = collectionReference.document(user._id);

        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Client client = documentSnapshot.toObject(Client.class);
                        mutableLiveData.postValue(new ObserverObject("get client", true, client));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mutableLiveData.postValue(new ObserverObject("get client", false));
                    }
                });
    }

    public void saveCurrentUser(Context context, User user) {
        String currentUserJson = new Gson().toJson(user, User.class);
        SharedPreferences.Editor editorSP = context.getSharedPreferences(Config.SP_FILE_TAG, Context.MODE_PRIVATE).edit();
        editorSP.putString(Config.SP_USER_TAG, currentUserJson);
        editorSP.apply();
    }
}
