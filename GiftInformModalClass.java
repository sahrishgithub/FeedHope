package com.example.feedhope.ReceiverInterface.GiftInform;

public class GiftInformModalClass {
    private String name,quantity,status,category,condition;
    public String getName() { return name; }
    public void setName(String name) {this.name = name;}
    public String getQuantity()
    {
        return quantity;
    }
    public void setQuantity(String quantity) {this.quantity = quantity;}
    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
    public String getCondition() {
        return condition;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public GiftInformModalClass(String name,
                                String quantity,
                                String category,
                                String condition,
                                String status)
    {
        this.name = name;
        this.quantity = quantity;
        this.category = category;
        this.condition = condition;
        this.status = status;
    }
}