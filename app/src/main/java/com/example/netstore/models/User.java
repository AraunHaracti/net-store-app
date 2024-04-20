package com.example.netstore.models;

import java.util.Date;

public class User {
    public String id;
    public String firebaseId;
    public String login;
    public String password;
    public String name;
    public String surname;
    public Date birthday;
    public String email;
    public String phone;
    public UserType type;

    public User() {
    }

    public User(String login,
                String password,
                String name,
                String surname,
                Date birthday,
                String email,
                String phone,
                UserType type) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.email = email;
        this.phone = phone;
        this.type = type;
    }

    public User(String id,
                String firebaseId,
                String login,
                String password,
                String name,
                String surname,
                Date birthday,
                String email,
                String phone,
                UserType type) {
        this.id = id;
        this.firebaseId = firebaseId;
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.email = email;
        this.phone = phone;
        this.type = type;
    }

    public enum UserType {
        Client,
        Employee
    }
}
