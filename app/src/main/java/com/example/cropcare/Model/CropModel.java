package com.example.cropcare.Model;

public class CropModel {
    private int id;
    private long date;
    private String name;

    public CropModel(int id, long date, String name) {
        this.id = id;
        this.date = date;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public long getDate() {
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
