package com.github.drinking_buddies.entities;

/**
 * A beer data entity
 * 
 * @author plibin0
 */
public class Beer {
    private String name;
    private String brewery;
    private int likes;
    private double score;
    private Image picture;
    
    public Beer(String name, String brewery, int likes, double score, Image picture) {
        this.name = name;
        this.brewery = brewery;
        this.likes = likes;
        this.score = score;
        this.picture = picture;
    }

    public String getName() {
        return name;
    }
    
    public String getBrewery() {
        return brewery;
    }
    
    public int getLikes() {
        return likes;
    }
    
    public double getScore() {
        return score;
    }
    
    public Image getPicture() {
        return picture;
    }
}
