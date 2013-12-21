package com.github.drinking_buddies.entities;

//Address POJO
public class Address {
    
    private int id;
    private String street;
    private String number;
    private String zipCode;
    private String city;
    private String country;
    
    
    
    public Address(int id,String street, String number, String zipCode, String city,
            String country) {
        this.street = street;
        this.number = number;
        this.zipCode = zipCode;
        this.city = city;
        this.country = country;
    }
    
    public String getStreet() {
        return street;
    }
    public String getNumber() {
        return number;
    }
    public String getZipCode() {
        return zipCode;
    }
    public String getCity() {
        return city;
    }
    public String getCountry() {
        return country;
    }
    
    
}
