package com.example.netstore.models;

import java.util.Date;

public class Employee extends User {
    public Date hireDate;
    public String job;
    public String department;
    public double salary;

    public Employee() {
    }

    public Employee(String firebaseId, String name, String surname, Date birthday, String email, UserType type) {
        super(firebaseId, name, surname, birthday, email, type);
    }

    public Employee(String firebaseId, String name, String surname, Date birthday, String email, UserType type, Date hireDate, String job, String department, double salary) {
        super(firebaseId, name, surname, birthday, email, type);
        this.hireDate = hireDate;
        this.job = job;
        this.department = department;
        this.salary = salary;
    }
}
