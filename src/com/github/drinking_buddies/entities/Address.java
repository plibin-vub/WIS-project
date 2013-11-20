package com.github.drinking_buddies.entities;

import org.jooq.Record;

import static com.github.drinking_buddies.jooq.Tables.ADDRESS;

public class Address {
    private String street;
    private String number;
    private String city;
    private String zip;
    private String country;
    
    public Address(Record r) {
        this.street = r.getValue(ADDRESS.STREET);
        this.number = r.getValue(ADDRESS.NUMBER);
        this.city = r.getValue(ADDRESS.CITY);
        this.zip = r.getValue(ADDRESS.ZIPCODE);
        this.country = r.getValue(ADDRESS.COUNTRY);
    }
    
    public Address(String street, String number, String city, String zip, String country) {
        this.street = street;
        this.number = number;
        this.city = city;
        this.zip = zip;
        this.country = country;
    }

    public String getStreet() {
        return street;
    }
    public String getNumber() {
        return number;
    }
    public String getCity() {
        return city;
    }
    public String getZip() {
        return zip;
    }
    public String getCountry() {
        return country;
    }
}
