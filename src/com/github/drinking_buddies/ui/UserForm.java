package com.github.drinking_buddies.ui;

import static com.github.drinking_buddies.jooq.Tables.USER;
import static com.github.drinking_buddies.jooq.Tables.BUDDY;
import static com.github.drinking_buddies.jooq.Tables.BAR;
import static com.github.drinking_buddies.jooq.Tables.BEER;
import static com.github.drinking_buddies.jooq.Tables.FAVORITE_BAR;
import static com.github.drinking_buddies.jooq.Tables.FAVORITE_BEER;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Result;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.db.DBUtils;
import com.github.drinking_buddies.entities.Bar;
import com.github.drinking_buddies.entities.Beer;
import com.github.drinking_buddies.entities.User;
import com.github.drinking_buddies.jwt.ShareLocationHandler;
import com.github.drinking_buddies.ui.utils.TemplateUtils;
import com.github.drinking_buddies.webservices.facebook.Facebook;
import com.github.drinking_buddies.webservices.facebook.Friend;
import com.github.drinking_buddies.webservices.facebook.Person;
import com.github.drinking_buddies.webservices.rest.exceptions.RestException;

import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.Signal2;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WImage;
import eu.webtoolkit.jwt.WPushButton;
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
            WPushButton b = new WPushButton(tr("user-form.share-location"));
            ShareLocationHandler slh = new ShareLocationHandler(main, b);
            main.bindWidget("share-location", b);
            
            slh.locationShared().addListener(this, new Signal2.Listener<String, String>() {
                public void trigger(String arg1, String arg2) {
                       System.err.println(arg1+"-"+arg2);
                }
            });
        } else {
            main.bindWidget("share-location", null);
        }
        
        updateFavorites(main);
        
        main.bindString("find-nearby-friends-url", Application.FIND_NEARBY_FRIENDS_URL);
        main.bindString("find-nearby-bars-url", Application.FIND_NEARBY_BARS_URL);
        WPushButton friendImport = new WPushButton(tr("user-form.friend-import"));
        main.bindWidget("friend-import", friendImport);
        friendImport.clicked().addListener(this, new Signal.Listener() {
            public void trigger() {
                            friendImport(); 
                    }
                
            
        });
    }
    
    protected void friendImport() {
        Application app = Application.getInstance();
        Connection conn = app.getConnection();
        DSLContext dsl = DBUtils.createDSLContext(conn);
        int userId = app.getLoggedInUser().getId();
        try{
            Facebook fb=new Facebook(app.getLoggedInUser().getToken());
            List<Friend> friends=fb.getFriends();
            for (Friend friend : friends) {
               Record1<Integer> r = dsl.select(USER.ID).from(USER).where(USER.OAUTH_NAME.eq(friend.getId())).fetchAny();
               if(r!=null){
                   dsl.insertInto(BUDDY, BUDDY.USER_ID, BUDDY.BUDDY_ID).values(userId, r.getValue(USER.ID)).execute();
               }else{
                   Person p = fb.getUser(friend.getId());
                   int newUser = dsl
                   .insertInto(USER,
                               USER.OAUTH_NAME,
                               USER.OAUTH_PROVIDER,
                               USER.FIRST_NAME,
                               USER.LAST_NAME,
                               USER.URL
                               )
                    .values(p.getId(),
                            "facebook",
                            p.getFirst_name(),
                            p.getLast_name(),
                            createUserURL(p)
                            ).returning()
                    .execute();
                   dsl.insertInto(BUDDY, BUDDY.USER_ID, BUDDY.BUDDY_ID).values(userId, newUser).execute();
               }
            }
            conn.commit();
            
        } catch (RestException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            app.closeConnection(conn);
        }
       
        
    }
    
    private String createUserURL(Person p) {
        //in order to be complete, 
        //we should check that this URL is not in the database yet:
        //if that would be the case we could add an incrementing number to assure uniqueness
        return p.getFirst_name().toLowerCase().replace(" ", "_") + "." + p.getLast_name().toLowerCase().replace(" ", "_");
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


