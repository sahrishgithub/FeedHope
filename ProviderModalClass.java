package com.example.feedhope.ProviderInterface.ProviderRegister;

public class ProviderModalClass {
    String name,phone,email,pass,location;
    public ProviderModalClass(String name, String phone, String email, String pass,String location) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.pass = pass;
        this.location = location;
    }
    public ProviderModalClass(String name, String phone, String email, String pass) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.pass = pass;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public String getLocation() {
        return location;
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