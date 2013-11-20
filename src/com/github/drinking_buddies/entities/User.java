package com.github.drinking_buddies.entities;

import org.jooq.Record;

import static com.github.drinking_buddies.jooq.Tables.USER;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String oAuthName;
    private String oAuthProvider;
    
    public User(Record r) {
        id = r.getValue(USER.ID);
        firstName = r.getValue(USER.FIRST_NAME);
        lastName = r.getValue(USER.LAST_NAME);
        oAuthName = r.getValue(USER.OAUTH_NAME);
        oAuthProvider = r.getValue(USER.OAUTH_PROVIDER);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
    
    public int getId() {
        return id;
    }
    
    public String getSmallImageUrl() {
        if ("facebook".equals(oAuthProvider))
            return "http://graph.facebook.com/" + firstName + "." + lastName + "/picture";
        return null;
    }
    
    public String getLargeImageUrl() {
        if ("facebook".equals(oAuthProvider))
            return "http://graph.facebook.com/" + firstName + "." + lastName + "/picture?type=large";
        return null;
    }
}
