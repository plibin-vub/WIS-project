package com.github.drinking_buddies.entities;


public class User {
    private String name;
    private String birthdate;

    
    public User (String name, String birthdate) {
        this.name = name;
        this.birthdate = birthdate;
    }
    
    public String getName() {
        return name;
    }
    
    public String getBirthdate() {
        return birthdate;
    }
    
}
