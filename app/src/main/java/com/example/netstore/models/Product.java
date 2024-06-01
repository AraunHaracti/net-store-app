package com.example.netstore.models;

import android.media.Image;
import android.net.Uri;

import java.net.URI;

public class Product {
    public String _id;
    public String name;
    public String description;
    public String photoPath;
    public Double price;
    public int count;

    public Product() {
    }

    public Product(String _id, String name, Double price, int count) {
        this._id = _id;
        this.name = name;
        this.price = price;
        this.count = count;
    }

    public Product(String name, String description, String photoPath, Double price, int count) {
        this.name = name;
        this.description = description;
        this.photoPath = photoPath;
        this.price = price;
        this.count = count;
    }

    public Product(String _id, String name, String description, String photoPath, Double price, int count) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.photoPath = photoPath;
        this.price = price;
        this.count = count;
    }
}
