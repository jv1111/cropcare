package com.example.cropcare.Model;

public class TaskModel {
    private int id;
    private String date;
    private int cropId;
    private String cropName;
    private String note;

    public TaskModel(int id, String date, int cropId, String cropName, String note) {
        this.id = id;
        this.date = date;
        this.cropId = cropId;
        this.cropName = cropName;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public int getCropId() {
        return cropId;
    }

    public String getCropName() {
        return cropName;
    }

    public String getNote() {
        return note;
    }

    @Override
    public String toString() {
        return "TaskModel{id=" + id + ", date='" + date + "', cropId=" + cropId +
                ", cropName='" + cropName + "', note='" + note + "'}";
    }
}
