package com.example.netstore.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.netstore.config.ObserverObject;
import com.example.netstore.models.Order;
import com.example.netstore.models.ShoppingCart;
import com.example.netstore.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderViewModel {
    private MutableLiveData<ObserverObject> mutableLiveData = new MutableLiveData<>();
    public LiveData<ObserverObject> getInfoData() {
        return mutableLiveData;
    }

    private FirebaseFirestore firestore;

    private String collectionName = "orders";

    public OrderViewModel() {
        firestore = FirebaseFirestore.getInstance();
    }

    public void getOrders(User user) {
        firestore.collection(collectionName).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Order> orderList = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.getString("idUser").equals(user._id))
                                orderList.add(documentSnapshot.toObject(Order.class));
                        }
                        mutableLiveData.postValue(new ObserverObject("get orders", true, orderList));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mutableLiveData.postValue(new ObserverObject("get orders", false));
                    }
                });
    }

    public void getOrders() {
        firestore.collection(collectionName).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Order> orderList = new ArrayList<>();
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    orderList.add(documentSnapshot.toObject(Order.class));
                }
                mutableLiveData.postValue(new ObserverObject("get orders", true, orderList));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mutableLiveData.postValue(new ObserverObject("get orders", false));
            }
        });
    }

    public void formOrder(ShoppingCart shoppingCart) {
        Order order = new Order();
        order.idUser = shoppingCart.idUser;
        order.totalPayable = shoppingCart.totalPayable;
        order.status = "Создан";
        order.dateCreate = Date.from(java.time.LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        order.dateUpdate = Date.from(java.time.LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        order.productOrderList = shoppingCart.cartItems;

        firestore.collection(collectionName).document().set(order)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        mutableLiveData.postValue(new ObserverObject("form order", true));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mutableLiveData.postValue(new ObserverObject("form order", false));
                    }
                });
    }
}
