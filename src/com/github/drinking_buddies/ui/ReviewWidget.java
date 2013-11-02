package com.github.drinking_buddies.ui;

import java.text.DecimalFormat;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.entities.Review;
import com.github.drinking_buddies.ui.utils.TemplateUtils;

import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WMouseEvent;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WTemplate;

/**
 * A widget to visualize reviews.
 */
public class ReviewWidget extends WTemplate {
    private Review review;
    public ReviewWidget(Review review, WContainerWidget root) {
        super(tr("review"), root);
        TemplateUtils.configureDefault(Application.getInstance(), this);
        
        this.review = review;
        
        this.bindString("average-score", formatScore(review.getAverageScore()));
        this.bindString("poster", review.getPoster().getName());
        
        this.bindString("color-score", formatScore(review.getColorScore()));
        this.bindString("smell-score", formatScore(review.getSmellScore()));
        this.bindString("taste-score", formatScore(review.getTasteScore()));
        this.bindString("feel-score", formatScore(review.getFeelScore()));
    }

    public ReviewWidget(Review review) {
        this(review, null);
    }
    
    private void expand() {
        this.bindString("text", review.getText());
        showEllipsis(false);
        WPushButton control = new WPushButton(tr("review.collapse"));
        control.clicked().addListener(this, new Signal1.Listener<WMouseEvent>() {
            public void trigger(WMouseEvent arg) {
                collapse();
            }
        });
        //TODO show comments here
    }
    
    private void collapse() {
        //TODO this 100 is chosen quite arbitrarily
        final int charLimit = 100;
        final String shortText = shortText(review.getText(), charLimit);
        this.bindString("text", shortText);
        showEllipsis(!review.getText().equals(shortText));
        WPushButton control = new WPushButton(tr("review.expand/comment"));
        control.clicked().addListener(this, new Signal1.Listener<WMouseEvent>() {
            public void trigger(WMouseEvent arg) {
                expand();
            }
        });
    }
    
    private void showEllipsis(boolean show) {
        if (show)
            this.bindWidget("ellipsis", new WTemplate(tr("ellipsis")));
        else 
            this.bindWidget("ellipsis", null);
    }
    
    private static final DecimalFormat formatter = new DecimalFormat("#.0"); 
    private String formatScore(double score) {
        return formatter.format(score);
    }
    
    private String shortText(String text, int charLimit) {
        if (text.length() <= charLimit) {
            return text;
        } else {
            //TODO
            //this code does not handle ellipses properly
            //prolly, if we run into an ellipse we should cut right after the ellipse?
            for (int i = charLimit; i < text.length(); ++i) {
                if (text.charAt(i) == '.')
                    return text.substring(0, i + 1);
            }
            return text;
        }
    }
}
