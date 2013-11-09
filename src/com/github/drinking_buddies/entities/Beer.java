package com.github.drinking_buddies.entities;

/**
 * A beer data entity
 * 
 * @author plibin0
 */
public class Beer {
    private String name;
    private String brewery;
    private int favoredBy;
    private double score;
    private Image picture;
    
    public Beer(String name, String brewery, int favoredBy, double score, Image picture) {
        this.name = name;
        this.brewery = brewery;
        this.favoredBy = favoredBy;
        this.score = score;
        this.picture = picture;
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
    
    public double getScore() {
        return score;
    }
    
    public Image getPicture() {
        return picture;
    }
}
