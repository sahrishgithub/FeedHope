package com.example.feedhope.RiderInterface.Register;

public class RiderModalClass {

    String name,phone,licence,hours,email,pass,location;
    long card;

    public RiderModalClass(String name, String phone, String licence, String hours,long card,String email, String pass, String location) {
        this.name = name;
        this.phone = phone;
        this.licence = licence;
        this.hours = hours;
        this.card = card;
        this.email = email;
        this.pass = pass;
        this.location = location;
    }

    public RiderModalClass(String name, String phone, String email, String pass) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.pass = pass;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
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

    public long getCard() {
        return card;
    }

    public void setCard(long card) {
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