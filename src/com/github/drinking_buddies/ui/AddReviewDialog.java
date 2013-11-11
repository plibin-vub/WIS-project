package com.github.drinking_buddies.ui;

import static com.github.drinking_buddies.jooq.Tables.*;

import java.sql.Connection;

import org.jooq.DSLContext;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.entities.Beer;
import com.github.drinking_buddies.entities.Review;
import com.github.drinking_buddies.jooq.tables.records.ReviewRecord;
import com.github.drinking_buddies.ui.utils.TemplateUtils;

import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WDialog;
import eu.webtoolkit.jwt.WDoubleValidator;
import eu.webtoolkit.jwt.WLineEdit;
import eu.webtoolkit.jwt.WMouseEvent;
import eu.webtoolkit.jwt.WObject;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WTemplate;
import eu.webtoolkit.jwt.WTextArea;
import eu.webtoolkit.jwt.WValidator;

public class AddReviewDialog extends WDialog {
    private Signal1<Review> reviewAdded = new Signal1<Review>();
    
    public AddReviewDialog(final Beer beer, WObject parent) {
        super(tr("add-review-dialog.title").arg(beer.getName()), parent);
        
        final WTemplate main = new WTemplate(tr("add-review-dialog"), this.getContents());
        TemplateUtils.configureDefault(Application.getInstance(), main);
        
        final WLineEdit color = new WLineEdit();
        color.setValidator(createScoreValidator());
        main.bindWidget("color", color);
        final WLineEdit smell = new WLineEdit();
        smell.setValidator(createScoreValidator());
        main.bindWidget("smell", smell);
        final WLineEdit taste = new WLineEdit();
        taste.setValidator(createScoreValidator());
        main.bindWidget("taste", taste);
        final WLineEdit feel = new WLineEdit();
        feel.setValidator(createScoreValidator());
        main.bindWidget("feel", feel);
        
        final WTextArea text = new WTextArea();
        main.bindWidget("text", text);
        
        WPushButton ok = new WPushButton(tr("add-review-dialog.ok"));
        main.bindWidget("ok", ok);
        ok.clicked().addListener(this, new Signal1.Listener<WMouseEvent>() {
            public void trigger(WMouseEvent arg) {
                AddReviewDialog.this.accept();
                Review review = save(beer, 
                                    Float.parseFloat(color.getText()),
                                    Float.parseFloat(smell.getText()),
                                    Float.parseFloat(taste.getText()),
                                    Float.parseFloat(feel.getText()),
                                    text.getText());
                reviewAdded.trigger(review);
            }
        });
        
        WPushButton cancel = new WPushButton(tr("add-tag-dialog.cancel"));
        main.bindWidget("cancel", cancel);
        cancel.clicked().addListener(this, new Signal1.Listener<WMouseEvent>() {
            public void trigger(WMouseEvent arg) {
                AddReviewDialog.this.reject();
            }
        });
    }
    
    private WValidator createScoreValidator() {
        WDoubleValidator v = new WDoubleValidator();
        v.setBottom(0);
        v.setTop(Review.highestScore);
        return v;
    }
    
    private Review save(Beer beer, 
                        float color,
                        float smell, 
                        float taste,
                        float feel, 
                        String text) {
        Application app = Application.getInstance();
        Connection conn = null;
        try {
            conn = app.getConnection();
            
            //TODO save poster and posttime
            DSLContext dsl = app.createDSLContext(conn);
            ReviewRecord r 
                    = dsl
                        .insertInto(REVIEW, 
                                    REVIEW.VISUAL_SCORE, 
                                    REVIEW.SMELL_SCORE,
                                    REVIEW.TASTE_SCORE,
                                    REVIEW.FEEL_SCORE,
                                    REVIEW.TEXT,
                                    REVIEW.POST_TIME,
                                    REVIEW.USER_ID)
                        .values(color, smell, taste, feel, text, null, null)
                        .returning().fetchOne();
            
            conn.commit();
                
            return new Review(r.getId(), 
                                    r.getVisualScore(),
                                    r.getSmellScore(),
                                    r.getTasteScore(),
                                    r.getFeelScore(),
                                    r.getText(),
                                    null,
                                    null);
        } catch (Exception e) {
            app.rollback(conn);
            throw new RuntimeException(e);
        } finally {
            app.closeConnection(conn);
        }
    }
    
    public Signal1<Review> reviewAdded() {
        return reviewAdded;
    }
}
