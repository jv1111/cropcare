package com.example.cropcare.Model;

public class RVModelCropInfoModel {
    private String date;
    private String cropName;

    public RVModelCropInfoModel(String date, String cropName) {
        this.date = date;
        this.cropName = cropName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    @Override
    public String toString() {
        return "RVModelCropInfoModel{" +
                "date='" + date + '\'' +
                ", cropName='" + cropName + '\'' +
                '}';
    }

}
