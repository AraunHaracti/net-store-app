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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class ProductViewModel {
    private MutableLiveData<ObserverObject> mutableLiveData = new MutableLiveData<>();
    public LiveData<ObserverObject> getInfoData() {
        return mutableLiveData;
    }

    private FirebaseFirestore firestore;
    private FirebaseStorage storage;

    private String collectionName = "products";
    private String imagePath = "photo/products/";

    public ProductViewModel() {
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public void addProduct(Product product) {
        DocumentReference documentRef = firestore.collection(collectionName).document();

        StorageReference storageRef = storage.getReference().child(imagePath + documentRef.getId());

        storageRef.putFile(Uri.parse(product.photoPath))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                product._id = documentRef.getId();
                                product.photoPath = uri.toString();

                                documentRef.set(product)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                mutableLiveData.postValue(new ObserverObject("add product", true));
                                            }
                                        });
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
        DocumentReference documentRef = firestore.collection(collectionName).document(product._id);

        if (product.photoPath.startsWith("https://firebasestorage.googleapis.com/")) {
            documentRef.set(product)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            mutableLiveData.postValue(new ObserverObject("edit product", true));
                        }
                    }) .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mutableLiveData.postValue(new ObserverObject("edit product", false));
                        }
                    });
        } else {
            StorageReference storageRef = storage.getReference().child(imagePath + documentRef.getId());

            storageRef.putFile(Uri.parse(product.photoPath))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    product.photoPath = uri.toString();

                                    documentRef.set(product)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    mutableLiveData.postValue(new ObserverObject("edit product", true));
                                                }
                                            });
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
    }

    public void getProducts() {
        CollectionReference collectionRef = firestore.collection(collectionName);

        collectionRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Product> productList = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Product product = documentSnapshot.toObject(Product.class);

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
