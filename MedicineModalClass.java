package com.example.feedhope.ProviderInterface.MedicineDonation;

public class MedicineModalClass {
    private String medicineName,name,form,quantity,condition,manufacture,expire;
    public String getName(){return name;}
    public void setName(String name){this.name = name;}
    public String getMedicineName(){return medicineName;}
    public void setMedicineName(String medicineName){this.medicineName = medicineName;}
    public String getForm() { return form; }
    public void setForm(String form) {this.form = form;}
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
    public String getManufacture()
    {
        return manufacture;
    }
    public void setManufacture(String manufacture) {this.manufacture = manufacture;}
    public String getExpire() { return expire; }
    public void setExpire(String expire) { this.expire = expire; }
    public MedicineModalClass(String name,
                           String medicineName,
                           String form,
                           String quantity,
                           String condition,
                           String manufacture,
                           String expire)
    {
        this.name = name;
        this.medicineName=medicineName;
        this.form = form;
        this.quantity = quantity;
        this.condition = condition;
        this.manufacture = manufacture;
        this.expire = expire;
    }
}