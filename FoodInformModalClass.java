package com.example.feedhope.ReceiverInterface.FoodInform;

public class FoodInformModalClass {
    private String name,quantity,storage,expire,status;
    public String getName() { return name; }
    public void setName(String name) {this.name = name;}
    public String getQuantity()
    {
        return quantity;
    }
    public void setQuantity(String quantity) {this.quantity = quantity;}
    public String getStorage() { return storage; }
    public void setStorage(String storage)
    {
        this.storage = storage;
    }
    public String getExpire()
    {
        return expire;
    }
    public void setExpire(String expire) {this.expire = expire;}
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public FoodInformModalClass(String name,
                                String quantity,
                                String storage,
                                String expire,
                                String status)
    {
        this.name = name;
        this.quantity = quantity;
        this.storage = storage;
        this.expire = expire;
        this.status = status;
    }
}