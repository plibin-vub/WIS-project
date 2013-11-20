package com.github.drinking_buddies.entities;

import java.util.Date;

public class Comment {
    private String text;
    private User poster;
    private Date postDate;
    
    public Comment(String text, User poster, Date postDate) {
        this.text = text;
        this.poster = poster;
        this.postDate = postDate;
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
