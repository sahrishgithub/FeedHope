package com.example.feedhope.ProviderInterface.ClothDonation;

public class ClothModelClass {
    private String name,type,quantity,condition,category,size,seasonal;
    public String getName(){return name;}
    public void setName(String name){this.name = name;}
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
    public String getCategory()
    {
        return category;
    }
    public void setCategory(String category) {this.category = category;}
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public String getSeasonal() { return seasonal; }
    public void setSeasonal(String seasonal) { this.seasonal = seasonal; }
    public ClothModelClass(String name,
                           String type,
                           String condition,
                           String quantity,
                           String category,
                           String seasonal,
                           String size)
    {
        this.name = name;
        this.type = type;
        this.condition = condition;
        this.quantity = quantity;
        this.category = category;
        this.seasonal = seasonal;
        this.size = size;
    }
}