package com.example.unitconverter.RiderInterface;

import android.os.Parcel;
import android.os.Parcelable;

public class RiderModalClass implements Parcelable {

    private String name,phone,type,idcard,hours,days,card,email,pass;

    public RiderModalClass(String name, String phone,String type,String idcard,String hours,String days,String card,String email,String pass) {
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

    protected RiderModalClass(Parcel in) {
        name = in.readString();
        phone = in.readString();
        type = in.readString();
        idcard = in.readString();
        hours = in.readString();
        days = in.readString();
        card = in.readString();
        email = in.readString();
        pass = in.readString();
    }

    public static final Creator<RiderModalClass> CREATOR = new Creator<RiderModalClass>() {
        @Override
        public RiderModalClass createFromParcel(Parcel in) {
            return new RiderModalClass(in);
        }

        @Override
        public RiderModalClass[] newArray(int size) {
            return new RiderModalClass[size];
        }
    };

    public RiderModalClass(String name, String phone, String email, String pass) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.pass = pass;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getName() {
        return name;
    }
    public String getPhone() {
        return phone;
    }
    public String getType() {
        return type;
    }
    public String getIdcard() {
        return idcard;
    }
    public String getHours() {
        return hours;
    }
    public String getDays() {
        return days;
    }
    public String getCard() {
        return card;
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
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(type);
        dest.writeString(idcard);
        dest.writeString(hours);
        dest.writeString(days);
        dest.writeString(card);
        dest.writeString(email);
        dest.writeString(pass);
    }
}