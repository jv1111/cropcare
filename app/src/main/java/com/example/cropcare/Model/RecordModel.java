package com.example.cropcare.Model;

public class RecordModel {
    private int id;
    private int userId;
    private int cropId;
    private String cropName;
    private int taskId;
    private String note;
    private String status;
    private long time;

    public RecordModel(int id, int userId, int cropId, String cropName, int taskId, String note, String status, long time) {
        this.id = id;
        this.userId = userId;
        this.cropId = cropId;
        this.cropName = cropName;
        this.taskId = taskId;
        this.note = note;
        this.status = status;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCropId() {
        return cropId;
    }

    public void setCropId(int cropId) {
        this.cropId = cropId;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "RecordModel{" +
                "id=" + id +
                ", userId=" + userId +
                ", cropId=" + cropId +
                ", cropName='" + cropName + '\'' +
                ", taskId=" + taskId +
                ", note='" + note + '\'' +
                ", status='" + status + '\'' +
                ", time=" + time +
                '}';
    }
}
