package com.example.netstore.models;

import com.example.netstore.models.nested.ProductNested;

import java.util.List;

public class ShoppingCart {
    public String idUser;
    public double totalPayable;
    public List<ProductNested> cartItems;

    public ShoppingCart() {
    }

    public ShoppingCart(String idUser, int totalPayable, List<ProductNested> cartItems) {
        this.idUser = idUser;
        this.totalPayable = totalPayable;
        this.cartItems = cartItems;
    }
}

