package com.github.drinking_buddies.entities;

import org.jooq.Record;

import static com.github.drinking_buddies.jooq.Tables.USER;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String oAuthName;
    private String oAuthProvider;
    private String url;
    private String token;
    
    public User(Record r) {
        id = r.getValue(USER.ID);
        firstName = r.getValue(USER.FIRST_NAME);
        lastName = r.getValue(USER.LAST_NAME);
        oAuthName = r.getValue(USER.OAUTH_NAME);
        oAuthProvider = r.getValue(USER.OAUTH_PROVIDER);
        url=r.getValue(USER.URL);          
    }

    public User(String firstName, String lastName, String url) {
        this.firstName=firstName;
        this.lastName=lastName;
        this.url=url;
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
            return "http://graph.facebook.com/" + oAuthName + "/picture";
        return null;
    }
    
    public String getLargeImageUrl() {
        if ("facebook".equals(oAuthProvider))
            return "http://graph.facebook.com/" + oAuthName + "/picture?type=large";
        return null;
    }

    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
}
