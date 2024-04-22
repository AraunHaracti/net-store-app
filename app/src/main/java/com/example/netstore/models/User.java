package com.example.netstore.models;

import java.util.Date;

public class User {
    public String _id;
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

    public User(String _id, String firebaseId, String email, String name, String surname, Date birthday, UserType type) {
        this._id = _id;
        this.firebaseId = firebaseId;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.type = type;
    }

    public enum UserType {
        Client,
        Employee
    }
}
