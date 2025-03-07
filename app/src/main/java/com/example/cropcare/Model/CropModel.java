package com.example.cropcare.Model;

public class CropModel {
    private int id;
    private int userId;
    private long date;
    private String name;

    public CropModel(int id, int userId, long date, String name) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public long getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "CropModel{id=" + id + ", userId=" + userId + ", date='" + date + "', name='" + name + "'}";
    }
}
