package com.github.drinking_buddies.ui.comments;

import java.util.List;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.entities.Comment;
import com.github.drinking_buddies.entities.User;
import com.github.drinking_buddies.ui.utils.TemplateUtils;

import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WTemplate;

//Abstract widget that represents a list of comments.
//To use this widget, the user needs to implement the abstract method:
//saveComment
//This allows us to reuse this widget in different parts of our application;
//namely for bars and beer reviews.
public abstract class CommentsWidget extends WContainerWidget {
    private List<Comment> comments;
    
    private WContainerWidget commentsContainer = new WContainerWidget();
    
    private WTemplate main;
    
    public CommentsWidget(List<Comment> comments, User poster, WContainerWidget root) {
        super(root);
        
        this.comments = comments;
        
        this.main = new WTemplate(tr("comments-widget"), this);
        TemplateUtils.configureDefault(Application.getInstance(), main);
        
       
        if(poster!=null){
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
        }else{
            this.main.bindString("add-comment", tr("comments.please-log-in"));
        }
        for (Comment c : comments) {
            commentsContainer.addWidget(new CommentWidget(c));
        }
        main.bindWidget("comments", commentsContainer);
        
        updateAllComments();
    }
    
    //Update the comment counter.
    private void updateAllComments() {
        main.bindInt("number", comments.size());
    }
    
    //This method should save the Comment to the database,
    //and connect it to the appropriate database entity 
    //(e.g.: bar, beer).
    protected abstract void saveComment(Comment comment);
}
