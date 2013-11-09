package com.github.drinking_buddies.ui;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.entities.Beer;
import com.github.drinking_buddies.entities.Review;
import com.github.drinking_buddies.entities.Tag;
import com.github.drinking_buddies.ui.utils.ImageUtils;
import com.github.drinking_buddies.ui.utils.TemplateUtils;

import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WImage;
import eu.webtoolkit.jwt.WLink;
import eu.webtoolkit.jwt.WMouseEvent;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WResource;
import eu.webtoolkit.jwt.WTemplate;

/**
 * The beer form UI component.
 * This form shows beer information and allows users to look at and provide reviews,
 * these reviews can also be commented upon.
 */
public class BeerForm extends WContainerWidget {
    private WContainerWidget tagContainer = new WContainerWidget();
  
    public BeerForm(final Beer beer, Iterable<Tag> tags) {
        //the main template for the beer form 
        //(a WTemplate constructor accepts the template text and its parent)
        WTemplate main = new WTemplate(tr("beer-form"), this);
        TemplateUtils.configureDefault(Application.getInstance(), main);
        //we bind to some of the template's variables
        main.bindString("beer", beer.getName());
        main.bindString("brewery", beer.getBrewery());
        main.bindInt("favored-by", beer.getFavoredBy());
        main.bindString("score", String.valueOf(beer.getScore()));
        main.bindInt("highest-score", Review.highestScore);
        //we bind the beer's picture to the template
        WImage picture = new WImage();
        {
            WResource r = ImageUtils.createResource(beer.getPicture().getData(), beer.getPicture().getMimetype());
            picture.setImageLink(new WLink(r));
        }
        main.bindWidget("picture", picture);
        
        //add the "add to favorites" button to the template
        WPushButton addToFavorites = new WPushButton(tr("beer-form.add-to-favorites"));
        //connect a listener to the button
        addToFavorites.clicked().addListener(this, new Signal1.Listener<WMouseEvent>() {
            public void trigger(WMouseEvent arg) {
                //TODO
                //add to favorites + show a message that we did!
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
        
        //add the "add review" button to the main template
        WPushButton addReview = new WPushButton(tr("beer-form.add-review"));
        //connect a listener to the button
        addReview.clicked().addListener(this, new Signal1.Listener<WMouseEvent>() {
            public void trigger(WMouseEvent arg) {
                AddReviewDialog dialog = new AddReviewDialog(beer, BeerForm.this);
                dialog.reviewAdded().addListener(BeerForm.this, new Signal1.Listener<Review>() {
                    public void trigger(Review tag) {
                        
                    }
                });
                dialog.show();
            }
        });
        main.bindWidget("add-review", addReview);
    }
    
    private void addTagWidget(Tag tag) {
        new TagWidget(tag.getText(), tagContainer);
    }

    public void tagAdded(Tag tag) {
        addTagWidget(tag);
    }
}
