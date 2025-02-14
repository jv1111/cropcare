package com.example.cropcare.Model;

public class CropModel {
    private int id;
    private String date;
    private String name;

    public CropModel(int id, String date, String name) {
        this.id = id;
        this.date = date;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "CropModel{id=" + id + ", date='" + date + "', name='" + name + "'}";
    }
}
