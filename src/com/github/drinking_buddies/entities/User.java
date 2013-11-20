package com.github.drinking_buddies.entities;

import org.jooq.Record;

import static com.github.drinking_buddies.jooq.Tables.USER;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    
    public User(Record r) {
        id = r.getValue(USER.ID);
        firstName = r.getValue(USER.FIRST_NAME);
        lastName = r.getValue(USER.LAST_NAME);
    }
    
    public User (int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
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
}
