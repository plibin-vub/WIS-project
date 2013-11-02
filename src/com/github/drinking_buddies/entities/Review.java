package com.github.drinking_buddies.entities;

import java.util.Date;

public class Review {
    private double colorScore;
    private double smellScore;
    private double tasteScore;
    private double feelScore;
    private String text;
    private Poster poster;
    private Date postDate;
    
    public Review(double colorScore, double smellScore, double tasteScore,
            double feelScore, String text, Poster poster, Date postDate) {
        this.colorScore = colorScore;
        this.smellScore = smellScore;
        this.tasteScore = tasteScore;
        this.feelScore = feelScore;
        this.text = text;
        this.poster = poster;
        this.postDate = postDate;
    }
    
    public double getColorScore() {
        return colorScore;
    }
    
    public double getSmellScore() {
        return smellScore;
    }
    
    public double getTasteScore() {
        return tasteScore;
    }
    
    public double getFeelScore() {
        return feelScore;
    }
    
    public double getAverageScore() {
        return (colorScore + smellScore + tasteScore + feelScore) / 4;
    }
    
    public String getText() {
        return text;
    }
    
    public Poster getPoster() {
        return poster;
    }
    
    public Date getPostDate() {
        return postDate;
    }
}
