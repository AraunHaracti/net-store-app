package com.example.netstore.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.netstore.ObserverObject;
import com.example.netstore.models.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.util.UUID;

public class ProductViewModel {
    private MutableLiveData<ObserverObject> mutableLiveData = new MutableLiveData<>();
    public LiveData<ObserverObject> getInfoData() {
        return mutableLiveData;
    }

    public void addProduct(Product product) {

        FirebaseStorage fStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = fStorage.getReference();

        String filePath = "photo/products/" + UUID.randomUUID().toString();

        StorageReference fileRef = storageRef.child(filePath);

        fileRef.putFile(product.photoUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        product.photoUri = taskSnapshot.getUploadSessionUri();

                        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
                        CollectionReference collectionReference = fStore.collection("products");
                        collectionReference.add(product)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        mutableLiveData.postValue(new ObserverObject("add product", true));
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        mutableLiveData.postValue(new ObserverObject("add product", false));
                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mutableLiveData.postValue(new ObserverObject("add product", false));
                    }
                });
    }
}
