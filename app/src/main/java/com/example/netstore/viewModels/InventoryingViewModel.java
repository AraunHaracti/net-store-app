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

import java.util.ArrayList;
import java.util.List;

public class InventoryingViewModel {
    private MutableLiveData<ObserverObject> mutableLiveData = new MutableLiveData<>();
    public LiveData<ObserverObject> getInfoData() {
        return mutableLiveData;
    }

    private FirebaseFirestore firestore;

    public InventoryingViewModel() {
        firestore = FirebaseFirestore.getInstance();
    }

    public void receiveInventorying(Place place, Product product, int count) {
        DocumentReference documentReference = firestore.collection("places").document(place._id);

        firestore.collection("products").document(product._id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                int countProducts = documentSnapshot.toObject(Product.class).count + count;

                firestore.collection("products").document(product._id).update("count", countProducts).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        firestore.runTransaction(transaction -> {
                            DocumentSnapshot documentSnapshot = transaction.get(documentReference);
                            List<Product> productNestedList = documentSnapshot.toObject(Place.class).products;

                            boolean addFlag = false;
                            if (productNestedList != null) {
                                for (int i = 0; i < productNestedList.size(); i++) {
                                    if (productNestedList.get(i)._id.equals(product._id)) {
                                        productNestedList.get(i).count += count;
                                        addFlag = true;
                                        break;
                                    }
                                }
                            } else {
                                productNestedList = new ArrayList<>();
                            }
                            if (!addFlag) {
                                productNestedList.add(new Product(product._id, product.name, product.price, count));
                            }

                            transaction.update(documentReference, "products", productNestedList);

                            mutableLiveData.postValue(new ObserverObject("receive", true));
                            return null;
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mutableLiveData.postValue(new ObserverObject("receive", false));
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mutableLiveData.postValue(new ObserverObject("receive", false));
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mutableLiveData.postValue(new ObserverObject("receive", false));
            }
        });
    }

    public void sendInventorying(Place place, Product product, int count) {
        DocumentReference documentReference = firestore.collection("places").document(place._id);

        firestore.collection("products").document(product._id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                int countProducts = documentSnapshot.toObject(Product.class).count - count;

                firestore.collection("products").document(product._id).update("count", countProducts).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        firestore.runTransaction(transaction -> {
                            DocumentSnapshot documentSnapshot = transaction.get(documentReference);
                            List<Product> productNestedList = documentSnapshot.toObject(Place.class).products;

                            boolean addFlag = false;
                            if (productNestedList != null) {
                                for (int i = 0; i < productNestedList.size(); i++) {
                                    if (productNestedList.get(i)._id.equals(product._id)) {
                                        productNestedList.get(i).count -= count;
                                        addFlag = true;
                                        break;
                                    }
                                }
                            } else {
                                productNestedList = new ArrayList<>();
                            }
                            if (!addFlag) {
                                productNestedList.add(new Product(product._id, product.name, product.price, count));
                            }

                            transaction.update(documentReference, "products", productNestedList);

                            mutableLiveData.postValue(new ObserverObject("send", true));
                            return null;
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mutableLiveData.postValue(new ObserverObject("send", false));
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mutableLiveData.postValue(new ObserverObject("send", false));
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mutableLiveData.postValue(new ObserverObject("send", false));
            }
        });
    }

    public void moveInventorying(Place placeBegin, Place placeEnd, Product product, int count) {
        DocumentReference documentReferenceBegin = firestore.collection("places").document(placeBegin._id);
        DocumentReference documentReferenceEnd = firestore.collection("places").document(placeEnd._id);

        firestore.runTransaction(transaction_1 -> {
            List<Product> productNestedListBegin = transaction_1.get(documentReferenceBegin).toObject(Place.class).products;

            for (int i = 0; i < productNestedListBegin.size(); i++) {
                if (productNestedListBegin.get(i)._id.equals(product._id)) {
                    productNestedListBegin.get(i).count -= count;
                    break;
                }
            }

            transaction_1.update(documentReferenceBegin, "products", productNestedListBegin);


            firestore.runTransaction(transaction_2 -> {
                List<Product> productNestedListEnd = transaction_2.get(documentReferenceEnd).toObject(Place.class).products;

                boolean addFlag = false;
                if (productNestedListEnd != null) {
                    for (int i = 0; i < productNestedListEnd.size(); i++) {
                        if (productNestedListEnd.get(i)._id.equals(product._id)) {
                            productNestedListEnd.get(i).count += count;
                            addFlag = true;
                            break;
                        }
                    }
                } else {
                    productNestedListEnd = new ArrayList<>();
                }
                if (!addFlag) {
                    productNestedListEnd.add(new Product(product._id, product.name, product.price, count));
                }

                transaction_2.update(documentReferenceEnd, "products", productNestedListEnd);

                mutableLiveData.postValue(new ObserverObject("move", true));
                return null;
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mutableLiveData.postValue(new ObserverObject("move", false));
                }
            });

            return null;
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mutableLiveData.postValue(new ObserverObject("move", false));
            }
        });
    }
}
