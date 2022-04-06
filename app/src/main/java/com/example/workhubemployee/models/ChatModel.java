package com.example.workhubemployee.models;

public class ChatModel {

    public String employeeId;
    public String from;
    public String to;
    public String message;


    public ChatModel() {
    }

    public ChatModel(  String employeeId,String from,String to,String message) {

        this.employeeId = employeeId;
        this.from = from;
        this.to = to;
        this.message = message;

    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
