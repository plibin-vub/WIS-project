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
    private double alcohol;
    private int favoredBy;
    private String pictureUrl;
    
    public Beer(int id, String name, String brewery, int favoredBy, double alcohol, String pictureUrl) {
        this.id = id;
        this.name = name;
        this.brewery = brewery;
        this.alcohol = alcohol;
        this.favoredBy = favoredBy;
        this.pictureUrl = pictureUrl;
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
    
    public double getAlcohol() {
        return alcohol;
    }
    
    public int getFavoredBy() {
        return favoredBy;
    }
    
    public String getPictureUrl() {
        return pictureUrl;
    }
}
