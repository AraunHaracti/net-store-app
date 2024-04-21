package com.example.netstore.models;

import java.util.Date;
import java.util.List;

public class Client extends User {
    public String address;
    public List<Order> orders;

    public Client() {
    }

    public Client(String firebaseId, String name, String surname, Date birthday, String email, UserType type) {
        super(firebaseId, name, surname, birthday, email, type);
    }

    public Client(String firebaseId, String name, String surname, Date birthday, String email, UserType type, String address, List<Order> orders) {
        super(firebaseId, name, surname, birthday, email, type);
        this.address = address;
        this.orders = orders;
    }
}
