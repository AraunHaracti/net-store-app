package com.example.netstore.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.netstore.config.ObserverObject;
import com.example.netstore.models.Product;
import com.example.netstore.models.ShoppingCart;
import com.example.netstore.models.User;
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

    public void getShoppingCart(User user) {
        firestore.collection(collectionName).document(user._id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ShoppingCart shoppingCart = documentSnapshot.toObject(ShoppingCart.class);
                        mutableLiveData.postValue(new ObserverObject("get shopping cart", true, shoppingCart));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mutableLiveData.postValue(new ObserverObject("get shopping cart", false));
                    }
                });
    }

    public void formOrder(ShoppingCart shoppingCart) {
        OrderViewModel orderViewModel = new OrderViewModel();

        orderViewModel.getInfoData().observeForever(new Observer<ObserverObject>() {
            @Override
            public void onChanged(ObserverObject observerObject) {
                if (observerObject.tag == "form order" && observerObject.status) {
                    shoppingCart.cartItems = new ArrayList<>();
                    firestore.collection(collectionName).document(shoppingCart.idUser).set(shoppingCart);
                }
                mutableLiveData.postValue(observerObject);
            }
        });

        orderViewModel.formOrder(shoppingCart);
    }

    public void addProductInShoppingCart(User user, Product product) {
        firestore.collection(collectionName).document(user._id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ShoppingCart shoppingCart = documentSnapshot.toObject(ShoppingCart.class);

                        shoppingCart.cartItems.add(new Product(product._id, product.name, product.price, product.count));

                        double total = 0;
                        for (Product productNested : shoppingCart.cartItems) {
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
