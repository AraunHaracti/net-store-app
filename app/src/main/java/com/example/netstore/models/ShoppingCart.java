package com.example.netstore.models;

import java.util.List;

public class ShoppingCart {
    public String idUser;
    public int totalPayable;
    public List<CartItem> cartItems;

    public ShoppingCart() {
    }

    public ShoppingCart(String idUser, int totalPayable, List<CartItem> cartItems) {
        this.idUser = idUser;
        this.totalPayable = totalPayable;
        this.cartItems = cartItems;
    }

    public class CartItem {
        public int idProduct;
        public String name;
        public double price;
        public int count;

        public CartItem(int idProduct, String name, double price, int count) {
            this.idProduct = idProduct;
            this.name = name;
            this.price = price;
            this.count = count;
        }
    }
}

