package com.github.drinking_buddies.ui;

import static com.github.drinking_buddies.jooq.Tables.FAVORITE_BEER;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.EnumSet;
import java.util.List;

import org.jooq.DSLContext;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.entities.Beer;
import com.github.drinking_buddies.entities.Review;
import com.github.drinking_buddies.entities.Tag;
import com.github.drinking_buddies.entities.User;
import com.github.drinking_buddies.ui.utils.TemplateUtils;

import eu.webtoolkit.jwt.Icon;
import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.StandardButton;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WMessageBox;
import eu.webtoolkit.jwt.WMouseEvent;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WString;
import eu.webtoolkit.jwt.WTemplate;
import eu.webtoolkit.jwt.WDialog.DialogCode;

/**
 * The beer form UI component.
 * This form shows beer information and allows users to look at and provide reviews,
 * these reviews can also be commented upon.
 */
public class BeerForm extends WContainerWidget {
    private WTemplate main;
    
    private WContainerWidget tagContainer = new WContainerWidget();
    private WContainerWidget reviewContainer = new WContainerWidget();
    
    private List<Review> reviews;
  
    public BeerForm(final Beer beer, List<Tag> tags, List<Review> reviews) {
        this.reviews = reviews;
        
        //the main template for the beer form 
        //(a WTemplate constructor accepts the template text and its parent)
        main = new WTemplate(tr("beer-form"), this);
        TemplateUtils.configureDefault(Application.getInstance(), main);
        //we bind to some of the template's variables
        main.bindString("beer", beer.getName());
        main.bindString("brewery", beer.getBrewery());
        main.bindString("alcohol", beer.getAlcohol()+"");
        main.bindInt("favored-by", beer.getFavoredBy());
        main.bindInt("highest-score", Review.highestScore);
        //we bind the beer's picture url to the template
        main.bindString("picture-url", beer.getPictureUrl());
        
        //add the "add to favorites" button to the template
        WPushButton addToFavorites = new WPushButton(tr("beer-form.add-to-favorites"));
        //connect a listener to the button
        addToFavorites.clicked().addListener(this, new Signal1.Listener<WMouseEvent>() {
            public void trigger(WMouseEvent arg) {
                boolean added = addToFavorites(beer);
                
                String prefix = "beer-form.add-to-favorites.";
                WString status = tr(prefix + "status");
                
                final WMessageBox mb;
                if (added) {
                    mb = new WMessageBox(status, 
                            tr(prefix + "added").arg(beer.getName()), 
                            Icon.Information,
                            EnumSet.of(StandardButton.Ok));
                } else {
                   mb = new WMessageBox(status, 
                            tr(prefix+"not-added").arg(beer.getName()),
                            Icon.Warning,
                            EnumSet.of(StandardButton.Ok));
                }
                mb.setModal(true);
                mb.buttonClicked().addListener(BeerForm.this, new Signal1.Listener<StandardButton>() {
                    public void trigger(StandardButton sb) {
                        mb.done(DialogCode.Accepted);
                    }
                });
                mb.show();
            }
        });
        main.bindWidget("add-to-favorites", addToFavorites);
        
        //set the url and text, used in the "in which bars can I find beer-xxx" anchor
        main.bindString("find-bars-url", Application.getInstance().resolveRelativeUrl("/find-bars-with-beer/" + beer.getName()));
        main.bindString("find-bars-text", tr("beer-form.in-which-bar-can-i-find").arg(beer.getName()));
               
        //add tagwidgets to the main template
        for (Tag t : tags) {
            addTagWidget(t);
        }
        main.bindWidget("tags", tagContainer);
        
        //add the "add tag" button to the main template
        WPushButton addTag = new WPushButton(tr("beer-form.add-tag"));
        //connect a listener to the button
        addTag.clicked().addListener(this, new Signal1.Listener<WMouseEvent>() {
            public void trigger(WMouseEvent arg) {
                AddTagDialog dialog = new AddTagDialog(beer, BeerForm.this);
                dialog.tagAdded().addListener(BeerForm.this, new Signal1.Listener<Tag>() {
                    public void trigger(Tag tag) {
                        tagAdded(tag);
                    }
                });
                dialog.show();
            }
        });
        main.bindWidget("add-tag", addTag);
        
        for (Review r : reviews) {
            addReviewWidget(r);
        }
        main.bindWidget("reviews", reviewContainer);
        
        //add the "add review" button to the main template
        WPushButton addReview = new WPushButton(tr("beer-form.add-review"));
        //connect a listener to the button
        addReview.clicked().addListener(this, new Signal1.Listener<WMouseEvent>() {
            public void trigger(WMouseEvent arg) {
                AddReviewDialog dialog = new AddReviewDialog(beer, BeerForm.this);
                dialog.reviewAdded().addListener(BeerForm.this, new Signal1.Listener<Review>() {
                    public void trigger(Review review) {
                        reviewAdded(review);
                    }
                });
                dialog.show();
            }
        });
        main.bindWidget("add-review", addReview);
        
        updateReviewScore();
    }
    
    private void addTagWidget(Tag tag) {
        new TagWidget(tag.getText(), tagContainer);
    }
    
    private void addReviewWidget(Review review) {
        new ReviewWidget(review, reviewContainer);
    }

    public void tagAdded(Tag tag) {
        addTagWidget(tag);
    }
    
    public void reviewAdded(Review review) {
        addReviewWidget(review);
        reviews.add(review);
        updateReviewScore();
    }
    
    private double calculateScore() {
        double total = 0;
        for (Review r : reviews) {
            total += r.getAverageScore();
        }
        return total/reviews.size();
    }
    
    private static final DecimalFormat formatter = new DecimalFormat("#.0"); 
    private String formatScore(double score) {
        return formatter.format(score);
    }
    
    private void updateReviewScore() {
        main.bindString("score", formatScore(calculateScore()));
    }
    
    private boolean addToFavorites(Beer beer) {
        Application app = Application.getInstance();
        Connection conn = null;
        try {
            conn = app.getConnection();
            
            User user = app.getLoggedInUser();
            
            DSLContext dsl = app.createDSLContext(conn);
            long count =
                    dsl
                        .select()
                        .from(FAVORITE_BEER)
                        .where(FAVORITE_BEER.USER_ID.equal(user.getId()))
                        .and(FAVORITE_BEER.BEER_ID.equal(beer.getId()))
                        .fetchCount();
            
            if (count > 0) {
                //did not add the beer to the user's favorites,
                //since it already is a favorite
                return false;
            } else {
                dsl
                    .insertInto(FAVORITE_BEER,
                                FAVORITE_BEER.BEER_ID,
                                FAVORITE_BEER.USER_ID)
                    .values(beer.getId(),
                            user.getId())
                    .execute();
                conn.commit();
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
