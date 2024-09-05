package com.example.unitconverter.RiderInterface;

public class DutyModalClass {
    private String name,pick,drop,date,status;
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

    public DutyModalClass(String name,
                          String pick,
                          String drop,
                          String date,
                          String status)
    {
        this.name = name;
        this.pick = pick;
        this.drop = drop;
        this.date = date;
        this.status = status;
    }
}