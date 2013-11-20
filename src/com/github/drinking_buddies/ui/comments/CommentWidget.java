package com.github.drinking_buddies.ui.comments;

import java.util.Locale;

import org.ocpsoft.prettytime.PrettyTime;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.entities.Comment;
import com.github.drinking_buddies.ui.utils.TemplateUtils;

import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WTemplate;

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
        
        Locale l = new Locale(Application.getInstance().getConfiguration().getWt().getLocale());
        this.bindString("posted-x-time-ago", new PrettyTime(l).format(comment.getPostDate()));
    }
}
