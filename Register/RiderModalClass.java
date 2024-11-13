package com.example.feedhope.RiderInterface.Register;

public class RiderModalClass {

    private String name,phone,licence,hours,days,card,email,pass,location;

    public RiderModalClass(String name, String phone, String licence, String hours, String days, String card,String email, String pass,String Location) {
        this.name = name;
        this.phone = phone;
        this.licence = licence;
        this.hours = hours;
        this.days = days;
        this.card = card;
        this.email = email;
        this.pass = pass;
        this.location=Location;
    }

    public RiderModalClass(String name, String phone, String email, String pass,String location) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.pass = pass;
        this.location=location;
    }

    public RiderModalClass(String name1, String phone1, String licence1, String card1, String pass1, String location1) {
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String Location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}