package com.example.feedhope.ProviderInterface.PaymentDonation;
public class PaymentModalClass {
    String name,amount;

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getAmount() {
        return amount;
    }
    public PaymentModalClass(String name,String amount){
        this.name=name;
        this.amount=amount;
    }
}