package com.example.unitconverter.ProviderInterface;

import android.os.Parcel;
import android.os.Parcelable;

public class ProviderModalClass implements Parcelable {

    private String name,phone,email,pass;

    public ProviderModalClass(String name, String phone,String email,String pass) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.pass = pass;
    }

    protected ProviderModalClass(Parcel in) {
        name = in.readString();
        phone = in.readString();
        email = in.readString();
        pass = in.readString();
    }

    public static final Creator<ProviderModalClass> CREATOR = new Creator<ProviderModalClass>() {
        @Override
        public ProviderModalClass createFromParcel(Parcel in) {
            return new ProviderModalClass(in);
        }

        @Override
        public ProviderModalClass[] newArray(int size) {
            return new ProviderModalClass[size];
        }
    };

    public void setName(String name){
        this.name=name;
    }
    public void setPhone(String phone){
        this.phone=phone;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public void setPass(String pass){
        this.pass=pass;
    }
    public String getName() {
        return name;
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
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(pass);
    }
}