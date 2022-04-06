package com.example.workhubemployee.models;

public class ShiftModel {

    public String date;
    public String employeeId;
    public String employeeName;
    public String shiftEnd;
    public String shiftStart;


    public ShiftModel() {
    }

    public ShiftModel(  String date,String employeeId,String employeeName,String shiftEnd,String shiftStart) {

        this.employeeId = employeeId;
        this.date = date;
        this.employeeName = employeeName;
        this.shiftEnd = shiftEnd;
        this.shiftStart = shiftStart;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getShiftEnd() {
        return shiftEnd;
    }

    public void setShiftEnd(String shiftEnd) {
        this.shiftEnd = shiftEnd;
    }

    public String getShiftStart() {
        return shiftStart;
    }

    public void setShiftStart(String shiftStart) {
        this.shiftStart = shiftStart;
    }
}
