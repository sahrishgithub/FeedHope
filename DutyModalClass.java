package com.example.feedhope.RiderInterface.Duty;
public class DutyModalClass {
    private String name,pick,drop,date,status;
    private int completedCount,pendingCount,salary,DutyID;
    public void setSalary(int salary) {
        this.salary = salary;
    }
    public int getSalary() {
        return salary;
    }
    public void setDutyID(int dutyID) {
        DutyID = dutyID;
    }
    public int getDutyID() {
        return DutyID;
    }
    public void setCompletedCount(int completedCount) {
        this.completedCount = completedCount;
    }
    public void setPendingCount(int pendingCount) {
        this.pendingCount = pendingCount;
    }
    public int getCompletedCount() {
        return completedCount;
    }
    public int getPendingCount() {
        return pendingCount;
    }
    public String getName() { return name; }
    public void setName(String name) {this.name = name;}
    public String getPick()
    {
        return pick;
    }
    public void setPick(String pick) {this.pick = pick;}
    public String getDrop() { return drop; }
    public void setDrop(String drop)
    {
        this.drop = drop;
    }
    public String getDate()
    {
        return date;
    }
    public void setDate(String date) {this.date = date;}
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public DutyModalClass(int DutyID,
                          String name,
                          String pick,
                          String drop,
                          String date,
                          String status)
    {
        this.DutyID=DutyID;
        this.name = name;
        this.pick = pick;
        this.drop = drop;
        this.date = date;
        this.status = status;
    }
}