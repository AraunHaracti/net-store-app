package com.example.netstore.viewModels;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.netstore.config.ObserverObject;
import com.example.netstore.models.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProductViewModel {
    private MutableLiveData<ObserverObject> mutableLiveData = new MutableLiveData<>();
    public LiveData<ObserverObject> getInfoData() {
        return mutableLiveData;
    }

    public void addProduct(Product product) {
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = fStore.collection("products");
        DocumentReference newProductRef = collectionReference.document();
        product._id = newProductRef.getId();

        FirebaseStorage fStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = fStorage.getReference();
        String filePath = "photo/products/" + newProductRef.getId();
        StorageReference fileRef = storageRef.child(filePath);

        fileRef.putFile(Uri.parse(product.photoPath))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        product.photoPath = filePath;

                        newProductRef.set(product)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
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

    public void editProduct(Product product) {
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = fStore.collection("products");
        DocumentReference productRef = collectionReference.document(product._id);

        FirebaseStorage fStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = fStorage.getReference();
        String filePath = "photo/products/" + product._id;
        StorageReference fileRef = storageRef.child(filePath);

        fileRef.putFile(Uri.fromFile(new File(product.photoPath)))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    product.photoPath = filePath;

                    productRef.set(product)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    mutableLiveData.postValue(new ObserverObject("edit product", true));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    mutableLiveData.postValue(new ObserverObject("edit product", false));
                                }
                            });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mutableLiveData.postValue(new ObserverObject("edit product", false));
                    }
                });
    }

    public void getProducts() {
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = fStore.collection("products");

        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Product> productList = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Product product = new Product();

                            product._id = documentSnapshot.getString("_id");
                            product.name = documentSnapshot.getString("name");
                            product.description = documentSnapshot.getString("description");
                            product.price = documentSnapshot.getDouble("price");
                            product.count = documentSnapshot.getLong("count").intValue();
                            product.photoPath = documentSnapshot.getString("photoPath");

                            productList.add(product);
                        }
                        mutableLiveData.postValue(new ObserverObject("get list product", true, productList));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mutableLiveData.postValue(new ObserverObject("get list product", false));
                    }
                });
    }
}
