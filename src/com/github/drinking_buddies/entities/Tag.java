package com.github.drinking_buddies.entities;

//Tag POJO
public class Tag {
    private int id;
    private String text;

    public Tag(int id, String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }
    
    public String getText() {
        return text;
    }
}
