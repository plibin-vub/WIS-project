package com.github.drinking_buddies;

import static com.github.drinking_buddies.jooq.Tables.ADDRESS;
import static com.github.drinking_buddies.jooq.Tables.BAR;
import static com.github.drinking_buddies.jooq.Tables.BEER;
import static com.github.drinking_buddies.jooq.Tables.BEER2_BEER_TAG;
import static com.github.drinking_buddies.jooq.Tables.BEER_TAG;
import static com.github.drinking_buddies.jooq.Tables.FAVORITE_BAR;
import static com.github.drinking_buddies.jooq.Tables.FAVORITE_BEER;
import static com.github.drinking_buddies.jooq.Tables.REVIEW;
import static com.github.drinking_buddies.jooq.Tables.USER;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Result;

import com.github.drinking_buddies.config.Configuration;
import com.github.drinking_buddies.db.DBUtils;
import com.github.drinking_buddies.entities.Address;
import com.github.drinking_buddies.entities.Bar;
import com.github.drinking_buddies.entities.Beer;
import com.github.drinking_buddies.entities.Image;
import com.github.drinking_buddies.entities.Review;
import com.github.drinking_buddies.entities.Tag;
import com.github.drinking_buddies.entities.User;
import com.github.drinking_buddies.jooq.queries.BarQueries;
import com.github.drinking_buddies.ui.BarForm;
import com.github.drinking_buddies.ui.BarSearchForm;
import com.github.drinking_buddies.ui.BeerForm;
import com.github.drinking_buddies.ui.BeerSearchForm;
import com.github.drinking_buddies.ui.NearbyBarsForm;
import com.github.drinking_buddies.ui.StartForm;
import com.github.drinking_buddies.ui.UserForm;
import com.github.drinking_buddies.webservices.brewerydb.BreweryDb;
import com.github.drinking_buddies.webservices.rest.exceptions.RestException;

import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WBootstrapTheme;
import eu.webtoolkit.jwt.WEnvironment;
import eu.webtoolkit.jwt.WLink;
import eu.webtoolkit.jwt.WXmlLocalizedStrings;

public class Application extends WApplication {
    private ServletContext servletContext;
    private Configuration configuration;
    
    private User loggedInUser;

    
    public Application(WEnvironment env, ServletContext servletContext) {
        super(env);
        this.useStyleSheet(
                new WLink("styles/app1.css"));
        this.servletContext = servletContext;
        
        setTheme(new WBootstrapTheme());
        
        this.useStyleSheet(new WLink("styles/app2.css"));
        
        WXmlLocalizedStrings resources = new WXmlLocalizedStrings();
        resources.use("/com/github/drinking_buddies/ui/templates");
        resources.use("/com/github/drinking_buddies/i18n/resources");
        this.setLocalizedStrings(resources);
        
        setTitle(tr("app.title"));
        
        this.internalPathChanged().addListener(this, new Signal1.Listener<String>(){
            @Override
            public void trigger(String internalPath) {
                handleInternalPath(internalPath);
            }            
        });

        configuration = Configuration.loadConfiguration();
        
        handleInternalPath(getInternalPath());
    }
    
    private String[] split(String internalPath) {
        //remove the first slash: 
        //-if the internalpath is empty the array will be empty
        //-if the internalpath is not empty the array will contain all words in the path
        internalPath = internalPath.substring(1);
        return internalPath.split("/");
    }
    
    public static String BEERS_URL = "beers";
    public static String BARS_URL = "bars";
    public static String FIND_NEARBY_FRIENDS_URL = "find_nearby_friends";
    public static String FIND_NEARBY_BARS_URL = "find_nearby_bars";
    
    private void handleInternalPath(String ip) {
        String[] parts = split(ip);
        
        getRoot().clear();
        
        if ("".equals(parts[0])) {
            getRoot().addWidget(new StartForm());
            return;
        }
            
        if (BEERS_URL.equals(parts[0])) {
            if (parts.length > 1) {
                final String beerURL = parts[1];
                
                Integer id = null;
                String beerName = null;
                String beerWSName = null;
                Integer favoredBy = null;
                List<Tag> tags = new ArrayList<Tag>();
                List<Review> reviews = new ArrayList<Review>();
                
                Connection conn = null;
                try {
                    conn = DBUtils.getConnection();
                    DSLContext dsl = DBUtils.createDSLContext(conn);
                    
                    Record r 
                        = dsl
                            .select(BEER.ID, BEER.NAME, BEER.WEBSERVICE_NAME)
                            .from(BEER)
                            .where(BEER.URL.eq(beerURL))
                            .fetchOne();
                    id = r.getValue(BEER.ID);
                    beerName = r.getValue(BEER.NAME);
                    beerWSName = r.getValue(BEER.WEBSERVICE_NAME);
                    
                    favoredBy
                        = dsl
                            .select()
                            .from(FAVORITE_BEER)
                            .where(FAVORITE_BEER.BEER_ID.equal(id))
                            .fetchCount();
                    
                    Result<Record2<Integer, String>> results 
                        = dsl
                            .select(BEER_TAG.ID, BEER_TAG.NAME)
                            .from(BEER_TAG)
                            .join(BEER2_BEER_TAG)
                            .on(BEER_TAG.ID.equal(BEER2_BEER_TAG.BEER_TAG_ID))
                            .where(BEER2_BEER_TAG.BEER_ID.equal(id))
                            .orderBy(BEER_TAG.ID)
                            .fetch();
                    
                    for (Record2<Integer, String> r2 : results) {
                        tags.add(new Tag(r2.value1(), r2.value2()));
                    }
                    
                    Result<Record> reviewResults 
                    = dsl
                        .select()
                        .from(REVIEW)
                        .where(REVIEW.BEER_ID.equal(id))
                        .orderBy(REVIEW.POST_TIME)
                        .fetch();
                    
                    for (Record rr : reviewResults) {
                        Record ur = dsl.select().from(USER).where(USER.ID.equal(rr.getValue(REVIEW.USER_ID))).fetchOne();
                        reviews.add(new Review(rr, ur));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                } finally {
                    DBUtils.closeConnection(conn);
                }
                
                com.github.drinking_buddies.webservices.brewerydb.Beer beer_from_brewerydb;
                try {
                    beer_from_brewerydb = BreweryDb.getBeer(beerWSName);
                    Beer b = new Beer(id, 
                            beerName, 
                            beer_from_brewerydb.getMainBrewery(), 
                            favoredBy, 
                            beer_from_brewerydb.getAbv(),
                            beer_from_brewerydb.getMediumLabelUrl());
                    getRoot().addWidget(new BeerForm(b, tags, reviews));
                } catch (RestException e) {
                    e.printStackTrace();
                }
            } else {
                getRoot().addWidget(new BeerSearchForm());
            }
        } else {
            //show 404
        }
        if ("users".equals(parts[0])) {
            if (parts.length > 0) {
                String url = parts[1];
                 
                 Connection conn = null;
                 try {
                     conn = DBUtils.getConnection();
                     DSLContext dsl = DBUtils.createDSLContext(conn);
                     Record r = 
                             dsl
                                 .select()
                                 .from(USER)
                                 .where(USER.URL.equal(url))
                                 .fetchOne();
                     if (r != null) {
                         getRoot().addWidget(new UserForm(new User(r), null));
                     } else {
                         throw new RuntimeException("Unknown user URL");
                     }
                 } catch (Exception e) {
                     e.printStackTrace();
                     throw new RuntimeException(e);
                 } finally {
                     DBUtils.closeConnection(conn);
                 }
            }
        } else {
            //show 404
        }
        if (BARS_URL.equals(parts[0])) {
            if(parts.length>1) {
                final String barURL = parts[1];
                Bar bar=null;
                     
                     Connection conn = null;
                     try {
                         conn = DBUtils.getConnection();
                         DSLContext dsl = DBUtils.createDSLContext(conn);
                         
                         Record r 
                             = dsl
                                 .select(BAR.ID,BAR.NAME,BAR.WEBSITE,BAR.PHOTO,ADDRESS.ID,ADDRESS.STREET,ADDRESS.NUMBER,ADDRESS.ZIPCODE,ADDRESS.CITY,ADDRESS.COUNTRY)
                                 .from(BAR,ADDRESS)
                                 .where(BAR.URL.eq(barURL))
                                 .and(ADDRESS.ID.eq(BAR.ADDRESS_ID))
                                 .fetchOne();
                         if(r==null){
                             //TODO show error message in stead of 404
                             show404();
                             return;
                         }
                         int id=r.getValue(BAR.ID);
                         int favoredBy
                         = dsl
                             .select()
                             .from(FAVORITE_BAR)
                             .where(FAVORITE_BAR.BAR_ID.equal(id))
                             .fetchCount();
                         BigDecimal score= BarQueries.getAvgScore(dsl, id);
                         Address address=new Address(r.getValue(ADDRESS.ID), r.getValue(ADDRESS.STREET), r.getValue(ADDRESS.NUMBER)
                                 , r.getValue(ADDRESS.ZIPCODE), r.getValue(ADDRESS.CITY), r.getValue(ADDRESS.COUNTRY));
                         Image barPhoto = null; //TODO
                         double scoreValue=0;
                         if(score!=null){
                             scoreValue=score.doubleValue();
                         }
                         bar = new Bar(id,r.getValue(BAR.NAME),favoredBy,scoreValue , r.getValue(BAR.WEBSITE),barPhoto, address,barURL) ;
                         }
                      catch (Exception e) {
                         e.printStackTrace();
                         throw new RuntimeException(e);
                     } finally {
                         DBUtils.closeConnection(conn);
                     }

                getRoot().addWidget(new BarForm(bar));
            } else {
                getRoot().addWidget(new BarSearchForm());
            }
          } else {
              show404();
          }
          if ("nearby_bars".equals(parts[0])) {
              getRoot().addWidget(new NearbyBarsForm());
          }
    }
    
    
    private void show404() {
        // TODO Auto-generated method stub
        
    }

    public static Application getInstance() {
        return (Application)WApplication.getInstance();
    }
    
    public ServletContext getServletContext() {
        return servletContext;
    }
    
    public Configuration getConfiguration() {
        return configuration;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void login(User user) {
        loggedInUser = user;
    }
    
    public void internalRedirect(String relativePath) {
        setInternalPath(relativePath, true);
    }
    
    public Connection getConnection() {
        try {
            return DBUtils.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void searchBeers(String search) {
        this.setInternalPath("/" + BEERS_URL, false);
        
        getRoot().clear();
        getRoot().addWidget(new BeerSearchForm(search));
    }
    
    public void searchBars(String search) {
        this.setInternalPath("/" + BARS_URL, false);
        
        getRoot().clear();
        getRoot().addWidget(new BarSearchForm(search));
    }

    public void closeConnection(Connection conn) {
        DBUtils.closeConnection(conn);
    }
}
