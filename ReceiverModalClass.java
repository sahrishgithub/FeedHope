package com.example.unitconverter.ReceiverInterface;

import android.os.Parcel;
import android.os.Parcelable;

public class ReceiverModalClass implements Parcelable {
    private String reference,type,member,requirement,frequency,time,phone,email,pass;
    public ReceiverModalClass(String reference,String type,String member,String requirement,String frequency,String time, String phone,String email,String pass) {
        this.reference = reference;
        this.type = type;
        this.member = member;
        this.requirement = requirement;
        this.frequency = frequency;
        this.time = time;
        this.phone = phone;
        this.email = email;
        this.pass = pass;
    }

    protected ReceiverModalClass(Parcel in) {
        reference = in.readString();
        type = in.readString();
        member = in.readString();
        requirement = in.readString();
        frequency = in.readString();
        time = in.readString();
        phone = in.readString();
        email = in.readString();
        pass = in.readString();
    }

    public static final Creator<ReceiverModalClass> CREATOR = new Creator<ReceiverModalClass>() {
        @Override
        public ReceiverModalClass createFromParcel(Parcel in) {
            return new ReceiverModalClass(in);
        }

        @Override
        public ReceiverModalClass[] newArray(int size) {
            return new ReceiverModalClass[size];
        }
    };

    public ReceiverModalClass(String reference, String phone, String email, String pass) {
        this.reference = reference;
        this.phone = phone;
        this.email = email;
        this.pass = pass;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReference() {
        return reference;
    }
    public String getType() {
        return type;
    }
    public String getMember() {
        return member;
    }
    public String getRequirement() {
        return requirement;
    }
    public String getFrequency() {
        return frequency;
    }
    public String getTime() {
        return time;
    }
    public String getPhone() {
        return phone;
    }
    public String getEmail() {
        return email;
    }
    public String getPass() {
        return pass;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reference);
        dest.writeString(type);
        dest.writeString(member);
        dest.writeString(requirement);
        dest.writeString(frequency);
        dest.writeString(time);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(pass);
    }
}