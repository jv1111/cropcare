package com.example.cropcare.Model;

public class TaskModel {
    private int id;
    private int userId;
    private String cropName;
    private int cropId;
    private String note;
    private long startTime;
    private long endTime;
    private boolean isRepeat;
    private int repeatEveryDays;

    public TaskModel(int id, int userId, String cropName, int cropId, String note, long startTime, long endTime, boolean isRepeat, int repeatEveryDays) {
        this.id = id;
        this.userId = userId;
        this.cropName = cropName;
        this.cropId = cropId;
        this.note = note;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isRepeat = isRepeat;
        this.repeatEveryDays = repeatEveryDays;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getCropName() {
        return cropName;
    }

    public int getCropId() {
        return cropId;
    }

    public String getNote() {
        return note;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public boolean isRepeat() {
        return isRepeat;
    }

    public int getRepeatEveryDays() {
        return repeatEveryDays;
    }

    @Override
    public String toString() {
        return "TaskModel{id=" + id + ", userId=" + userId + ", cropName='" + cropName + "', cropId=" + cropId +
                ", note='" + note + "', startTime=" + startTime + ", endTime=" + endTime +
                ", isRepeat=" + isRepeat + ", repeatEveryDays=" + repeatEveryDays + "}";
    }
}
