package com.example.feedhope.ReceiverInterface.ReceiverRegister;

public class ReceiverModalClass {
    String reference, type,frequency, email, pass,location,phone;
    int member;
    long cardNo;
    boolean isRejected;

    public ReceiverModalClass(String reference, String type, int member, String frequency, String phone,long cardNo, String email, String pass,String location) {
        this.reference = reference;
        this.type = type;
        this.member = member;
        this.frequency = frequency;
        this.phone = phone;
        this.cardNo = cardNo;
        this.email = email;
        this.pass = pass;
        this.location = location;
    }

    public ReceiverModalClass(String reference, String phone, String email, String pass) {
        this.reference = reference;
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

    public int getMember() {
        return member;
    }

    public void setMember(int member) {
        this.member = member;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public void setCardNo(long cardNo) {
        this.cardNo = cardNo;
    }

    public long getCardNo() {
        return cardNo;
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
