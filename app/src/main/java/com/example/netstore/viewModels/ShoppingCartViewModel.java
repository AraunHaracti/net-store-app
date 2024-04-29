package com.example.netstore.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.netstore.config.ObserverObject;
import com.example.netstore.models.Product;
import com.example.netstore.models.ShoppingCart;
import com.example.netstore.models.User;
import com.example.netstore.models.nested.ProductNested;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ShoppingCartViewModel {
    private MutableLiveData<ObserverObject> mutableLiveData = new MutableLiveData<>();
    public LiveData<ObserverObject> getInfoData() {
        return mutableLiveData;
    }

    private FirebaseFirestore firestore;

    private String collectionName = "carts";

    public ShoppingCartViewModel() {
        firestore = FirebaseFirestore.getInstance();
    }

    public void addProductInShoppingCart(User user, Product product) {
        firestore.collection(collectionName).document(user._id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ShoppingCart shoppingCart = documentSnapshot.toObject(ShoppingCart.class);

                        shoppingCart.cartItems.add(new ProductNested(product._id, product.name, product.price, product.count));

                        double total = 0;
                        for (ProductNested productNested : shoppingCart.cartItems) {
                            total += productNested.count * productNested.price;
                        }
                        shoppingCart.totalPayable = total;

                        firestore.collection(collectionName).document(user._id).set(shoppingCart)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        mutableLiveData.postValue(new ObserverObject("add product cart", true));
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mutableLiveData.postValue(new ObserverObject("add product cart", false));
                    }
                });
    }

    public void createShoppingCart(User user) {
        firestore.collection(collectionName).document(user._id)
                .set(new ShoppingCart(user._id, 0, new ArrayList<>()))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        mutableLiveData.postValue(new ObserverObject("create cart", true));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mutableLiveData.postValue(new ObserverObject("create cart", false));
                    }
                });
    }
}
