package com.github.drinking_buddies.ui.comments;

import java.util.Locale;

import org.ocpsoft.prettytime.PrettyTime;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.entities.Comment;
import com.github.drinking_buddies.ui.utils.TemplateUtils;

import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WTemplate;

//Widget to render an existing comment.
//We use this widget to render bar and beer review comments.
//Note: we render the post time here using the PrettyTime library.
public class CommentWidget extends WTemplate {
    public CommentWidget(Comment comment) {
        this(comment, null);
    }
    
    public CommentWidget(Comment comment, WContainerWidget parent) {
        super(tr("comment-widget"), parent);
        
        TemplateUtils.configureDefault(Application.getInstance(), this);
        
        this.bindString("poster-name", comment.getPoster().getFirstName() + " " + comment.getPoster().getLastName());
        this.bindString("poster-img", comment.getPoster().getSmallImageUrl());
        this.bindString("text", comment.getText());
        
        //we render the post time here using the PrettyTime library
        Locale l = Application.getInstance().getEnvironment().getLocale();
        this.bindString("posted-x-time-ago", new PrettyTime(l).format(comment.getPostDate()));
    }
}
