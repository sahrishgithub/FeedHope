package com.example.feedhope.ProviderInterface.ToyDonation;

public class ToyModalClass {
    private String name,toyName,age,quantity,condition,category;
    public String getName(){return name;}
    public void setName(String name){this.name = name;}
    public String getToyName(){return toyName;}
    public void setToyName(String toyName){this.toyName = toyName;}
    public String getAge() { return age; }
    public void setAge(String age) {this.age = age;}
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
    public String getCategory()
    {
        return category;
    }
    public void setCategory(String category) {this.category = category;}
    public ToyModalClass(String name,
                           String toyName,
                           String age,
                           String quantity,
                           String condition,
                           String category)
    {
        this.name = name;
        this.toyName=toyName;
        this.age = age;
        this.quantity = quantity;
        this.condition = condition;
        this.category = category;
    }
}