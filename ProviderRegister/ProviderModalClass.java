package com.example.feedhope.ProviderInterface.ProviderRegister;


public class ProviderModalClass {

    private String name;
    private String phone;
    private String email;
    private String pass;
    private String location;

    // Constructor with all fields
    public ProviderModalClass(String name, String phone, String email, String pass,String location) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.pass = pass;
        this.location=location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String Location) {
        this.location = location;
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