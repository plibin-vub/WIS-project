package com.github.drinking_buddies.ui.comments;

import java.util.List;

import com.github.drinking_buddies.entities.Comment;
import com.github.drinking_buddies.entities.User;

import eu.webtoolkit.jwt.WContainerWidget;

public class BarCommentsWidget extends CommentsWidget {

    public BarCommentsWidget(List<Comment> comments, User poster,
            WContainerWidget root) {
        super(comments, poster, root);
    }

    @Override
    protected void saveComment(Comment comment) {
        // TODO Auto-generated method stub

    }

}
