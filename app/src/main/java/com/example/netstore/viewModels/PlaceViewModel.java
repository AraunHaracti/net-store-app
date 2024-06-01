package com.example.netstore.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.netstore.config.ObserverObject;
import com.example.netstore.models.Place;
import com.example.netstore.models.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PlaceViewModel {
    private MutableLiveData<ObserverObject> mutableLiveData = new MutableLiveData<>();

    public LiveData<ObserverObject> getInfoData() {
        return mutableLiveData;
    }

    private FirebaseFirestore firestore;

    private String collectionName = "places";

    public PlaceViewModel() {
        firestore = FirebaseFirestore.getInstance();
    }

    public void addPlace(Place place) {
        DocumentReference documentRef = firestore.collection(collectionName).document();

        place._id = documentRef.getId();

        documentRef.set(place)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        mutableLiveData.postValue(new ObserverObject("add place", true));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mutableLiveData.postValue(new ObserverObject("add place", false));
                    }
                });
    }

    public void editPlace(Place place) {
        firestore.collection(collectionName).document(place._id).set(place)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        mutableLiveData.postValue(new ObserverObject("edit place", true));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mutableLiveData.postValue(new ObserverObject("edit place", false));
                    }
                });
    }

    public void deletePlace(Place place) {
        firestore.collection(collectionName).document(place._id).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        mutableLiveData.postValue(new ObserverObject("delete place", true));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mutableLiveData.postValue(new ObserverObject("delete place", false));
                    }
                });
    }

    public void getPlaces() {
        firestore.collection(collectionName).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Place> placeList = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Place place = new Place();

                            place._id = documentSnapshot.getString("_id");
                            place.name = documentSnapshot.getString("name");
                            place.description = documentSnapshot.getString("description");
                            place.products = (List<Product>) documentSnapshot.get("products");

                            placeList.add(place);
                        }
                        mutableLiveData.postValue(new ObserverObject("get list place", true, placeList));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mutableLiveData.postValue(new ObserverObject("get list place", false));
                    }
                });
    }
}
