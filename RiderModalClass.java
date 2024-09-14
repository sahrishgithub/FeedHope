package com.example.unitconverter.RiderInterface;

public class RiderModalClass {

    private String name;
    private String phone;
    private String type;
    private String idcard;
    private String hours;
    private String days;
    private String card;
    private String email;
    private String pass;

    // Constructor with all fields
    public RiderModalClass(String name, String phone, String type, String idcard, String hours, String days, String card, String email, String pass) {
        this.name = name;
        this.phone = phone;
        this.type = type;
        this.idcard = idcard;
        this.hours = hours;
        this.days = days;
        this.card = card;
        this.email = email;
        this.pass = pass;
    }

    // Constructor with essential fields
    public RiderModalClass(String name, String phone, String email, String pass) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.pass = pass;
    }

    // Getters and Setters
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
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