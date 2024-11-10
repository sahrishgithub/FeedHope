package com.example.feedhope.ProviderInterface.ShoeDonation;

public class ShoeModalClass {
    private String name,type,quantity,condition,gender,size;
    public String getName() { return name; }
    public void setName(String name) {this.name = name;}
    public String getType() { return type; }
    public void setType(String type) {this.type = type;}
    public String getQuantity()
    {
        return quantity;
    }
    public void setQuantity(String quantity) {this.quantity = quantity;}
    public String getCondition() { return condition; }
    public void setCondition(String condition)
    {
        this.condition = condition;
    }
    public String getGender()
    {
        return gender;
    }
    public void setGender(String category) {this.gender = category;}
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public ShoeModalClass(String name,
                           String type,
                           String quantity,
                           String condition,
                           String gender,
                           String size)
    {
        this.name = name;
        this.type = type;
        this.quantity = quantity;
        this.condition = condition;
        this.gender = gender;
        this.size = size;
    }
}