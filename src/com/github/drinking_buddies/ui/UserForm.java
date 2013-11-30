package com.github.drinking_buddies.ui;

import static com.github.drinking_buddies.jooq.Tables.BAR;
import static com.github.drinking_buddies.jooq.Tables.BEER;
import static com.github.drinking_buddies.jooq.Tables.FAVORITE_BAR;
import static com.github.drinking_buddies.jooq.Tables.FAVORITE_BEER;

import java.sql.Connection;

import org.jooq.DSLContext;
import org.jooq.Record3;
import org.jooq.Result;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.db.DBUtils;
import com.github.drinking_buddies.entities.Bar;
import com.github.drinking_buddies.entities.User;
import com.github.drinking_buddies.ui.geolocation.GeolocationWidget;
import com.github.drinking_buddies.ui.utils.TemplateUtils;

import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WImage;
import eu.webtoolkit.jwt.WString;
import eu.webtoolkit.jwt.WTemplate;

/**
 * The user form UI component.
 * This form shows user information and location, favorite beers and favorite bars.
 */
public class UserForm extends WContainerWidget {
    private static int maxFavorites = 5;
    
    private User user;
    
    private WTemplate main;
    
    public UserForm(User user, Bar currentBar) {
        this.user = user;
        
        main = new WTemplate(tr("user-form"), this);
        TemplateUtils.configureDefault(Application.getInstance(), main);
        main.bindString("user-pic-url", user.getLargeImageUrl());
        main.bindString("first-name", user.getFirstName());
        main.bindString("last-name", user.getLastName());
        if (currentBar == null) {
            main.bindWidget("is-drinking-in", null);
        } else {
            WString drinking = tr("user-form.is-drinking-in");
            drinking
                .arg(currentBar.getName())
                .arg(currentBar.getAddress().getCity())
                .arg(currentBar.getAddress().getCountry());
            WTemplate t = new WTemplate(tr("is-drinking-in"));
            t.bindString("text", drinking);
            main.bindWidget("is-drinking-in", t);
        }
        
        if (isLoggedInUser(user)) {
            GeolocationWidget glw = new GeolocationWidget();
            main.bindWidget("share-location", glw);
        }
        
        updateFavorites(main);
        
        main.bindString("find-nearby-friends-url", Application.FIND_NEARBY_FRIENDS_URL);
        main.bindString("find-nearby-bars-url", Application.FIND_NEARBY_BARS_URL);
    }
    
    private void updateFavorites(WTemplate main) {
        Application app = Application.getInstance();
        
        WTemplate favorites = new WTemplate(tr("user-favorites"));
        TemplateUtils.configureDefault(app, favorites);
        
        int favoriteBeers = 0;
        int favoriteBars = 0;
        
        Connection conn = null;
        try {
            conn = app.getConnection();
            DSLContext dsl = DBUtils.createDSLContext(conn);
            
            Result<Record3<String, String, Integer>> results = 
                    dsl
                        .select(BEER.NAME, BEER.URL, BEER.ID)
                        .from(FAVORITE_BEER)
                        .join(BEER)
                        .on(BEER.ID.equal(FAVORITE_BEER.BEER_ID))
                        .where(FAVORITE_BEER.USER_ID.equal(this.user.getId()))
                        .fetch();
            
            final String favBeerPrefix = "fav-beer-";
            final String favBeerDeletePrefix = "fav-beer-delete-";
            
            for (int i = 0; i < maxFavorites; ++i) {
                favorites.bindWidget(favBeerPrefix + (i + 1), null);
                favorites.bindWidget(favBeerDeletePrefix + (i + 1), null);
            }
            
            for (final Record3<String, String, Integer> r : results) {
                WTemplate l = new WTemplate(tr("user-favorite-link"));
                TemplateUtils.configureDefault(Application.getInstance(), l);
                l.bindString("url", r.getValue(BEER.URL));
                l.bindString("name", r.getValue(BEER.NAME));
                favorites.bindWidget(favBeerPrefix + (favoriteBeers + 1), l);
                
                if (isLoggedInUser(user)) {
                    WImage remove = new WImage("/pics/remove-action.png");
                    favorites.bindWidget(favBeerDeletePrefix + (favoriteBeers + 1), remove);
                    remove.clicked().addListener(this, new Signal.Listener() {
                        @Override
                        public void trigger() {
                            deleteFavoriteBeer(r.getValue(BEER.ID));
                        }
                    });
                }
                
                ++favoriteBeers;
            }    
            
            results = 
                    dsl
                        .select(BAR.NAME, BAR.URL, BAR.ID)
                        .from(FAVORITE_BAR)
                        .join(BAR)
                        .on(BAR.ID.equal(FAVORITE_BAR.BAR_ID))
                        .where(FAVORITE_BAR.USER_ID.equal(this.user.getId()))
                        .fetch();
            
            final String favBarPrefix = "fav-bar-";
            final String favBarDeletePrefix = "fav-bar-delete-";
            
            for (int i = 0; i < maxFavorites; ++i) {
                favorites.bindWidget(favBarPrefix + (i + 1), null);
                favorites.bindWidget(favBarDeletePrefix + (i + 1), null);
            }
            
            for (final Record3<String, String, Integer> r : results) {
                WTemplate l = new WTemplate(tr("user-favorite-link"));
                TemplateUtils.configureDefault(Application.getInstance(), l);
                l.bindString("url", r.getValue(BAR.URL));
                l.bindString("name", r.getValue(BAR.NAME));
                favorites.bindWidget(favBarPrefix + (favoriteBars + 1), l);
                
                if (isLoggedInUser(user)) {
                    WImage remove = new WImage("/pics/remove-action.png");
                    favorites.bindWidget(favBarDeletePrefix + (favoriteBars + 1), remove);
                    remove.clicked().addListener(this, new Signal.Listener() {
                        @Override
                        public void trigger() {
                            deleteFavoriteBar(r.getValue(BAR.ID));
                        }
                    });
                }
                
                ++favoriteBars;
            }         
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            app.closeConnection(conn);
        }
        
        if (favoriteBeers == 0 && favoriteBars == 0)
            main.bindWidget("favorites", null);
        else
            main.bindWidget("favorites", favorites);
    }
    
    private void deleteFavoriteBeer(int beerId) {
        Application app = Application.getInstance();
        Connection conn = null;
        try {
            conn = app.getConnection();
            DSLContext dsl = DBUtils.createDSLContext(conn);
            dsl
                .delete(FAVORITE_BEER)
                .where(FAVORITE_BEER.USER_ID.equal(user.getId()))
                .and(FAVORITE_BEER.BEER_ID.equal(beerId))
                .execute();
            conn.commit();
            
            updateFavorites(this.main);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            app.closeConnection(conn);
        }
    }
    
    private void deleteFavoriteBar(int barId) {
        Application app = Application.getInstance();
        Connection conn = null;
        try {
            conn = app.getConnection();
            DSLContext dsl = DBUtils.createDSLContext(conn);
            dsl
                .delete(FAVORITE_BAR)
                .where(FAVORITE_BAR.USER_ID.equal(user.getId()))
                .and(FAVORITE_BAR.BAR_ID.equal(barId))
                .execute();
            conn.commit();
            
            updateFavorites(this.main);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            app.closeConnection(conn);
        }
    }
    
    private boolean isLoggedInUser(User u) {
        Application app = Application.getInstance();
        if (app.getLoggedInUser() != null && app.getLoggedInUser().getId() == u.getId())
            return true;
        return false;       
    }
}


