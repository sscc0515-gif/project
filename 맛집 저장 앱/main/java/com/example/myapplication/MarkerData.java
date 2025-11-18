package com.example.myapplication;

public class MarkerData {
    public String id;        // Firebase 키
    public String name;
    public double latitude;
    public double longitude;

    public MarkerData() { }

    public MarkerData(String id, String name, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}

