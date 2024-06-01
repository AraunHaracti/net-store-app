package com.example.netstore.models;

import com.example.netstore.models.nested.DeliveryDataNested;
import com.example.netstore.models.nested.PaymentDataNested;

import java.util.Date;
import java.util.List;

public class Order {
    public String idUser;
    public double totalPayable;

    public String status;

    public Date dateCreate;
    public Date dateUpdate;

    public DeliveryDataNested deliveryData;
    public PaymentDataNested paymentData;

    public List<Product> productOrderList;

    public Order() {
    }

    public Order(String idUser, double totalPayable, String status, Date dateCreate, Date dateUpdate, DeliveryDataNested deliveryData, PaymentDataNested paymentData, List<Product> productOrderList) {
        this.idUser = idUser;
        this.totalPayable = totalPayable;
        this.status = status;
        this.dateCreate = dateCreate;
        this.dateUpdate = dateUpdate;
        this.deliveryData = deliveryData;
        this.paymentData = paymentData;
        this.productOrderList = productOrderList;
    }
}