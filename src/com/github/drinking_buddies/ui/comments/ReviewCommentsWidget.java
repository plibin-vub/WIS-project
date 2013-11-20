package com.github.drinking_buddies.ui.comments;

import java.util.List;

import com.github.drinking_buddies.entities.Comment;
import com.github.drinking_buddies.entities.User;

import eu.webtoolkit.jwt.WContainerWidget;

public class ReviewCommentsWidget extends CommentsWidget {
    public ReviewCommentsWidget(List<Comment> comments, User poster, WContainerWidget root) {
        super(comments, poster, root);
    }
    
    public ReviewCommentsWidget(List<Comment> comments, User poster) {
        this(comments, poster, null);
    }

    @Override
    protected void saveComment(Comment comment) {
        //TODO
    }
}
