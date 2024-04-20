package com.example.netstore.models;

import java.util.Date;

public class User {
    public String firebaseId;
    public String email;
    public String name;
    public String surname;
    public Date birthday;
    public UserType type;

    public User() {
    }

    public User(String name,
                String surname,
                Date birthday,
                String email,
                UserType type) {
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.email = email;
        this.type = type;
    }

    public User(String firebaseId,
                String name,
                String surname,
                Date birthday,
                String email,
                UserType type) {
        this.firebaseId = firebaseId;
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.email = email;
        this.type = type;
    }

    public enum UserType {
        Client,
        Employee
    }
}
