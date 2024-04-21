package com.example.netstore.models;

import java.util.Date;
import java.util.List;

public class Order {
    public String idUser;
    public double totalPayable;

    public String status;

    public Date dateCreate;
    public Date dateUpdate;

    public DeliveryData deliveryData;
    public PaymentData paymentData;

    public List<OrderItem> productOrderList;

    public Order() {
    }

    public class OrderItem {
        public int idProduct;
        public String name;
        public double price;
        public int count;

        public OrderItem(int idProduct, String name, double price, int count) {
            this.idProduct = idProduct;
            this.name = name;
            this.price = price;
            this.count = count;
        }
    }

    public class DeliveryData {
        public String address;
        public String typeDelivery;

        public DeliveryData(String address, String typeDelivery) {
            this.address = address;
            this.typeDelivery = typeDelivery;
        }
    }

    public class PaymentData {
        public String type;
        public String status;

        public PaymentData(String type, String status) {
            this.type = type;
            this.status = status;
        }
    }
}