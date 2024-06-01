package com.example.netstore.models;

import java.util.List;

public class Place {
    public String _id;
    public String name;
    public String description;
    public List<Product> products;

    public Place() {
    }

    public Place(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Place(String _id, String name, String description) {
        this._id = _id;
        this.name = name;
        this.description = description;
    }

    public Place(String _id, String name, String description, List<Product> products) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.products = products;
    }
}
