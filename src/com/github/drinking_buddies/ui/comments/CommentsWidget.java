package com.github.drinking_buddies.ui.comments;

import java.util.List;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.entities.Comment;
import com.github.drinking_buddies.entities.User;
import com.github.drinking_buddies.ui.utils.TemplateUtils;

import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WTemplate;

public abstract class CommentsWidget extends WContainerWidget {
    private List<Comment> comments;
    
    private WContainerWidget commentsContainer = new WContainerWidget();
    
    private WTemplate main;
    
    public CommentsWidget(List<Comment> comments, User poster, WContainerWidget root) {
        super(root);
        
        this.comments = comments;
        
        this.main = new WTemplate(tr("comments-widget"), this);
        TemplateUtils.configureDefault(Application.getInstance(), main);
        
        AddCommentWidget acw = new AddCommentWidget(poster);
        this.main.bindWidget("add-comment", acw);
        acw.addedComment().addListener(this, new Signal1.Listener<Comment>() {
            public void trigger(Comment c) {
                saveComment(c);
                CommentsWidget.this.comments.add(c);
                commentsContainer.insertWidget(0, new CommentWidget(c));
                updateAllComments();
            }
        });
        
        for (Comment c : comments) {
            commentsContainer.addWidget(new CommentWidget(c));
        }
        main.bindWidget("comments", commentsContainer);
        
        updateAllComments();
    }
    
    private void updateAllComments() {
        main.bindInt("number", comments.size());
    }
    
    protected abstract void saveComment(Comment comment);
}
