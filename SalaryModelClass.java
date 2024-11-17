package com.example.feedhope.RiderInterface.SalaryReport;
public class SalaryModelClass {
    private String email,paymentDate;
    private int paymentID,dutyID,salary;

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setDutyID(int dutyID) {
        this.dutyID = dutyID;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public String getEmail() {
        return email;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public int getDutyID() {
        return dutyID;
    }

    public int getSalary() {
        return salary;
    }

    public int getPaymentID() {
        return paymentID;
    }
    public SalaryModelClass(int paymentID,
                            int dutyID,
                            String email,
                            int salary,
                            String paymentDate){
        this.paymentID = paymentID;
        this.dutyID = dutyID;
        this.email = email;
        this.salary = salary;
        this.paymentDate = paymentDate;
    }
}