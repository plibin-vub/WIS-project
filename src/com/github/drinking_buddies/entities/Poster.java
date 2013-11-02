package com.github.drinking_buddies.entities;

public class Poster {
    private String name;
    private byte[] picture;
    
    public Poster(String name, byte[] picture) {
        this.name = name;
        this.picture = picture;
    }

    public String getName() {
        return name;
    }
    
    public byte[] getPicture() {
        return picture;
    }
}
