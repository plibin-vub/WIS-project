package com.github.drinking_buddies.entities;

import java.util.Date;

import org.jooq.Record;

import com.github.drinking_buddies.ui.utils.DateUtils;

import static com.github.drinking_buddies.jooq.Tables.REVIEW;

public class Review {
    public static final int highestScore = 5;
    
    //TODO map all id's to Longs!
    private int id;
    private float colorScore;
    private float smellScore;
    private float tasteScore;
    private float feelScore;
    private String text;
    private User poster;
    private Date postDate;
    
    public Review(Record rr, Record ur) {
        id = rr.getValue(REVIEW.ID);
        colorScore = rr.getValue(REVIEW.VISUAL_SCORE);
        smellScore = rr.getValue(REVIEW.SMELL_SCORE);
        tasteScore = rr.getValue(REVIEW.TASTE_SCORE);
        feelScore = rr.getValue(REVIEW.FEEL_SCORE);
        text = rr.getValue(REVIEW.TEXT);
        poster = new User(ur);
        postDate = DateUtils.sqliteDateToJavaDate(rr.getValue(REVIEW.POST_TIME));
    }
    
    public Review(int id, float colorScore, float smellScore, float tasteScore,
            float feelScore, String text, User poster, Date postDate) {
        this.id = id;
        this.colorScore = colorScore;
        this.smellScore = smellScore;
        this.tasteScore = tasteScore;
        this.feelScore = feelScore;
        this.text = text;
        this.poster = poster;
        this.postDate = postDate;
    }
    
    public int getId() {
        return id;
    }
    
    public float getColorScore() {
        return colorScore;
    }
    
    public float getSmellScore() {
        return smellScore;
    }
    
    public float getTasteScore() {
        return tasteScore;
    }
    
    public float getFeelScore() {
        return feelScore;
    }
    
    public float getAverageScore() {
        return (colorScore + smellScore + tasteScore + feelScore) / 4;
    }
    
    public String getText() {
        return text;
    }
    
    public User getPoster() {
        return poster;
    }
    
    public Date getPostDate() {
        return postDate;
    }
}
