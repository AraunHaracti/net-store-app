package com.example.netstore.models.nested;

public class ProductNested {
    public String idProduct;
    public String name;
    public double price;
    public int count;

    public ProductNested() {
    }

    public ProductNested(String idProduct, String name, double price, int count) {
        this.idProduct = idProduct;
        this.name = name;
        this.price = price;
        this.count = count;
    }
}
