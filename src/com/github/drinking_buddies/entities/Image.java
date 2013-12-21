package com.github.drinking_buddies.entities;

//Image POJO
public class Image {
    private byte[] data;
    private String mimetype;
    
    public Image(byte[] data, String mimetype) {
        this.data = data;
        this.mimetype = mimetype;
    }

    public byte[] getData() {
        return data;
    }
    
    public String getMimetype() {
        return mimetype;
    }
}
