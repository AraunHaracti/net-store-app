package com.example.netstore.models;

import java.util.Date;
import java.util.List;

public class Client extends User {
    public String address;
    public List<Order> orders;

    public Client() {
    }

    public Client(String _id, String firebaseId, String email, String name, String surname, Date birthday, UserType type) {
        super(_id, firebaseId, email, name, surname, birthday, type);
    }

    public Client(String _id, String firebaseId, String email, String name, String surname, Date birthday, UserType type, String address, List<Order> orders) {
        super(_id, firebaseId, email, name, surname, birthday, type);
        this.address = address;
        this.orders = orders;
    }
}
