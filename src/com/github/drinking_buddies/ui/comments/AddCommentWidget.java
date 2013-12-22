package com.github.drinking_buddies.ui.comments;

import java.util.Date;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.entities.Comment;
import com.github.drinking_buddies.entities.User;
import com.github.drinking_buddies.ui.utils.TemplateUtils;

import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WMouseEvent;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WTemplate;
import eu.webtoolkit.jwt.WTextArea;

//A widget to add comments.
//We use this widgets to add comments to:
//-beer reviews
//-bars
public class AddCommentWidget extends WTemplate {
    private Signal1<Comment> addedComment = new Signal1<Comment>();
    
    private WTextArea text;
    private WPushButton post;
    private WPushButton clear;
    
    public AddCommentWidget(User poster) {
        this(poster, null);
    }
    
    public AddCommentWidget(User poster, WContainerWidget parent) {
        super(tr("add-comment-widget"), parent);
        TemplateUtils.configureDefault(Application.getInstance(), this);
        
        this.bindString("poster-img", poster.getSmallImageUrl());
        text = new WTextArea();
        text.setPlaceholderText(tr("add-comment.leave-a-new-comment"));
        text.addStyleClass("span11 comment-input");
        this.bindWidget("text", text);
        post = new WPushButton(tr("add-comment.post"));
        this.bindWidget("post", post);
        post.clicked().addListener(this, new Signal1.Listener<WMouseEvent>() {
            public void trigger(WMouseEvent arg) {
                if (!text.getText().equals("")) {
                    addedComment.trigger(new Comment(text.getText(), Application.getInstance().getLoggedInUser(), new Date()));
                    text.setText("");
                }
            }
        });
        clear = new WPushButton(tr("add-comment.clear"));
        this.bindWidget("clear", clear);
        clear.clicked().addListener(this, new Signal1.Listener<WMouseEvent>(){
            public void trigger(WMouseEvent arg0) {
                text.setText("");
            }            
        });
    }
    
    //Signal that is triggered when a comment is added.
    //This allows for decoupling this widget and the 
    //code that shows this widget.
    //This allows us to use the widget both for bars and beers.
    Signal1<Comment> addedComment() {
        return addedComment;
    }
}
