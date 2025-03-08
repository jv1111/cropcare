package com.example.cropcare.Model;

public class CoFarmerModel {
    private int id;
    private int parentUserId;
    private String username;
    private String password;

    public CoFarmerModel(int id, int parentUserId, String username, String password) {
        this.id = id;
        this.parentUserId = parentUserId;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentUserId() {
        return parentUserId;
    }

    public void setParentUserId(int parentUserId) {
        this.parentUserId = parentUserId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "CoFarmerModel{" +
                "id=" + id +
                ", parentUserId=" + parentUserId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
