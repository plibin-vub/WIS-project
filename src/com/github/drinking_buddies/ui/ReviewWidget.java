package com.github.drinking_buddies.ui;

import static com.github.drinking_buddies.jooq.Tables.REVIEW_COMMENT;
import static com.github.drinking_buddies.jooq.Tables.USER;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.db.DBUtils;
import com.github.drinking_buddies.entities.Comment;
import com.github.drinking_buddies.entities.Review;
import com.github.drinking_buddies.entities.User;
import com.github.drinking_buddies.ui.comments.ReviewCommentsWidget;
import com.github.drinking_buddies.ui.utils.DateUtils;
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
        this.bindString("poster", review.getPoster().getFirstName() + " " + review.getPoster().getLastName());
        
        this.bindString("color-score", formatScore(review.getColorScore()));
        this.bindString("smell-score", formatScore(review.getSmellScore()));
        this.bindString("taste-score", formatScore(review.getTasteScore()));
        this.bindString("feel-score", formatScore(review.getFeelScore()));
        
        collapse();
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
        this.bindWidget("control", control);
        
        User user = Application.getInstance().getLoggedInUser();
        List<Comment> comments = new ArrayList<Comment>();
        fetchComments(this.review, comments);        
        this.bindWidget("comments", new ReviewCommentsWidget(review, comments, user));
    }
    
    private void fetchComments(Review review, List<Comment> comments) {
        Application app = Application.getInstance();
        Connection conn = null;
        try {
            conn = app.getConnection();
            DSLContext dsl = DBUtils.createDSLContext(conn);
            
            Result<Record> results 
                = dsl
                    .select()
                    .from(REVIEW_COMMENT)
                    .where(REVIEW_COMMENT.REVIEW_ID.equal(review.getId()))
                    .orderBy(REVIEW_COMMENT.POST_TIME, REVIEW_COMMENT.ID)
                    .fetch();
            
            for (Record r : results) {
                String text = r.getValue(REVIEW_COMMENT.TEXT);
                Date postDate = DateUtils.sqliteDateToJavaDate(r.getValue(REVIEW_COMMENT.POST_TIME));
                
                Record userRecord
                = dsl
                    .select()
                    .from(USER)
                    .where(USER.ID.eq(r.getValue(REVIEW_COMMENT.USER_ID)))
                    .fetchOne();   
                User poster = new User(userRecord);
                
                comments.add(new Comment(text, poster, postDate));
            }
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            app.closeConnection(conn);
        }
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
        this.bindWidget("control", control);
        this.bindWidget("comments", null);
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
