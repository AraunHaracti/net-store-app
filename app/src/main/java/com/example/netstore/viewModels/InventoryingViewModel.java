package com.example.netstore.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.netstore.config.ObserverObject;
import com.example.netstore.models.Place;
import com.example.netstore.models.Product;

public class InventoryingViewModel {
    private MutableLiveData<ObserverObject> mutableLiveData = new MutableLiveData<>();
    public LiveData<ObserverObject> getInfoData() {
        return mutableLiveData;
    }

    public void receiveInventorying(Place place, Product product, int count) {
        mutableLiveData.postValue(new ObserverObject("receive", false));
    }

    public void sendInventorying(Place place, Product product, int count) {
        mutableLiveData.postValue(new ObserverObject("send", false));
    }

    public void moveInventorying(Place placeBegin, Place placeEnd, Product product, int count) {
        mutableLiveData.postValue(new ObserverObject("move", false));
    }
}
