package com.example.workhubemployee.models;

public class PostModel {
    public String user;
    public String data;

    public PostModel(){

    }
    public PostModel(String user, String data){
        this.user = user;
        this.data = data;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
