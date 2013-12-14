package com.github.drinking_buddies.entities;

public class Bar {
    private int id;
    private String name;
    private int favoredBy;
    private double score;
    private Image picture;
    private Address address;
    private String url;
    
    
    public Bar(int id, String name, int favoredBy, double score,String website, Image picture,
            Address address,String url) {
        this.id = id;
        this.name = name;
        this.favoredBy = favoredBy;
        this.score = score;
        this.picture = picture;
        this.address = address;
        this.url=url;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getFavoredBy() {
        return favoredBy;
    }
    public double getScore() {
        return score;
    }
    public Image getPicture() {
        return picture;
    }
    public Address getAddress() {
        return address;
    }
    public String getUrl() {
       
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public void setPicture(Image picture) {
        this.picture = picture;
    }
}
