package com.example.feedhope.ReceiverInterface.ReceiverRegister;

public class ReceiverModalClass {
    private String reference, type, member, requirement, frequency, time, phone, email, pass,location;
    private boolean isRejected;

    public ReceiverModalClass(String reference, String type, String member, String requirement, String frequency, String time, String phone, String email, String pass,String location) {
        this.reference = reference;
        this.type = type;
        this.member = member;
        this.requirement = requirement;
        this.frequency = frequency;
        this.time = time;
        this.phone = phone;
        this.email = email;
        this.pass = pass;
        this.location=location;
    }

    public ReceiverModalClass(String reference, String phone, String email, String pass,String location) {
        this.reference = reference;
        this.phone = phone;
        this.email = email;
        this.pass = pass;
        this.location=location;
    }

    // Getters and setters
    public String getLocation() {
        return location;
    }

    public void setLocation(String Location) {
        this.location = location;
    }
    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public boolean isRejected() {
        return isRejected;
    }

    public void setRejected(boolean rejected) {
        isRejected = rejected;
    }
}
