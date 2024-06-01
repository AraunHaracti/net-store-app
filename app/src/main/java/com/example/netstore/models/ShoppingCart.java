package com.example.netstore.models;

import java.util.List;

public class ShoppingCart {
    public String idUser;
    public double totalPayable;
    public List<Product> cartItems;

    public ShoppingCart() {
    }

    public ShoppingCart(String idUser, int totalPayable, List<Product> cartItems) {
        this.idUser = idUser;
        this.totalPayable = totalPayable;
        this.cartItems = cartItems;
    }
}

