package com.example.netstore.models;

import android.net.Uri;

import java.net.URI;

public class Product {
    public String name;
    public String description;
    public Uri photoUri;
    public Double price;
    public int count;

    public Product(String name, String description, Uri photoUri, Double price, int count) {
        this.name = name;
        this.description = description;
        this.photoUri = photoUri;
        this.price = price;
        this.count = count;
    }
}
