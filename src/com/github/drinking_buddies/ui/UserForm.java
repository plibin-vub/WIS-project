package com.github.drinking_buddies.ui;

import static com.github.drinking_buddies.jooq.Tables.ADDRESS;
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
import org.jooq.InsertResultStep;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Result;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.db.DBUtils;
import com.github.drinking_buddies.entities.Address;
import com.github.drinking_buddies.entities.Bar;
import com.github.drinking_buddies.entities.Beer;
import com.github.drinking_buddies.entities.User;
import com.github.drinking_buddies.jooq.tables.records.UserRecord;
import com.github.drinking_buddies.jooq.utils.SearchUtils;
import com.github.drinking_buddies.jwt.ShareLocationHandler;
import com.github.drinking_buddies.ui.utils.TemplateUtils;
import com.github.drinking_buddies.webservices.facebook.Facebook;
import com.github.drinking_buddies.webservices.facebook.Friend;
import com.github.drinking_buddies.webservices.facebook.Person;
import com.github.drinking_buddies.webservices.rest.exceptions.RestException;

import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.Signal2;
import eu.webtoolkit.jwt.WAnchor;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WImage;
import eu.webtoolkit.jwt.WLink;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WString;
import eu.webtoolkit.jwt.WTemplate;

//The user form widget.
//This form shows user information and location, favorite beers and favorite bars.
public class UserForm extends WContainerWidget {
    private static int maxFavorites = 5;
    
    private User user;
    
    private WTemplate main;

    private WPushButton share;

    private WPushButton stopShare;
    
    public UserForm(User user, Bar currentBar) {
        this.user = user;
        
        main = new WTemplate(tr("user-form"), this);
        TemplateUtils.configureDefault(Application.getInstance(), main);
        main.bindString("user-pic-url", user.getLargeImageUrl());
        main.bindString("first-name", user.getFirstName());
        main.bindString("last-name", user.getLastName());
        
        //widgets that allow the user to share his/here location
        if (isLoggedInUser(user)) {
            share = new WPushButton(tr("user-form.share-location"));
            stopShare = new WPushButton(tr("user-form.stop-share-location"));
            stopShare.clicked().addListener(this, new Signal.Listener() {
                    public void trigger() {
                        removeUserBar(); 
                    }
        
    
            });
            ShareLocationHandler slh = new ShareLocationHandler(main, share);
            main.bindWidget("share-location", share);
            main.bindWidget("stop-share-location", stopShare);
            slh.locationShared().addListener(this, new Signal2.Listener<String, String>() {
                public void trigger(String arg1, String arg2) {
                    Integer barId=SearchUtils.getClosesedBarId(Double.parseDouble(arg1), Double.parseDouble(arg2)) ;
                    if(barId!=null){
                        updateUserWithBar(barId);
                        
                    }
                    System.err.println(arg1+"-"+arg2+" barId:"+barId);
                }
            });
            
            //button that allows for a user to import this friends
            WPushButton friendImport = new WPushButton(tr("user-form.friend-import"));
            main.bindWidget("friend-import", friendImport);
            friendImport.clicked().addListener(this, new Signal.Listener() {
                public void trigger() {
                                friendImport(); 
                        }
            });
            WPushButton logout = new WPushButton(tr("user-form.logout"));
            logout.clicked().addListener(this, new Signal.Listener() {
                public void trigger() {
                    Application.getInstance().logout();
                    TemplateUtils.setHeader(Application.getInstance(), main);
                        }
            });
            main.bindWidget("logout", logout);
        } else {
            main.bindWidget("share-location", null);
            main.bindWidget("stop-share-location", null);
            main.bindWidget("friend-import", null);
            main.bindWidget("logout", null);
        }
        setDrinkingInBar(currentBar);
        updateFavorites(main);
        
        WAnchor anchor=new WAnchor(new WLink(WLink.Type.InternalPath,"/"+ Application.FIND_NEARBY_FRIENDS_URL),tr("user-form.find-nearby-friends"));
        main.bindWidget("find-nearby-friends", anchor);
        WAnchor findBars=new WAnchor(new WLink(WLink.Type.InternalPath,"/"+ Application.FIND_NEARBY_BARS_URL),tr("user-form.find-nearby-bars"));
        main.bindWidget("find-nearby-bars", findBars);
        
    }
    
    //Remove the user's current location.
    //(Stop sharing was clicked)
    protected void removeUserBar() {
        Application app = Application.getInstance();
        Connection conn = app.getConnection();
        DSLContext dsl = DBUtils.createDSLContext(conn);
        int userId = app.getLoggedInUser().getId();
        try{
            dsl.update(USER).set(USER.BAR_ID,0).where(USER.ID.eq(app.getLoggedInUser().getId())).execute();  
            conn.commit();
            setDrinkingInBar(null);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            app.closeConnection(conn);
        }
    }

    //Show the user's current bar location in the user interface.
    public void setDrinkingInBar(Bar currentBar){
        if (currentBar == null) {
            main.bindWidget("is-drinking-in", null);
            if(isLoggedInUser(user)){
                stopShare.hide();
                share.show();
            }
           
        } else {
            WString drinking = tr("user-form.is-drinking-in");
            drinking
                .arg(currentBar.getName())
                .arg(currentBar.getAddress().getCity())
                .arg(currentBar.getAddress().getCountry());
            WTemplate t = new WTemplate(tr("is-drinking-in"));
            t.bindString("text", drinking);
            main.bindWidget("is-drinking-in", t);
            if(isLoggedInUser(user)){
                share.hide();
                stopShare.show();
            }
        }
    }
    
    //Fetch the user's current location,
    //look up with which bar this corresponds.
    //If this corresponds with a bar,
    //show this bar in the UI with setDrinkingInBar()
    protected void updateUserWithBar(Integer barId) {
        Application app = Application.getInstance();
        Connection conn = app.getConnection();
        DSLContext dsl = DBUtils.createDSLContext(conn);
        int userId = app.getLoggedInUser().getId();
        try{
            dsl.update(USER).set(USER.BAR_ID,barId).where(USER.ID.eq(app.getLoggedInUser().getId())).execute();  
            conn.commit();
            Record barRecord
            = dsl
            .select(BAR.ID,BAR.NAME,BAR.WEBSITE,BAR.URL,BAR.PHOTO, BAR.PHOTO_MIME_TYPE, ADDRESS.ID,ADDRESS.STREET,ADDRESS.NUMBER,ADDRESS.ZIPCODE,ADDRESS.CITY,ADDRESS.COUNTRY)
            .from(BAR,ADDRESS)
            .where(BAR.ID.eq(barId))
            .and(ADDRESS.ID.eq(BAR.ADDRESS_ID))
            .fetchOne();
            Bar bar=null;
            if(barRecord!=null){
                Address address=new Address(barRecord.getValue(ADDRESS.ID), barRecord.getValue(ADDRESS.STREET), barRecord.getValue(ADDRESS.NUMBER)
                        , barRecord.getValue(ADDRESS.ZIPCODE), barRecord.getValue(ADDRESS.CITY), barRecord.getValue(ADDRESS.COUNTRY));
                bar = new Bar(barRecord.getValue(BAR.ID),barRecord.getValue(BAR.NAME),0,0 , barRecord.getValue(BAR.WEBSITE),null, address,barRecord.getValue(BAR.URL)) ;
                
            }
            setDrinkingInBar(bar);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            app.closeConnection(conn);
        }
    }

    //import Facebook friends as buddies into our database.
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
                   UserRecord newUser = dsl
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
                            ).returning().fetchOne()
                    ;
                   dsl.insertInto(BUDDY, BUDDY.USER_ID, BUDDY.BUDDY_ID).values(userId, newUser.getId()).execute();
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
    
    //create a unique URL for a user
    private String createUserURL(Person p) {
        //in order to be complete, 
        //we should check that this URL is not in the database yet:
        //if that would be the case we could add an incrementing number to assure uniqueness
        return p.getFirst_name().toLowerCase().replace(" ", "_") + "." + p.getLast_name().toLowerCase().replace(" ", "_");
    }

    //update the table of favorite beers/bars
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
    
    //Delete one of the user's favorite beers from the database,
    //update the user interface.
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
    
    //Delete one of the user's favorite bars from the database,
    //update the user interface.
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


