package com.github.drinking_buddies.entities;

/**
 * A beer data entity
 * 
 * @author plibin0
 */
public class Beer {
    private int id;
    private String name;
    private String brewery;
    private int favoredBy;
    private Image picture;
    
    public Beer(int id, String name, String brewery, int favoredBy, Image picture) {
        this.id = id;
        this.name = name;
        this.brewery = brewery;
        this.favoredBy = favoredBy;
        this.picture = picture;
    }
    
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public String getBrewery() {
        return brewery;
    }
    
    public int getFavoredBy() {
        return favoredBy;
    }
    
    public Image getPicture() {
        return picture;
    }
}
