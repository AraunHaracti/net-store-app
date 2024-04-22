package com.example.netstore.models;

import java.util.Date;

public class Employee extends User {
    public Date hireDate;
    public String job;
    public String department;
    public double salary;

    public Employee() {
    }

    public Employee(String _id, String firebaseId, String email, String name, String surname, Date birthday, UserType type) {
        super(_id, firebaseId, email, name, surname, birthday, type);
    }

    public Employee(String _id, String firebaseId, String email, String name, String surname, Date birthday, UserType type, Date hireDate, String job, String department, double salary) {
        super(_id, firebaseId, email, name, surname, birthday, type);
        this.hireDate = hireDate;
        this.job = job;
        this.department = department;
        this.salary = salary;
    }
}
