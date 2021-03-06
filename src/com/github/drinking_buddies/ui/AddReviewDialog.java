package com.github.drinking_buddies.ui;

import static com.github.drinking_buddies.jooq.Tables.REVIEW;
import static com.github.drinking_buddies.jooq.Tables.USER;

import java.sql.Connection;
import java.util.Date;

import org.jooq.DSLContext;
import org.jooq.Record;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.db.DBUtils;
import com.github.drinking_buddies.entities.Beer;
import com.github.drinking_buddies.entities.Review;
import com.github.drinking_buddies.jooq.tables.records.ReviewRecord;
import com.github.drinking_buddies.ui.utils.DateUtils;
import com.github.drinking_buddies.ui.utils.TemplateUtils;

import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WDialog;
import eu.webtoolkit.jwt.WDoubleValidator;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WLineEdit;
import eu.webtoolkit.jwt.WMouseEvent;
import eu.webtoolkit.jwt.WObject;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WTemplate;
import eu.webtoolkit.jwt.WTextArea;
import eu.webtoolkit.jwt.WValidator;
import eu.webtoolkit.jwt.WValidator.State;

//A dialog that allows the users to write a beer review.
//Users can give a score between 0-5 for these variables: 
//color, smell, taste, feel.
//They can also provide a text with a detailed description 
//of what they think about the beer.
public class AddReviewDialog extends WDialog {
    private WLineEdit color;
    private WLineEdit smell;
    private WLineEdit taste;
    private WLineEdit feel;
    
    private Signal1<Review> reviewAdded = new Signal1<Review>();
    
    public AddReviewDialog(final Beer beer, WObject parent) {
        super(tr("add-review-dialog.title").arg(beer.getName()), parent);
        
        this.resize(new WLength(600), WLength.Auto);
        
        final WTemplate main = new WTemplate(tr("add-review-dialog"), this.getContents());
        TemplateUtils.configureDefault(Application.getInstance(), main);
        
        color = new WLineEdit();
        //set the line-edit's validator:
        //the validator that is returned by createScoreValidator()
        //will do both client- and server-side validation.
        color.setValidator(createScoreValidator());
        main.bindWidget("color", color);
        smell = new WLineEdit();
        smell.setValidator(createScoreValidator());
        main.bindWidget("smell", smell);
        taste = new WLineEdit();
        taste.setValidator(createScoreValidator());
        main.bindWidget("taste", taste);
        feel = new WLineEdit();
        feel.setValidator(createScoreValidator());
        main.bindWidget("feel", feel);
        
        final WTextArea text = new WTextArea();
        main.bindWidget("text", text);
        text.setWidth(new WLength(550));
        
        WPushButton ok = new WPushButton(tr("add-review-dialog.ok"));
        main.bindWidget("ok", ok);
        ok.clicked().addListener(this, new Signal1.Listener<WMouseEvent>() {
            public void trigger(WMouseEvent arg) {
                //make sure the input is valid before saving it to the database!
                if (!validate())
                    return;
                
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
    
    //Validate the form server-side.
    private boolean validate() {
        return
            color.validate() == State.Valid &&
            smell.validate() == State.Valid &&
            taste.validate() == State.Valid &&
            feel.validate() == State.Valid; 
    }
    
    //Create a score validator that ensures that the score's value
    //lies between 0.0-5.0
    private WValidator createScoreValidator() {
        WDoubleValidator v = new WDoubleValidator();
        v.setBottom(0);
        v.setTop(Review.highestScore);
        v.setMandatory(true);
        return v;
    }
    
    //Save the Review to the database.
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
            
            DSLContext dsl = DBUtils.createDSLContext(conn);
            ReviewRecord rr 
                    = dsl
                        .insertInto(REVIEW, 
                                    REVIEW.BEER_ID,
                                    REVIEW.VISUAL_SCORE, 
                                    REVIEW.SMELL_SCORE,
                                    REVIEW.TASTE_SCORE,
                                    REVIEW.FEEL_SCORE,
                                    REVIEW.TEXT,
                                    REVIEW.POST_TIME,
                                    REVIEW.USER_ID)
                        .values(beer.getId(), color, smell, taste, feel, text, DateUtils.javaDateToSqliteFormat(new Date()), app.getLoggedInUser().getId())
                        .returning().fetchOne();
            
            Record ur 
                    = dsl
                        .select()
                        .from(USER)
                        .where(USER.ID.equal(rr.getUserId()))
                        .fetchOne();
                            
            conn.commit();
                
            return new Review(rr, ur);
        } catch (Exception e) {
            DBUtils.rollback(conn);
            throw new RuntimeException(e);
        } finally {
            app.closeConnection(conn);
        }
    }
    
    //Signal that is triggered when a review is added.
    //This allows for decoupling this dialog and the 
    //code that calls this dialog.
    public Signal1<Review> reviewAdded() {
        return reviewAdded;
    }
}
