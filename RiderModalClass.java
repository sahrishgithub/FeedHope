package com.example.unitconverter.RiderInterface;

public class RiderModalClass {

    private String name;
    private String phone;
    private String type;
    private String idcard;
    private String hours;
    private String days;
    private String card;
    private String email;
    private String pass;
    private double latitude;    // Field for latitude
    private double longitude;   // Field for longitude
    private String locationName; // Field for location name

    // Constructor with all fields (including location)
    public RiderModalClass(String name, String phone, String type, String idcard, String hours, String days,
                           String card, String email, String pass, double latitude, double longitude, String locationName) {
        this.name = name;
        this.phone = phone;
        this.type = type;
        this.idcard = idcard;
        this.hours = hours;
        this.days = days;
        this.card = card;
        this.email = email;
        this.pass = pass;
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationName = locationName;
    }

    // Constructor with essential fields (without location data)
    public RiderModalClass(String name, String phone, String email, String pass) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.pass = pass;
        this.latitude = 0.0;   // Default value for latitude
        this.longitude = 0.0;  // Default value for longitude
        this.locationName = ""; // Default empty location name
    }

    // Getters and Setters for all fields
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    // Getters and Setters for latitude, longitude, and locationName
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getPassword() {
        return pass;
    }

    // Renamed these getters to match field names
    public String getIdType() {
        return type;
    }

    public String getIdNumber() {
        return idcard;
    }

    public String getWorkingHours() {
        return hours;
    }

    public String getAvailabilityDays() {
        return days;
    }

    public String getBankDetail() {
        return card;
    }
}
