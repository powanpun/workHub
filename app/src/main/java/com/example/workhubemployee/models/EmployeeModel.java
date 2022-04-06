package com.example.workhubemployee.models;

public class EmployeeModel {

    public String name;
    public String email;
    public String mobile;
    public String designation;
    public String userId;


    public EmployeeModel() {
    }

    public EmployeeModel(  String name,String email,String mobile,String designation,String userId) {

        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.designation = designation;
        this.userId = userId;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
