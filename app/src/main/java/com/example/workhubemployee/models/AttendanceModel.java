package com.example.workhubemployee.models;

public class AttendanceModel {

    public String userId;
    public String date;
    public String inTime;
    public String outTime;

    public AttendanceModel(){

    }
    public AttendanceModel(String userId,String date,String inTime,String outTime){
        this.userId = userId;
        this.date = date;
        this.inTime = inTime;
        this.outTime = outTime;

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }
}
