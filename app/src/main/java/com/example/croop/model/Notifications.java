package com.example.croop.model;

import java.sql.Date;

public class Notifications {
    private int id;
    private String userName;
    private String userType;
    private int userID;
    private Date date;
    private String message;
    private String about;

    public Notifications(){

    }

    public Notifications(int id, String userName, String userType, int userID, Date date, String message, String about){
        this.id = id;
        this.userName = userName;
        this.userID = userID;
        this.userType = userType;
        this.date = date;
        this.message = message;
        this.about = about;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserType() {
        return userType;
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public Date getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getAbout() {
        return about;
    }
    public void setAbout(String about) {
        this.about = about;
    }
}
