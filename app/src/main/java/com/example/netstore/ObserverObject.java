package com.example.netstore;

public class ObserverObject {
    public String tag;
    public boolean status;
    public Object item;

    public ObserverObject(String tag, boolean status, Object item) {
        this.tag = tag;
        this.status = status;
        this.item = item;
    }

    public ObserverObject(String tag, boolean status) {
        this.tag = tag;
        this.status = status;
    }
}
